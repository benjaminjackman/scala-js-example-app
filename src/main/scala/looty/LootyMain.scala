package looty

import scala.scalajs.js
import looty.data.{PoeItemParser, PoeCacher}


//////////////////////////////////////////////////////////////
// Copyright (c) 2013 Ben Jackman, Jeff Gomberg
// All Rights Reserved
// please contact ben@jackman.biz or jeff@cgtanalytics.com
// for licensing inquiries
// Created by bjackman @ 12/9/13 11:22 PM
//////////////////////////////////////////////////////////////

class Point(x: js.Number, y: js.Number) extends js.Object

object LootyMain {


  def main(args: Array[String]) {
    console.log("Hello World! Looty Main!")

    val pc = new PoeCacher()
    for {
      tabs <- pc.getAllStashTabs("Standard")
      tab <- tabs
      item <- tab.items
    } {
      PoeItemParser.parseItem(item)
    }

  }

}