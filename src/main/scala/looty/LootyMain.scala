package looty

import scala.scalajs.js
import looty.model.{ComputedItem, ItemParser, PoeCacher}
import looty.poeapi.PoeTypes.Leagues
import looty.views.LootGrid
import scala.collection.mutable.ArrayBuffer
import cgta.ojs


//////////////////////////////////////////////////////////////
// Copyright (c) 2013 Ben Jackman, Jeff Gomberg
// All Rights Reserved
// please contact ben@jackman.biz or jeff@cgtanalytics.com
// for licensing inquiries
// Created by bjackman @ 12/9/13 11:22 PM
//////////////////////////////////////////////////////////////


object JsPoint {
  implicit class JsPointExtensions(val p: JsPoint) extends AnyVal {
    def magnitude = js.Math.sqrt(p.x * p.x + p.y * p.y)
  }
}
class JsPoint(val x: js.Number, val y: js.Number) extends js.Object
case class Point(x: Int, y: Int)

object LootyMain {

  def loadLooty() {
    val items = new js.Array[ComputedItem]()

    val pc = new PoeCacher()

    val parseFuture = for {
      tabInfos <- pc.getStashInfo(Leagues.Standard)
      tabs <- pc.getAllStashTabs(Leagues.Standard)
      invs <- pc.getAllInventories(Leagues.Standard)
    } yield {
      for {
        (tab, i) <- tabs.zipWithIndex
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

    val grid = new LootGrid

    parseFuture.onComplete { (x) =>
      grid.start(items)
    }

    //    parseFuture.onComplete {
    //      (x) =>
    //        grid.start(items)
    ////        items.sortBy(-_.score.score).foreach { ci =>
    ////          console.log(ci.location, ci.score.toString, ci.item.name)
    ////        }
    //    }

  }

  //    *  foo.method("blah")      ~~> foo.applyDynamic("method")("blah")
  //    *  foo.method(x = "blah")  ~~> foo.applyDynamicNamed("method")(("x", "blah"))
  //    *  foo.method(x = 1, 2)    ~~> foo.applyDynamicNamed("method")(("x", 1), ("", 2))
  //    *  foo.field           ~~> foo.selectDynamic("field")
  //    *  foo.varia = 10      ~~> foo.updateDynamic("varia")(10)
  //    *  foo.arr(10) = 13    ~~> foo.selectDynamic("arr").update(10, 13)
  //    *  foo.arr(10)         ~~> foo.applyDynamic("arr")(10)


  def tryJsObj() {
    val o = ojs.obj
    console.log("Begin Main!")
    val foo = ojs.obj(
      x = 5,
      y = 5,
      zs = ojs.arr(1, 2, 3),
      foo = ojs.obj(
        b = 6,
        c = 7)
    )
    console.log(o())
    console.log(JSON.stringify(foo))
    //Prints {"x":5,"y":5,"zs":[1,2,3],"foo":{"b":6,"c":7}}

    //Note that jsObj can also be given a type parameter
    //that type will be used as the return type,
    //However it's just a NOP and doesn't do real type
    //safety, I think it might be possible to do with
    //a macro, howevr i think a better approach would
    //be to do it as part of the actual ScalaJs backend.
    //using the new keyword.

    val xs = ArrayBuffer[Int](1, 2, 3, 4, 5, 6)
    val ys: js.Array[js.Number] = xs.map(x => x: js.Number).toArray[js.Number]: js.Array[js.Number]
    console.log(ys)
    console.log(Point(1, 3))
    console.log(ojs.obj[JsPoint](x = 1, y = 2).magnitude)
  }



  def main(args: Array[String]) {
    //tryJsObj()
    loadLooty()
  }

}