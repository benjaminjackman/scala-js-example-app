package looty

import scala.scalajs.js.Dynamic
import looty.views.LootGrid
import scala.scalajs.js
import looty.services.PoeRpcService
import scala.concurrent.ExecutionContext


//////////////////////////////////////////////////////////////
// Copyright (c) 2013 Ben Jackman, Jeff Gomberg
// All Rights Reserved
// please contact ben@jackman.biz or jeff@cgtanalytics.com
// for licensing inquiries
// Created by bjackman @ 12/9/13 11:22 PM
//////////////////////////////////////////////////////////////


object LootyMain {

  import scala.util.continuations._

  implicit object JsExecutionContext extends ExecutionContext {
    def execute(runnable: Runnable): Unit = {
      runnable.run()
    }

    def reportFailure(t: Throwable): Unit = {
      throw t
    }
  }


  def main(args: Array[String]) {
    console.log("Hello World! Looty Main!")
    //Right now just show the loot grid page
    PoeRpcService.getCharacters().foreach(chars => console.log(chars))

    //    def foo() = {
    //    println("Once here.")
    //    shift((k: Int => Int) => k(k(k(7))))
    //    }
    //    def bar() = {
    //    1+ foo()
    //    }
    //    def baz() = {
    //    reset(bar() * 2)
    //    }
    //    baz()  // result 70


  }

}