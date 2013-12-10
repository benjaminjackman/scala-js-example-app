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


object JsPromise {
  def fromAPlus[A](that : js.Dynamic) : JsPromise[A] = {
    //See http://promises-aplus.github.io/promises-spec/
    PRINT | "JsDynamic"
    PRINT | that
    new JsPromise[A] {
      //Uses continuations to return the result in what appears to be a synchronous fashion
      //If there was an error an exception is thrown
      def get: A = {
        ???
      }
    }
  }

}

trait JsPromise[A] {
  //Uses continuations to return the result in what appears to be a synchronous fashion
  //Exceptions will propagate normally etc.
  def get : A
}