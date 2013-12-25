package looty
package views

import org.scalajs.jquery.JQueryStatic
import scala.scalajs.js
import looty.model.{ComputedItemProps, ItemParser, PoeCacher, ComputedItem}
import looty.poeapi.PoeTypes.Leagues


//////////////////////////////////////////////////////////////
// Copyright (c) 2013 Ben Jackman, Jeff Gomberg
// All Rights Reserved
// please contact ben@jackman.biz or jeff@cgtanalytics.com
// for licensing inquiries
// Created by bjackman @ 12/9/13 11:17 PM
//////////////////////////////////////////////////////////////


class LootGrid() {
  val jq   : JQueryStatic           = global.jQuery.asInstanceOf[JQueryStatic]
  var grid : js.Dynamic             = null
  var items: js.Array[ComputedItem] = null


  def start() {
    val items = new js.Array[ComputedItem]()
    val pc = new PoeCacher()

    val fut = for {
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

      setItems(items)
    }

    fut.log()
  }

  private def setItems(items: js.Array[ComputedItem]) {
    val el = jq("#content")
    el.empty()
    el.append("""<div id="controls"></div>""")
    el.append("""<div id="grid"></div>""")
    appendControls()
    appendGrid(items)

  }
  private def appendControls() {
    val jq: JQueryStatic = global.jQuery.asInstanceOf[JQueryStatic]
    val el = jq("#controls")
    el.empty()

    val pc = new PoeCacher()

    for {
      stis <- pc.Net.getStisAndStore(Leagues.Standard)
      sti <- stis.toList
    } {
      val button = jq(s"""<button style="color: white;text-shadow: -1px 0 black, 0 1px black, 1px 0 black, 0 -1px black;background-color:${sti.colour.toRgb}">${sti.n}</button>""")
      el.append(button)
      button.on("click", (a: js.Any) => {
        //Set the grid to only have this tabs items in it and refresh this tab
        for {
          st <- pc.Net.getStashTabAndStore(Leagues.Standard, sti.i.toInt)
        } {
          val pc = new PoeCacher()
          items = new js.Array[ComputedItem]()
          for {
            item <- st.items
          } {
            val ci = ItemParser.parseItem(item)
            ci.location = sti.n
            items.push(ci)
          }
          console.log(items(0))
          grid.setData(items)
          grid.invalidate()
          grid.render()
        }
      }
      )
    }

  }

  private def appendGrid(items0: js.Array[ComputedItem]) {
    items = items0
    def makeColumn(name: String,tooltip: String)(f: ComputedItem => js.Any) = {
      val o = newObject
      o.id = name
      o.name = name
      o.field = name
      o.toolTip = tooltip
      o.sortable = true
      o.getter = f
      o
    }
    val columns = js.Array[js.Dynamic]()

    ComputedItemProps.all.foreach { p =>
      columns.push(makeColumn(p.shortName, p.description)(p.getJs))
    }

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

    grid = js.Dynamic.newInstance(global.Slick.Grid)("#grid", items, columns, options)
    addSort()
    def resize() {
      jq("#grid").css("height", global.window.innerHeight - 120)
      grid.resizeCanvas()
    }
    jq(global.window).resize(() => resize())

    resize()

  }

  private def addSort() {
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
  }
}


