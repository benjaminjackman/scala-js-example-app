package looty

import scala.scalajs.js
import looty.poeapi.PoeRpcs
import cgta.sjs.lang.Nullable
import scala.concurrent.ExecutionContext


//////////////////////////////////////////////////////////////
// Copyright (c) 2013 Ben Jackman, Jeff Gomberg
// All Rights Reserved
// please contact ben@jackman.biz or jeff@cgtanalytics.com
// for licensing inquiries
// Created by bjackman @ 12/9/13 11:22 PM
//////////////////////////////////////////////////////////////


object LootyMain {


  trait TryNullable extends js.Object {
    val x: js.Number
    val y: Nullable[js.Number]
  }

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
    //    PoeRpcs.getCharacters().foreach(chars => console.log(chars))

    //    val s = js.Dictionary("x" -> 5).asInstanceOf[TryNullable]
    //    console.log(s.y.get)

    //    val x = js.Dictionary().asInstanceOf[js.Dynamic]
    //    s"${x.y}"

    val x = js.Dictionary("y" -> null).asInstanceOf[js.Dynamic]
    s"${x.y}" //throws:Uncaught TypeError: Cannot call method 'toString' of null

    //  import scala.util.continuations._
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