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

object ItemParser {
  var failCnt  = 0
  var parseCnt = 0

  def parseItem(item: AnyItem): ComputedItem = {
    val ci = new ComputedItem(item)
    if (isEquippable(ci)) {
      parseExplicitMods(ci)
      parseProperties(ci)
    }
    ci
  }

  def isEquippable(ci: ComputedItem) = !ci.item.isGem && !ci.item.isCurrency && !ci.item.isMap


  def parseExplicitMods(ci: ComputedItem) {
    for {
      emods <- ci.item.explicitMods.toOption.toList
      mod: js.String <- emods
    } {
      var parsed = false
      AffixParsers.all.toList.foreach { parser =>
        if (parser.parse(mod, ci)) {
          parsed = true
        }
      }
      if (parsed) parseCnt += 1 else failCnt += 1
      //    if (!parsed) console.log(item.name, item.typeLine, mod) else console.log("#########", mod)
      if (!parsed) console.log("Unable to parse affix", ci.item.getFrameType.name, ci.item.name, "->", mod)
    }
  }


  def parseProperties(ci: ComputedItem) {
    if (!ci.item.isFlask) {
      for {
        props <- ci.item.properties.toOption.toList
        prop <- props
      } {
        var parsed = false
        PropertyParsers.all.toList.foreach { parser =>
          if (parser.parse(ci, prop)) {
            parsed = true
          }
          //        if (parsed) parseCnt += 1 else failCnt += 1
        }
        if (!parsed) console.log("Unable to parse property", ci.item.getFrameType.name, ci.item.name, "->", prop, ci.item)
      }
    }
  }
}