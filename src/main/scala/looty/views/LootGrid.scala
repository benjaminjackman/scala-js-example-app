package looty
package views

import org.scalajs.jquery.JQueryStatic
import scala.scalajs.js
import looty.model.{Elements, ComputedItem}


//////////////////////////////////////////////////////////////
// Copyright (c) 2013 Ben Jackman, Jeff Gomberg
// All Rights Reserved
// please contact ben@jackman.biz or jeff@cgtanalytics.com
// for licensing inquiries
// Created by bjackman @ 12/9/13 11:17 PM
//////////////////////////////////////////////////////////////


class LootGrid() {
  val jq: JQueryStatic = global.jQuery.asInstanceOf[JQueryStatic]
  def start(items: js.Array[ComputedItem]) {
    jq("#content").empty()
    def makeColumn(name: String)(f: ComputedItem => js.Any) = {
      val o = newObject
      o.id = name
      o.name = name
      o.field = name
      o.toolTip = name
      o.sortable = true
      o.getter = f
      o
    }
    val columns = js.Array[js.Dynamic](
      makeColumn("loc")(_.location),
      makeColumn("name")(_.item.name),
      makeColumn("typeName")(_.typeName),
      makeColumn("score")(_.score.score),
      makeColumn("dps")(_.total.dps.round),
      makeColumn("AR")(_.properties.armour),
      makeColumn("EV")(_.properties.evasionRating),
      makeColumn("ES")(_.properties.energyShield),
      makeColumn("Life")(_.plusTo.lifeAndMana.life),
      makeColumn("Mana")(_.plusTo.lifeAndMana.mana),
      makeColumn("pDps")(_.total.perElementDps(Elements.physical).round),
      makeColumn("fDps")(_.total.perElementDps(Elements.fire).round),
      makeColumn("cDps")(_.total.perElementDps(Elements.cold).round),
      makeColumn("lDps")(_.total.perElementDps(Elements.lightning).round)
    )

    val options = {
      val o = newObject
      o.enableCellNavigation = true
      o.enableColumnReorder = false
      o.multiColumnSort = true
      o.dataItemColumnValueExtractor = (item: ComputedItem, column: js.Dynamic) => {
        column.getter(item.asInstanceOf[js.Any])
      }
      o
    }

    val grid = js.Dynamic.newInstance(global.Slick.Grid)("#content", items, columns, options)

    grid.onSort.subscribe((e: js.Dynamic, args: js.Dynamic) => {
      val cols = args.sortCols.asInstanceOf[js.Array[js.Dynamic]]

      items.sort { (a: ComputedItem, b: ComputedItem) =>
        var i = 0
        var ret = 0.0
        while (i < cols.length && ret == 0) {
          val col = cols(i)
          val sign = if (cols(i).sortAsc.asInstanceOf[js.Boolean]) 1 else -1
          val a1: js.Dynamic = col.sortCol.getter(a.asInstanceOf[js.Any])
          val b1: js.Dynamic = col.sortCol.getter(b.asInstanceOf[js.Any])

          val res = a1 - b1
          if (js.isNaN(res)) {
            if (a1.toString > b1.toString) {
              ret = sign
            } else if (b1.toString > a1.toString) {
              ret = -sign
            }

          } else {
            ret = sign * res
          }

          i += 1
        }
        ret: js.Number
      }



      grid.invalidate()
      grid.render()
    })


    def resize() {
      jq("#content").css("height", global.window.innerHeight - 50)
      grid.resizeCanvas()
    }
    jq(global.window).resize(() => resize())

    resize()
  }
}


