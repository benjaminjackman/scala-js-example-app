package cgta.sjs
package lang

import scala.scalajs.js
import scala.concurrent.Future
import scala.scalajs.js.annotation.JSName


//////////////////////////////////////////////////////////////
// Copyright (c) 2013 Ben Jackman, Jeff Gomberg
// All Rights Reserved
// please contact ben@jackman.biz or jeff@cgtanalytics.com
// for licensing inquiries
// Created by bjackman @ 12/11/13 12:18 AM
//////////////////////////////////////////////////////////////



trait SJSDsl extends JSExtensions {

  def global = js.Dynamic.global
  def console = global.console.asInstanceOf[JSConsole]
  def JSON = global.JSON
  def newObject = js.Object().asInstanceOf[js.Dynamic]



  //Converts a callback style into a future
  //el.on("click", (x) => console.log(x))
  //decant(el.on("click", _)).onSuccess(console.log(_))
  def decant1[A](cb0 : ((A) => Unit) => Unit) : Future[A] = {
    val p = JSPromise[A]()
    def cb(a : A) {
      p.success(a)
    }
    cb0(cb)
    p.future
  }
  def decant0(cb0 : (() => Unit) => Unit) : Future[Unit] = {
    val p = JSPromise[Unit]()
    def cb() {
      p.success(Unit)
    }
    cb0(cb)
    p.future
  }


  implicit val jsExecutionContext = JSFuture.InternalCallbackExecutor

}
