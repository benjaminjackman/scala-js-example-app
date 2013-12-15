package looty

import scala.scalajs.js
import looty.model.{ComputedItem, ItemParser, PoeCacher}
import cgta.cjs.lang.JsPromise


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

    console.log("hi".cap)
    val items = new js.Array[ComputedItem]()

    val pc = new PoeCacher()

    val parseFuture = for {
      tabs <- pc.getAllStashTabs("Standard")
    } yield {
      for {
        tab <- tabs
        item <- tab.items
      } {
        items.push(ItemParser.parseItem(item))
      }
    }

    parseFuture.onComplete {
      (x) =>
        console.log("Done parsing items", items)
        console.log(items.toList.map(_.maxLinks).toArray)
    }


  }

}