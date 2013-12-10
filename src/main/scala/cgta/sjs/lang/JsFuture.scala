package cgta.sjs
package lang

import scala.scalajs.js


//////////////////////////////////////////////////////////////
// Copyright (c) 2013 Ben Jackman, Jeff Gomberg
// All Rights Reserved
// please contact ben@jackman.biz or jeff@cgtanalytics.com
// for licensing inquiries
// Created by bjackman @ 12/9/13 11:54 PM
//////////////////////////////////////////////////////////////


object JsFuture {
  def fromAPlus[A](that: js.Dynamic): JsFuture[A] = {
    //See http://promises-aplus.github.io/promises-spec/
    PRINT | "JsDynamic"
    PRINT | that
    new JsFuture[A] {
      //Uses continuations to return the result in what appears to be a synchronous fashion
      //If there was an error an exception is thrown
      override def get: A = {
        ???
      }

      override def foreach(f: A => Unit) {
        that.`then` {
          data: js.Any =>
            f(data.asInstanceOf[A])
        }
      }
    }
  }

}

trait JsFuture[A] {
  //Uses continuations to return the result in what appears to be a synchronous fashion
  //Exceptions will propagate normally etc.
  def get: A

  def foreach(f: A => Unit): Unit

  def map[B](f: A => B): JsFuture[B] = {
//    val d = new JsPromise()
//    foreach {
//      x =>
//        f(x)
//    }
//    d.future
    ???
  }
}

class JsPromise[A] {
  var value: Option[A] = None

  def future: JsFuture[A] = ???
}

