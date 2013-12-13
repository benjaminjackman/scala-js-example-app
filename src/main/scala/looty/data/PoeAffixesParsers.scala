package looty
package data

import scala.scalajs.js
import looty.poeapi.PoeTypes.AnyItem


//////////////////////////////////////////////////////////////
// Copyright (c) 2013 Ben Jackman, Jeff Gomberg
// All Rights Reserved
// please contact ben@jackman.biz or jeff@cgtanalytics.com
// for licensing inquiries
// Created by bjackman @ 12/13/13 2:25 AM
//////////////////////////////////////////////////////////////


case class MinMaxDamage[A](var min: Double, var max: Double) {
}

class ComputedItem(val baseItem: AnyItem) extends {
  var increasedPhysicalDamage = 0.0
  def physicalDamages = MinMaxDamage(0, 0)
  def physicalDps = attacksPerSecond * increasedPhysicalDamage * (physicalDamages.min + physicalDamages.max) / 2.0

  var dexterity    = 0
  var strength     = 0
  var intelligence = 0

  def attacksPerSecond: Double = ???

}

case class Expectation(mod: String, expects: (ComputedItem => Any, Any)*)


object PoeAffixesParsers {
  private var _all = new js.Array[AffixParser]()

  trait AffixParser {
    def expectation: Expectation
    def parse(s: js.String, i: ComputedItem): Boolean
  }

  def register(affix: AffixParser) {
    _all.push(affix)
  }


  abstract class Increased(style: String) extends AffixParser {
    lazy val regex = new js.RegExp(s"^([.+-\\d]+)%* increased $style")
    def parse(s: js.String, i: ComputedItem): Boolean = {
      s.`match`(regex).nullSafe.getOrElse(js.Array()).toList match {
        case null => false
        case x :: y :: zs =>
          process(x.toString.toDouble, i)
          true
        case xs => false
      }
    }
    def process(x: Double, i: ComputedItem)
  }

  abstract class BonusTo(style: String) extends AffixParser {
    lazy val regex = new js.RegExp(s"^([.+-\\d]+)%* to $style")
    def parse(s: js.String, i: ComputedItem): Boolean = {
      s.`match`(regex).nullSafe.getOrElse(js.Array()).toList match {
        case null => false
        case x :: y :: zs =>
          process(x.toString.toDouble, i)
          true
        case xs => false
      }
    }
    def process(x: Double, i: ComputedItem)
  }

  //20% increased Physical Damage
  //[] => ComputedItem.increasedPhysicalDamage == 8
  object IncreasedPhysicalDamage extends Increased("Physical Damage") {
    def expectation = Expectation("8% increased Physical Damage", (_.increasedPhysicalDamage, 8))
    override def process(x: Double, i: ComputedItem) { i.increasedPhysicalDamage += x }
  }
  register(IncreasedPhysicalDamage)

  object IncreasedDexterity extends BonusTo("Dexterity") {
    def expectation = Expectation("+8 to Dexterity", (_.dexterity, 8))
    def process(x: Double, i: ComputedItem): Unit = ???
  }


  object Increased


  val all = _all.toList
}