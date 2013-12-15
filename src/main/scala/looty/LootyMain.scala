package looty

import scala.scalajs.js
import looty.model.{ComputedItem, ItemParser, PoeCacher}
import cgta.cjs.lang.JsPromise
import looty.poeapi.PoeTypes.Leagues


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
      tabInfos <- pc.getStashInfo(Leagues.Standard)
      tabs <- pc.getAllStashTabs(Leagues.Standard)
      invs <- pc.getAllInventories(Leagues.Standard)
    } yield {
      for {
        (tab,i) <- tabs.zipWithIndex
        item <- tab.items
      } {
        val ci = ItemParser.parseItem(item)
        ci.location = tabInfos(i).n
        items.push(ci)
      }

      for {
        (char, inv) <- invs
        item <- inv.items
      } {
        val ci = ItemParser.parseItem(item)
        ci.location = char
        items.push(ci)
      }
    }

    parseFuture.onComplete {
      (x) =>
        items.sortBy(-_.score.score).foreach { ci =>
          console.log(ci.location, ci.score.toString, ci.item.name)
        }
    }


  }

}