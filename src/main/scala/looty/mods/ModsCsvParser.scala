package looty
package mods

import scala.concurrent.Future
import cgta.ojs.io.AjaxHelp
import scala.scalajs.js
import looty.model.Elements


//////////////////////////////////////////////////////////////
// Copyright (c) 2013 Ben Jackman, Jeff Gomberg
// All Rights Reserved
// please contact ben@jackman.biz or jeff@cgtanalytics.com
// for licensing inquiries
// Created by bjackman @ 12/26/13 3:38 PM
//////////////////////////////////////////////////////////////

/**
 * Loads the Mods csv
 */
object ModsCsvParser {

  object ModRow {
    implicit class ModRowExtensions(val r: ModRow) extends AnyVal {
    }
  }

  trait ModRow extends js.Object {
    val `1h Maces`          : js.String
    val `1h Swords and Axes`: js.String
    val `2h Maces`          : js.String
    val `2h Swords and Axes`: js.String
    val Amulets             : js.String
    val Belts               : js.String
    val Boots               : js.String
    val Bows                : js.String
    val Categories          : js.String
    val Chests              : js.String
    val Claws               : js.String
    val Daggers             : js.String
    val Description         : js.String
    val Gloves              : js.String
    val Helmets             : js.String
    val Level               : js.String
    val `Max V`             : js.String
    val `Min V`             : js.String
    val Name                : js.String
    val `Prefix/Suffix`     : js.String
    val Quivers             : js.String
    val Rings               : js.String
    val Sceptres            : js.String
    val Shields             : js.String
    val Staffs              : js.String
    val Value               : js.String
    val Wands               : js.String
  }

  var modDescriptions: List[ModRow] = null

  def init(): Future[Unit] = {
    AjaxHelp.get[String]("/data/item-mods.csv").map { csv =>
      modDescriptions = global.d3.csv.parse(csv).asInstanceOf[js.Array[ModRow]].toList

      loadParsers()
    }
  }

  private def addMod(category: String,
    description: String,
    parser: AffixesParser2.AffixParser,
    isHybrid: Boolean = false) {
    val mods = modDescriptions.filter(r => r.Categories.toString == category && r.Description.toString == description)
  }

  private def loadParsers() {
    val P = AffixesParser2

    //Mods for all elements
    for (element <- Elements.all) {
      val Element = element.cap
      addMod("Dmg",
        s"Base Min Added $Element Dmg / Base Max Added $Element Dmg",
        P.addsDamage(Element)(_.baseDamages(Element))
      )

      for (h <- 1 to 2) {
        val numHands = h.toString
        val cat = if (Element == "Physical") {
          s"${numHands}h Dmg"
        } else {
          if (h == 1) {
            "Bow and 1H elemental Dmg"
          } else {
            "2H Weapon elemental Dmg"
          }
        }
        addMod(cat,
          s"Local Min Added $Element Dmg / Local Max Added $Element Dmg",
          P.addsDamage(Element)(_.localDamages(Element))
        )
      }

    }

    //Mods for life and mana
    for (lifeMana <- List("Life", "Mana")) {
      addMod("Flask Effects",
        s"Flask $lifeMana Recovery Rate +%",
        P.increased(s"Flask $lifeMana Recovery rate")(_.increased.flaskRecoveryRate(lifeMana)))
    }

    //Gems
    for (gem <- List("Bow", "Minion", "Melee", "Cold", "Fire", "Lightning", "")) {
      val gemStr = if (gem.isEmpty) "" else gem + " "
      addMod("Gem level",
        s"Local Socketed ${gemStr}Gem Level",
        P.level(gem)(_.gem(gem)))
    }


    //Reflect
    addMod("Dmg return",
      "Physical Dmg To Return To Melee Attacker",
      P.simple1("Reflects", "Physical Damage to Melee Attackers")(_.reflects))
    //Physical Damage Scaling
    addMod("Dmg scaling", "Local Physical Dmg +%", P.increased("Physical Damage")(_.increased.localPhysicalDamage))

    //AR ES EV
    //========
    //Belt Armour
    addMod("Armor", "Armor Rating", P.plusTo("Armour")(_.plusTo.globalArmour))
    //HCGBS Armour
    addMod("Armor", "Local Armor Rating", P.plusTo("Armour")(_.plusTo.localArmour))
    //HCGBS Armour +%
    addMod("Armor", "Local Armor +%", P.increased("Armour")(_.increased.localArmour))
    //Amulet Armour +%
    addMod("Armor", "Armor Rating +%", P.increased("Armour")(_.increased.globalArmour))
    //Ring Amulet Belt
    addMod("Energy shield", "Base Max Energy Shield", P.plusTo("maximum Energy Shield")(_.plusTo.globalEnergyShield))
    //int gear
    addMod("Energy shield", "Local Energy Shield", P.plusTo("Energy Shield")(_.plusTo.localEnergyShield))
    //int-only gear
    addMod("Energy shield", "Local Energy Shield +%", P.increased("Energy Shield")(_.increased.localEnergyShield))
    //Amulet
    addMod("Energy shield",
      "Max Energy Shield +%",
      P.increased("maximum Energy Shield")(_.increased.globalEnergyShield))
    //Evasion rings
    addMod("Evasion", "Base Evasion Rating", P.plusTo("Evasion Rating")(_.plusTo.globalEvasion))
    //amulets
    addMod("Evasion", "Evasion Rating +%", P.increased("Evasion Rating")(_.plusTo.localEvasion))
    //dex gear
    addMod("Evasion", "Local Evasion Rating", P.plusTo("Evasion Rating")(_.increased.localEvasion))
    //dex-only gear
    addMod("Evasion", "Local Evasion Rating +%", P.increased("Evasion Rating")(_.increased.globalEvasion))





  }


}