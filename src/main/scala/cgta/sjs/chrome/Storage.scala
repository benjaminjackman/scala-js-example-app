package cgta.sjs.chrome

import scala.scalajs.js.annotation.JSName
import scala.scalajs.js


//////////////////////////////////////////////////////////////
// Copyright (c) 2013 Ben Jackman, Jeff Gomberg
// All Rights Reserved
// please contact ben@jackman.biz or jeff@cgtanalytics.com
// for licensing inquiries
// Created by bjackman @ 12/11/13 12:49 AM
//////////////////////////////////////////////////////////////


@JSName("chrome.storage")
object Storage extends js.Object {
  @JSName("chrome.storage.local")
  object local extends js.Object {
    val QUOTA_BYTES: js.Number = ???
    def clear() = ???
    def clear(cb: js.Function1[js.Any, Unit]) = ???

    def set(items: js.Any) = ???
    def set(items: js.Any, cb: js.Function0[Unit]) = ???

    //    def get(key: js.String) = ???
    //    def get(key: js.String, cb: js.Dynamic => Unit) = ???
    //
    //    def get(keys: js.Array[js.String]) = ???
    //    def get(keys: js.Array[js.String], cb: js.Dynamic => Unit) = ???

    def get(keys: js.Any) = ???
    def get[B <: js.Any](keys: js.Any, cb: js.Function1[B, Unit]) = ???

    def remove(keys: js.Any, cb: js.Function0[Unit]) = ???

  }

}