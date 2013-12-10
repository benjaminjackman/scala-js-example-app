package looty
package services

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



object PoeRpcService {
  val jQuery = global.jQuery.asInstanceOf[JQueryStatic]

  private def get[A](url: String, params: js.Any): Future[js.Array[A]] = {
    AjaxHelp(url, AjaxHelp.HttpRequestTypes.Post, params.nullSafe.map(s => jQuery.param(s)))
  }

  trait CharacterInfo extends js.Object {
    val `class`: String
    val classId: Int
    val league: String
    val level: Int
    val name: String
  }
  def getCharacters(): Future[js.Array[CharacterInfo]] = {
    get(url = "http://www.pathofexile.com/character-window/get-characters", params = null)
  }
}