package cgta.sjs
package lang

import scala.scalajs.js
import scala.concurrent.Future


//////////////////////////////////////////////////////////////
// Copyright (c) 2013 Ben Jackman, Jeff Gomberg
// All Rights Reserved
// please contact ben@jackman.biz or jeff@cgtanalytics.com
// for licensing inquiries
// Created by bjackman @ 12/11/13 12:18 AM
//////////////////////////////////////////////////////////////



trait CgtaJsDsl extends JsExtensions {

  def global = js.Dynamic.global
  def console = global.console.asInstanceOf[JsConsole]
  def JSON = global.JSON
  def newObject = js.Object().asInstanceOf[js.Dynamic]


  //Converts a callback style into a future
  //el.on("click", (x) => console.log(x))
  //decant(el.on("click", _)).onSuccess(console.log(_))
  def decant[A](cb0 : ((A) => Unit) => Unit) : Future[A] = {
    val p = JsPromise[A]()
    def cb(a : A) {
      p.success(a)
    }
    cb0(cb)
    p.future
  }

  implicit val jsExecutionContext = JsFuture.InternalCallbackExecutor

}
