package looty
package model

import looty.poeapi.PoeTypes.AnyItem
import scala.scalajs.js


//////////////////////////////////////////////////////////////
// Copyright (c) 2013 Ben Jackman, Jeff Gomberg
// All Rights Reserved
// please contact ben@jackman.biz or jeff@cgtanalytics.com
// for licensing inquiries
// Created by bjackman @ 12/13/13 1:52 AM
//////////////////////////////////////////////////////////////

object PoeItemParser {
  var failCnt  = 0
  var parseCnt = 0

  def parseItem(item: AnyItem): ComputedItem = {
    val ci = new ComputedItem(item)
    parseExplicitMods(item, ci)
    ci
  }

  def parseExplicitMods(item: AnyItem, ci: ComputedItem) {
    if (!item.isGem && !item.isCurrency) {
      for {
        emods <- item.explicitMods.toOption.toList
        mod: js.String <- emods} yield {
        parseExplicitMod(item, ci, mod)
      }
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
    //    if (!parsed) console.log(item.name, item.typeLine, mod) else console.log("#########", mod)
    if (!parsed) console.log(item.getFrameType.name, mod)
  }
}