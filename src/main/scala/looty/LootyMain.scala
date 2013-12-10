package looty

import scala.scalajs.js.Dynamic
import looty.views.LootGrid
import scala.scalajs.js
import looty.services.PoeRpcService


//////////////////////////////////////////////////////////////
// Copyright (c) 2013 Ben Jackman, Jeff Gomberg
// All Rights Reserved
// please contact ben@jackman.biz or jeff@cgtanalytics.com
// for licensing inquiries
// Created by bjackman @ 12/9/13 11:22 PM
//////////////////////////////////////////////////////////////


object LootyMain {

  import scala.util.continuations._


  def main(args: Array[String]) {
    Dynamic.global.console.log("Hello World! Looty Main!")
    //Right now just show the loot grid page
    PoeRpcService.getCharacters()

    def foo() = {
    println("Once here.")
    shift((k: Int => Int) => k(k(k(7))))
    }
    def bar() = {
    1+ foo()
    }
    def baz() = {
    reset(bar() * 2)
    }
    baz()  // result 70


  }

}