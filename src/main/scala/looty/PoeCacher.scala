package looty

import looty.poeapi.PoeRpcs
import cgta.sjs.chrome.Storage
import scala.concurrent.Future
import looty.poeapi.PoeTypes.Characters


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
class PoeCacher(account : String = "UnknownAccount!") {


  private var _characters : Characters = _

  def init(): Future[Unit] = {
    //Will init data from local storage
    Storage.local.get("characterData", (data : Characters) => {

    })

  }
  def basicRefresh(): Future[Unit] = {
    ???
  }
  def clear(): Future[Unit] = {
    ???
  }
  def fullRefresh(): Future[Unit] = {
    ???
  }


//  def storeCharacterData(): Future[Unit] = {
//    PoeRpcs.getCharacters().flatMap { data =>
//      val cd = newObject
//      cd.characterData = data
//      console.log("Storing!")
//      decant0(Storage.local.set(cd, _))
//    }
//  }
//
//  def storeCharacterInventories(): Future[Unit] = {
//    //TODO Convert to toFuture style
//    decant1(Storage.local.get[Characters]("characterData", (c: Characters) => {})).log().flatMap { data =>
//
//    }
//  }


}