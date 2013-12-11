package looty
package poeapi

import scala.scalajs.js
import cgta.sjs.io.AjaxHelp
import org.scalajs.jquery.JQueryStatic
import scala.concurrent.Future


//////////////////////////////////////////////////////////////
// Copyright (c) 2013 Ben Jackman, Jeff Gomberg
// All Rights Reserved
// please contact ben@jackman.biz or jeff@cgtanalytics.com
// for licensing inquiries
// Created by bjackman @ 12/9/13 11:45 PM
//////////////////////////////////////////////////////////////


object PoeRpcs {

  import PoeTypes._

  val jQuery = global.jQuery.asInstanceOf[JQueryStatic]

  private def get[A](url: String, params: js.Any): Future[A] = {
    AjaxHelp(url, AjaxHelp.HttpRequestTypes.Post, params.nullSafe.map(s => jQuery.param(s)))
  }

  def getCharacters(): Future[Characters] = {
    get[js.Array[CharacterInfo]](url = "http://www.pathofexile.com/character-window/get-characters", params = null)
  }

  def getCharacterInventory(character: String): Future[Inventory] = {
    val p = newObject
    p.character = character
    get[Inventory](url = "http://www.pathofexile.com/character-window/get-items", params = p)
  }

  def getStashTab(league: String, tabIndex: Int): Future[StashTab] = {
    val p = newObject
    p.league = league
    p.tabIndex = tabIndex

    get[StashTab](url = "http://www.pathofexile.com/character-window/get-stash-items", params = p)
  }

  def getStashTabInfo(league: String): Future[js.Array[StashTabInfo]] = {
    val p = newObject
    p.league = league
    p.tabIndex = 0
    p.tabs = 1

    get[StashTab](url = "http://www.pathofexile.com/character-window/get-stash-items", params = p).map { stab =>
      stab.tabs.toOption.getOrElse(sys.error(s"Stash tab was not set in ${stab.toJsonString}"))
    }
  }
}