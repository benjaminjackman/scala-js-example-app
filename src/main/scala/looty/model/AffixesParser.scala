package looty
package model

import scala.scalajs.js


//////////////////////////////////////////////////////////////
// Copyright (c) 2013 Ben Jackman, Jeff Gomberg
// All Rights Reserved
// please contact ben@jackman.biz or jeff@cgtanalytics.com
// for licensing inquiries
// Created by bjackman @ 12/13/13 2:25 AM
//////////////////////////////////////////////////////////////


object AffixesParser {
  def parse(item: ComputedItem, s: js.String): Boolean = {
    var parsed = false
    all.toList.foreach { parser =>
      if (parser.parse(s, item)) parsed = true
    }
    parsed
  }

  private val _all = new js.Array[AffixParser]()
  def add(affix: AffixParser) {
    _all.push(affix)
  }


  trait AffixParser {
    def parse(s: js.String, i: ComputedItem): Boolean
  }

  trait RegexAffixParser extends AffixParser {
    def regex: js.RegExp
  }

  trait BinaryAffixParser extends AffixParser {
    def str: js.String
    def parse(s: js.String, i: ComputedItem): Boolean = if (s =?= str) {
      process(i)
      true
    } else {
      false
    }
    def process(i: ComputedItem)
  }
  def simple0(s: js.String)(f: (ComputedItem) => Unit) {
    add {
      new BinaryAffixParser {
        def str: js.String = s
        def process(i: ComputedItem): Unit = f(i)
      }
    }
  }

  trait RegexAffixParser1 extends RegexAffixParser {
    override def parse(s: js.String, i: ComputedItem): Boolean = {
      s.`match`(regex).nullSafe.getOrElse(js.Array()).toList match {
        case null => false
        case x :: y :: zs =>
          process(i, y.toString.toDouble)
          true
        case xs => false
      }
    }
    def process(i: ComputedItem, x: Double)
  }


  def regex1(regex: js.String)(f: (ComputedItem, Double) => Unit) {
    val r = regex
    add {
      new RegexAffixParser1() {
        val regex = new js.RegExp(r)
        def process(i: ComputedItem, x: Double): Unit = f(i, x)
      }
    }
  }

  trait RegexAffixParser2 extends RegexAffixParser {
    override def parse(s: js.String, i: ComputedItem): Boolean = {
      s.`match`(regex).nullSafe.getOrElse(js.Array()).toList match {
        case null => false
        case x :: y :: z :: zs =>
          process(i, y.toString.toDouble, z.toString.toDouble)
          true
        case xs => false
      }
    }
    def process(i: ComputedItem, x: Double, y: Double)
  }

  def regex2(regex: js.String)(f: (ComputedItem, Double, Double) => Unit) {
    val r = regex
    add {
      new RegexAffixParser2() {
        val regex = new js.RegExp(r)
        def process(i: ComputedItem, x: Double, y: Double): Unit = f(i, x, y)
      }
    }
  }


  def increased(name: js.String)(f: (ComputedItem, Double) => Unit) { regex1(s"^([.+-\\d]+)%* increased $name$$")(f) }
  def reduced(name: js.String)(f: (ComputedItem, Double) => Unit) { regex1(s"^([.+-\\d]+)%* reduced $name$$")(f) }
  def plusTo(name: js.String)(f: (ComputedItem, Double) => Unit) { regex1(s"^([.+-\\d]+)%* to $name$$")(f) }
  def addsDamage(name: js.String)(f: (ComputedItem, Double, Double) => Unit) {
    regex2(s"^Adds ([\\d]+)-([\\d]+) $name Damage$$")(f)
  }
  def level(name: js.String)(f: (ComputedItem, Double) => Unit) {
    val a = if (name.isEmpty) "" else name + " "
    val r = s"^([.+-\\d]+)%* to Level of ${a}Gems in this item$$"
    regex1(r)(f)
  }
  def simple1(prefix: js.String, suffix: js.String)(f: (ComputedItem, Double) => Unit) {
    val a = if (prefix.isEmpty) "" else prefix + " "
    val b = if (suffix.isEmpty) "" else " " + suffix
    val r = s"^$a([.+-\\d]+)%*$b$$"
    regex1(r)(f)
  }


  for (x <- Attributes.all) {
    plusTo(x.cap)(_.plusTo.attribute.+=(x, _))
    level(x.cap)(_.gemLevel.attribute.+=(x, _))
  }

  for (x <- Elements.all) {
    increased(s"${x.cap} Damage")(_.increased.damage.+=(x, _))
    plusTo(s"${x.cap} Resistance")(_.plusTo.resistance.+=(x, _))
    addsDamage(x.cap) { (i, min, max) =>
      i.damages(x) +=(min, max)
    }
    level(x.cap)(_.gemLevel.element.+=(x, _))
  }

  for (x <- LifeAndMana.all) {
    regex1(s"^([+-\\d]+) ${x.cap} [gG]ained on Kill")(_.onKill.lifeAndMana(x) += _)
    regex1(s"^([+-\\d]+)% of Physical Attack Damage Leeched as ${x.cap}")(_.onKill.lifeAndMana(x) += _)
    plusTo(s"maximum ${x.cap}")(_.plusTo.lifeAndMana.+=(x, _))
    simple1("", s"${x.cap} Regenerated per second")(_.regeneratedPerSecond.+=(x, _))
    simple1("", s"${x.cap} gained for each enemy hit by your Attacks")(_.onHit.lifeAndMana.+=(x, _))
  }

  increased("Attack Speed")(_.increased.attackSpeed += _)
  increased("Stun Duration on enemies")(_.increased.stunDurationOnEnemies += _)
  increased("Chill Duration on enemies")(_.increased.chillDurationOnEnemies += _)
  increased("Global Critical Strike Multiplier")(_.increased.globalCriticalStrikeMultiplier += _)
  increased("Global Critical Strike Chance")(_.increased.globalCriticalStrikeChance += _)
  increased("Critical Strike Chance")(_.increased.criticalStrikeChance += _)
  increased("Critical Strike Chance for Spells")(_.increased.criticalStrikeChanceForSpells += _)
  increased("Quantity of Items found")(_.increased.quantityOfItemsFound += _)
  increased("Rarity of Items found")(_.increased.rarityOfItemsFound += _)
  increased("Movement Speed")(_.increased.movementSpeed += _)
  increased("Block and Stun Recovery")(_.increased.blockAndStunRecovery += _)
  increased("Spell Damage")(_.increased.spellDamage += _)
  increased("Mana Regeneration Rate")(_.increased.manaRegenerationRate += _)
  increased("Elemental Damage with Weapons")(_.increased.elementalDamageWithWeapons += _)
  increased("Light Radius")(_.increased.lightRadius += _)
  increased("Cast Speed")(_.increased.castSpeed += _)
  increased("Projectile Speed")(_.increased.projectileSpeed += _)
  increased("Accuracy Rating")(_.increased.accuracyRating += _)
  increased("Block Recovery")(_.increased.blockRecovery += _)
  increased("Elemental Damage"){ (i, n) =>
    i.increased.elementalDamage += n
    i.increased.damage.fire += n
    i.increased.damage.cold += n
    i.increased.damage.lightning += n
  }

  increased("Armour")(_.increased.armour += _)
  increased("Evasion Rating")(_.increased.evasion += _)
  increased("Energy Shield")(_.increased.energyShield += _)
  increased("maximum Energy Shield")(_.increased.maximumEnergyShield += _)
  increased("Armour and Evasion") { (i, a) => i.increased.armour += a; i.increased.evasion += a}
  increased("Armour and Energy Shield") { (i, a) => i.increased.armour += a; i.increased.energyShield += a}
  increased("Evasion and Energy Shield") { (i, a) => i.increased.evasion += a; i.increased.energyShield += a}

  plusTo("Accuracy Rating")(_.plusTo.accuracyRating += _)
  plusTo("Armour")(_.plusTo.armour += _)
  plusTo("Evasion Rating")(_.plusTo.evasionRating += _)
  plusTo("Energy Shield")(_.plusTo.energyShield += _)
  plusTo("maximum Energy Shield")(_.plusTo.energyShield += _)
  plusTo("all Elemental Resistances") { (i, n) =>
    i.plusTo.resistance.fire += n
    i.plusTo.resistance.cold += n
    i.plusTo.resistance.lightning += n
  }
  plusTo("all Attributes") { (i, n) =>
    i.plusTo.attribute.strength += n
    i.plusTo.attribute.dexterity += n
    i.plusTo.attribute.intelligence += n
  }
  plusTo("Strength and Dexterity") { (i, n) =>
    i.plusTo.attribute.strength += n
    i.plusTo.attribute.dexterity += n
  }
  plusTo("Strength and Intelligence") { (i, n) =>
    i.plusTo.attribute.strength += n
    i.plusTo.attribute.intelligence += n
  }
  plusTo("Dexterity and Intelligence") { (i, n) =>
    i.plusTo.attribute.dexterity += n
    i.plusTo.attribute.intelligence += n
  }


  reduced("Attribute Requirements")(_.reduced.attributeRequirements += _)
  reduced("Enemy Stun Threshold")(_.reduced.enemyStunThreshold += _)

  level("Melee")(_.gemLevel.melee += _)
  level("Minion")(_.gemLevel.minion += _)
  level("Bow")(_.gemLevel.bow += _)
  level("")(_.gemLevel.addToAll(_))

  simple1("Reflects", "Physical Damage to Melee Attackers")(_.reflectsPhysicalDamageToAttackers += _)
  simple1("", "additional Block Chance")(_.blockChance += _)
  simple1("", "Chance to Block")(_.blockChance += _)



  increased("Flask Charges gained")(_.flask.increased.chargesGained += _)
  increased("Flask Mana Recovery rate")(_.flask.increased.manaRecoveryRate += _)
  increased("Flask effect duration")(_.flask.increased.effectDuration += _)
  increased("Flask Life Recovery rate")(_.flask.increased.lifeRecoveryRate += _)
  increased("Flask Recovery Speed")(_.flask.increased.flaskRecoverySpeed += _)
  increased("Charge Recovery")(_.flask.increased.chargeRecovery += _)
  increased("Stun Recovery during flask effect")(_.flask.increased.stunRecovery += _)
  increased("Recovery Speed")(_.flask.increased.recoverySpeed += _)
  increased("Amount Recovered")(_.flask.increased.amountRecovered += _)
  increased("Recovery when on Low Life")(_.flask.increased.recoveryOnLowLife += _)
  increased("Life Recovered")(_.flask.increased.lifeRecovered += _)
  increased("Armour during flask effect")(_.flask.increased.armour += _)
  increased("Evasion Rating during flask effect")(_.flask.increased.evasion += _)
  increased("Movement Speed during flask effect")(_.flask.increased.evasion += _)

  simple0("Dispels Frozen and Chilled")(_.flask.dispelsFrozenAndChilled = true)
  simple0("Dispels Shocked")(_.flask.dispelsShocked = true)
  simple0("Dispels Burning")(_.flask.dispelsBurning = true)
  simple0("Removes Bleeding")(_.flask.removesBleeding = true)
  simple0("Immunity to Curses during flask effect. Removes Curses on use")(_.flask.curseImmunity = true)
  simple0("Adds Knockback to Melee Attacks during flask effect")(_.flask.knockback = true)
  simple0("Instant Recovery")(_.flask.instantRecovery = true)
  simple0("Instant Recovery when on Low Life")(_.flask.instantRecoveryLowLife = true)

  reduced("Amount Recovered")(_.flask.reduced.amountRecovered += _)
  reduced("Recovery Speed")(_.flask.reduced.recoverySpeed += _)
  reduced("Flask Charges used")(_.flask.reduced.flaskChargesUsed += _)

  simple1("", "Extra Charges")(_.flask.extraCharges += _)
  simple1("", "of Recovery applied Instantly")(_.flask.extraCharges += _)

  simple1("Recharges", "Charges when you take a Critical Strike")(_.flask.chargesOnCriticalStrikeTaken += _)
  simple1("Recharges", "Charge when you deal a Critical Strike")(_.flask.chargesOnCriticalStrikeGiven += _)
  simple1("Removes", "of Life Recovered from Mana when used")(_.flask.lifeFromMana += _)

  simple1("", "additional Elemental Resistances during flask effect")(_.flask.additionalResistances += _)
  simple1("Grants", "of Life Recovery to Minions")(_.flask.lifeRecoveryToMinions += _)

  val all = _all.toList

}

//20% increased Physical Damage
