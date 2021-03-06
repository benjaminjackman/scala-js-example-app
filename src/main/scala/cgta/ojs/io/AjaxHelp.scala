package cgta.ojs
package io

import org.scalajs.jquery.{JQueryAjaxSettings, JQueryStatic}
import scala.scalajs.js
import cgta.ojs.lang.{JsFuture, JsPromise}
import scala.concurrent.Future

//////////////////////////////////////////////////////////////
// Copyright (c) 2013 Ben Jackman, Jeff Gomberg
// All Rights Reserved
// please contact ben@jackman.biz or jeff@cgtanalytics.com
// for licensing inquiries
// Created by bjackman @ 12/9/13 11:15 PM
//////////////////////////////////////////////////////////////


object AjaxHelp {
  val jQuery: JQueryStatic = global.jQuery.asInstanceOf[JQueryStatic]

  object HttpRequestTypes extends Enumeration {
    type HttpRequestType = Value
    val Get = Value("Get")
    val Post = Value("Post")
    val Put = Value("Put")
    val Delete = Value("Delete")
  }

  def apply[A](url: String, requestType: HttpRequestTypes.HttpRequestType, data: Option[String]): Future[A] = {
    JsFuture.wrapPromisesAPlus[A] {
      val req = js.Dictionary(
        "url" -> url,
        "type" -> requestType.toString
      )
      data.foreach(data => req("data") = data)
      jQuery.ajax(req.asInstanceOf[JQueryAjaxSettings])
    }
  }

  def get[A](url : String): Future[A] = apply(url, HttpRequestTypes.Get, None)
}