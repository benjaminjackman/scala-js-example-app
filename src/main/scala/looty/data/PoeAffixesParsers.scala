package looty
package data

import scala.scalajs.js


//////////////////////////////////////////////////////////////
// Copyright (c) 2013 Ben Jackman, Jeff Gomberg
// All Rights Reserved
// please contact ben@jackman.biz or jeff@cgtanalytics.com
// for licensing inquiries
// Created by bjackman @ 12/13/13 2:25 AM
//////////////////////////////////////////////////////////////


object PoeAffixesParsers {
  private var _all = new js.Array[AffixParser]()

  trait AffixParser {
    def parse(s : js.String, i : ComputedItem) : Boolean
  }

  def register(affix : AffixParser) {
    _all.push(affix)
  }



  class Increased(style : String) extends AffixParser {
    lazy val regex = new js.RegExp(s"^([.+-\\d]+)%* increased $style")
    def parse(s : js.String, i : ComputedItem) : Boolean = {
      s.`match`(regex).nullSafe.getOrElse(js.Array()).toList match {
        case null => false
        case x::y::zs => true
        case xs => false
      }
    }
  }
  //20% increased Physical Damage
  object IncreasedPhysicalDamage extends Increased("Physical Damage")
  register(IncreasedPhysicalDamage)


  val all = _all.toList
}