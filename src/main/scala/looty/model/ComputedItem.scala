package looty
package model

import looty.poeapi.PoeTypes.AnyItem
import looty.model.WeaponTypes.WeaponType


//////////////////////////////////////////////////////////////
// Copyright (c) 2013 Ben Jackman, Jeff Gomberg
// All Rights Reserved
// please contact ben@jackman.biz or jeff@cgtanalytics.com
// for licensing inquiries
// Created by bjackman @ 12/14/13 1:06 PM
//////////////////////////////////////////////////////////////


case class MinMaxDamage(var min: Double, var max: Double) {
  def avg = min + max / 2.0
  def +=(min: Double, max: Double) {
    this.min += min
    this.max += max
  }
  def set(that: MinMaxDamage) {
    this.min = that.min
    this.max = that.max
  }
}


class ComputedItem(val item: AnyItem) extends {

  lazy val maxLinks: Int = item.sockets.toList.map(_.group).groupBy(x => x).map(_._2.size).maxOpt.getOrElse(0)
  def maxResist = plusTo.resistance.all.max
  lazy val score: ItemScore = ItemScorer(this).getOrElse(ItemScore(Nil, 0))

  def isEquippable = !item.isGem && !item.isCurrency && !item.isMap && !item.isQuest

  var location = ""

  object increased {
    val damage                         = Elements mutable 0.0
    var stunDurationOnEnemies          = 0.0
    var chillDurationOnEnemies         = 0.0
    var attackSpeed                    = 0.0
    var globalCriticalStrikeMultiplier = 0.0
    var globalCriticalStrikeChance     = 0.0
    var criticalStrikeChance           = 0.0
    var criticalStrikeChanceForSpells  = 0.0
    var armour                         = 0.0
    var evasion                        = 0.0
    var energyShield                   = 0.0
    var maximumEnergyShield            = 0.0
    var quantityOfItemsFound           = 0.0
    var rarityOfItemsFound             = 0.0
    var movementSpeed                  = 0.0
    var blockAndStunRecovery           = 0.0
    var spellDamage                    = 0.0
    var manaRegenerationRate           = 0.0
    var elementalDamageWithWeapons     = 0.0
    var lightRadius                    = 0.0
    var castSpeed                      = 0.0
    var projectileSpeed                = 0.0
    var accuracyRating                 = 0.0
    var blockRecovery                  = 0.0
    var elementalDamage                = 0.0
  }

  object reduced {
    var attributeRequirements = 0.0
    var enemyStunThreshold    = 0.0
  }

  val damages = Elements of MinMaxDamage(0, 0)

  object plusTo {
    val attribute      = Attributes mutable 0.0
    val resistance     = Elements mutable 0.0
    val lifeAndMana    = LifeAndMana mutable 0.0
    var accuracyRating = 0.0
    var evasionRating  = 0.0
    var armour         = 0.0
    var energyShield   = 0.0
  }

  object leech {var physical = LifeAndMana mutable 0.0}
  object onKill {var lifeAndMana = LifeAndMana mutable 0.0}
  object onHit {var lifeAndMana = LifeAndMana mutable 0.0}
  object gemLevel {
    val element   = Elements mutable 0.0
    val attribute = Attributes mutable 0.0
    var melee     = 0.0
    var minion    = 0.0
    var bow       = 0.0
    var any       = 0.0
    def total = {
      element.all.sum + attribute.all.sum + melee + minion + bow + any
    }
  }

  object total {
    def dps: Double = perElementDps.all.sum
    val perElementDps = Elements calculatedWith { element =>
      properties.damages(element).avg * properties.attacksPerSecond
    }
  }

  object slots {
    def is1H: Boolean = properties.weaponType.is1H
    def is2H: Boolean = properties.weaponType.is2H
    def isWeapon: Boolean = properties.weaponType.isWeapon
    def isFlask = item.isFlask
    //TODO Fix this!
    var isSpiritShield = false
    var isShield       = false

    var isHelmet = false
    var isChest  = false
    var isGloves = false
    var isAmulet = false
    var isRing   = false
    var isBelt   = false
    var isBoots  = false
    var isQuiver = false
  }


  object properties {
    var weaponType: WeaponType = WeaponTypes.NoWeaponType
    var armour                 = 0.0
    var energyShield           = 0.0
    var evasionRating          = 0.0
    val damages                = Elements of MinMaxDamage(0, 0)
    var quality                = 0.0
    var criticalStrikeChance   = 0.0
    var attacksPerSecond       = 0.0
    var chanceToBlock          = 0.0
  }

  var reflectsPhysicalDamageToAttackers = 0.0
  var blockChance                       = 0.0

  val regeneratedPerSecond = LifeAndMana mutable 0.0

  object flask {

    object increased {
      var lifeRecoveryRate   = 0.0
      var effectDuration     = 0.0
      var manaRecoveryRate   = 0.0
      var flaskRecoverySpeed = 0.0
      var chargesGained      = 0.0
      var chargeRecovery     = 0.0
      var stunRecovery       = 0.0
      var recoverySpeed      = 0.0
      var amountRecovered    = 0.0
      var recoveryOnLowLife  = 0.0
      var lifeRecovered      = 0.0
      var armour             = 0.0
      var evasion            = 0.0
      var movementSpeed      = 0.0
    }

    object reduced {
      var amountRecovered  = 0.0
      var recoverySpeed    = 0.0
      var flaskChargesUsed = 0.0
    }

    var extraCharges                 = 0.0
    var amountAppliedInstantly       = 0.0
    var chargesOnCriticalStrikeTaken = 0.0
    var chargesOnCriticalStrikeGiven = 0.0
    var lifeFromMana                 = 0.0

    var additionalResistances = 0.0
    var lifeRecoveryToMinions = 0.0

    var dispelsFrozenAndChilled = false
    var dispelsShocked          = false
    var dispelsBurning          = false
    var removesBleeding         = false
    var curseImmunity           = false
    var knockback               = false
    var instantRecovery         = false
    var instantRecoveryLowLife  = false
  }
}
