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

  private def get[A](url: String, params: js.Any): Future[js.Array[A]] = {
    AjaxHelp(url, AjaxHelp.HttpRequestTypes.Post, params.nullSafe.map(s => jQuery.param(s)))
  }

  def getCharacters(): Future[js.Array[CharacterInfo]] = {
    get(url = "http://www.pathofexile.com/character-window/get-characters", params = null)
  }




  def getStashTab(league: String, tabIndex: Int): Future[StashTab] = {
    //    var url = "http://www.pathofexile.com/character-window/get-stash-items";
    //    return this.getItems<StashTab>(url, {league: league, tabIndex: tabIndex, tabs: 1}).then(function (stashTab:StashTab) {
    //      stashTab.info = stashTab.tabs[tabIndex]
    //      delete stashTab.tabs
    //      return stashTab
    //    })
  ???
  }
}