package looty

import looty.poeapi.PoeRpcs
import cgta.cjs.chrome.Storage
import scala.concurrent.Future
import looty.poeapi.PoeTypes.{StashTab, StashTabInfos, StashTabInfo, Inventory, Characters}
import cgta.cjs.lang.JsFuture
import looty.poeapi.PoeTypes.Leagues.League


//////////////////////////////////////////////////////////////
// Copyright (c) 2013 Ben Jackman, Jeff Gomberg
// All Rights Reserved
// please contact ben@jackman.biz or jeff@cgtanalytics.com
// for licensing inquiries
// Created by bjackman @ 12/11/13 12:37 AM
//////////////////////////////////////////////////////////////

/**
 * This class will cache the data from the website in localstorage
 */
class PoeCacher(account: String = "UnknownAccount!") {

  object Store {
    def getChars = Storage.local.futGet[Characters](s"$account-characters")
    def setChars(chars: Characters) = Storage.local.futSet(s"$account-characters", chars)

    def getInv(char: String) = Storage.local.futGet[Inventory](s"$account-$char-inventory")
    def setInv(char: String, inv: Inventory) = Storage.local.futSet(s"$account-$char-inventory", inv)

    def getStis(league: League) = Storage.local.futGet[StashTabInfos](s"$account-$league-stis")
    def setStis(league: League, stis: StashTabInfos) = Storage.local.futSet(s"$account-$league-stis", stis)

    def getStashTab(league: League, tabIdx: Int) =
      Storage.local.futGet[StashTab](s"$account-$league-$tabIdx-stis")
    def setStashTab(league: League, tabIdx: Int, st: StashTab) =
      Storage.local.futSet(s"$account-$league-$tabIdx-stis", st)
  }

  object Net {
    def getCharsAndStore = PoeRpcs.getCharacters() flatMap {
      chars => Store.setChars(chars).map(_ => chars)
    }

    def getInvAndStore(char: String) = PoeRpcs.getCharacterInventory(char) flatMap {
      inv => Store.setInv(char, inv).map(_ => inv)
    }

    def getStisAndStore(league: League) = PoeRpcs.getStashTabInfos(league) flatMap {
      stis => Store.setStis(league, stis).map(_ => stis)
    }

    def getStashTabAndStore(league: League, tabIdx: Int) = PoeRpcs.getStashTab(league, tabIdx) flatMap {
      stis => Store.setStashTab(league, tabIdx, stis).map(_ => stis)
    }
  }


  def getChars(): Future[Characters] = {
    //Attempt to get get the chars from local storage, or else go out to the network and load
    Store.getChars flatMap {
      case Some(chars) => JsFuture(chars)
      case None => Net.getCharsAndStore
    }
  }

  def getInv(char: String): Future[Inventory] = {
    Store.getInv(char) flatMap {
      case Some(inv) => JsFuture(inv)
      case None => Net.getInvAndStore(char)
    }
  }

  def getStashInfo(league: League): Future[StashTabInfos] = {
    Store.getStis(league) flatMap {
      case Some(stis) => JsFuture(stis)
      case None => Net.getStisAndStore(league)
    }
  }

  def getStashTab(league: League, tabIdx: Int) : Future[StashTab] = {
    Store.getStashTab(league, tabIdx) flatMap {
      case Some(st) => JsFuture(st)
      case None => Net.getStashTabAndStore(league, tabIdx)
    }
  }

  def basicRefresh() {

  }


}