package looty
package model

import looty.poeapi.PoeTypes.AnyItem
import scala.scalajs.js
import looty.poeapi.PoeTypes.AnyItem.FrameTypes


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
    if (ci.isEquippable) {
      parseMods(ci, ci.item.explicitMods.toOption)
      parseMods(ci, ci.item.implicitMods.toOption)
      parseProperties(ci)
      parseRequirements(ci)
      parseTypeLine(ci)
    }
    ci
  }


  def parseMods(ci: ComputedItem, mods: Option[js.Array[js.String]]) {
    for {
      emods <- mods
      mod: js.String <- emods
    } {
      if (!AffixParsers.parse(ci, mod) && ci.item.getFrameType =!= FrameTypes.unique) {
        console.log("Unable to parse affix", ci.item.getFrameType.name, ci.item.name, "->", mod)
      }
    }
  }


  def parseProperties(ci: ComputedItem) {
    if (!ci.item.isFlask) {
      for {
        props <- ci.item.properties.toOption.toList
        prop <- props
      } {
        if (!PropertyParsers.parse(ci, prop)) {
          console.log("Unable to parse property", ci.item.getFrameType.name, ci.item.name, "->", prop, ci.item)
        }
      }
    }
  }

  def parseRequirements(ci: ComputedItem) {

  }

  def parseTypeLine(ci: ComputedItem) {
    if (!ArmourParser.parse(ci, ci.item.typeLine) && ci.isEquippable && !ci.slots.isWeapon && !ci.slots.isFlask) {
      console.log("Unable to parse typeline", ci.item.getFrameType.name, ci.item.typeLine, ci.item.name, ci.item)
    }
  }
}