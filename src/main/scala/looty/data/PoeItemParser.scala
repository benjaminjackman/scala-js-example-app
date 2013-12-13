package looty
package data

import looty.poeapi.PoeTypes.AnyItem
import scala.scalajs.js
import looty.data.PoeAffixesParsers.AffixParser


//////////////////////////////////////////////////////////////
// Copyright (c) 2013 Ben Jackman, Jeff Gomberg
// All Rights Reserved
// please contact ben@jackman.biz or jeff@cgtanalytics.com
// for licensing inquiries
// Created by bjackman @ 12/13/13 1:52 AM
//////////////////////////////////////////////////////////////

class ComputedItem(baseItem: AnyItem) {

}


object PoeItemParser {
  var failCnt  = 0
  var parseCnt = 0

  def parseItem(item: AnyItem): ComputedItem = {
    val ci = new ComputedItem(item)
    parseExplicitMods(item, ci)
    console.log(s"$parseCnt:$failCnt")
    ci
  }

  def parseExplicitMods(item: AnyItem, ci: ComputedItem) {
    for (emods <- item.explicitMods.toOption.toList; mod: js.String <- emods) yield {
      parseExplicitMod(item, ci, mod)
    }
  }

  def parseExplicitMod(item: AnyItem, ci: ComputedItem, mod: String) {
    var parsed = false
    PoeAffixesParsers.all.toList.foreach { parser =>
      if (parser.parse(mod, ci)) {
        parsed = true
      }
    }
    if (parsed) parseCnt += 1 else failCnt += 1
    if (!parsed) console.log(mod)
  }
}