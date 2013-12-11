package looty.poeapi

import scala.scalajs.js
import cgta.sjs.lang.Nullable


//////////////////////////////////////////////////////////////
// Copyright (c) 2013 Ben Jackman, Jeff Gomberg
// All Rights Reserved
// please contact ben@jackman.biz or jeff@cgtanalytics.com
// for licensing inquiries
// Created by bjackman @ 12/10/13 9:16 PM
//////////////////////////////////////////////////////////////


object PoeTypes {

  object Leagues extends Enumeration {
    type League = Value
    val Standard   = Value("Standard")
    val Domination = Value("Domination")
    val Hardcore   = Value("Hardcore")
    val Nemesis    = Value("Nemesis")
  }

  trait CharacterInfo extends js.Object {
    val `class`: js.String
    val classId: js.Number
    val league : js.String
    val level  : js.Number
    val name   : js.String
  }

  trait Inventory extends js.Object {
    val error    : Nullable[js.String]
    val character: js.String
    val items    : js.Array[AnyItem]
  }

  trait StashTab extends js.Object {
    val numTabs: js.Number
    val items  : js.Array[AnyItem]

    //Returned optionally when tabs = 1 is set
    val tabs   : Nullable[js.Array[StashTabInfo]]
    val error  : Nullable[js.String]
  }

  trait StashTabInfo extends js.Object {
    val colour: Colour
    val i     : js.Number
    val n     : js.String
    val src   : js.String
  }

  trait Colour extends js.Object {
    val r: js.Number
    val g: js.Number
    val b: js.Number
  }


  trait AnyItem extends js.Object {
    val verified     : js.Boolean
    //width and height a big two handed is 2w by 3h a currency item 1w1h a dagger 1w3h
    val w            : js.Number
    val h            : js.Number
    //a Url
    val icon         : js.String
    val support      : js.Boolean
    val league       : js.String
    val sockets      : js.Array[Socket]
    val name         : js.String
    val typeLine     : js.String
    val identified   : js.Boolean
    val properties   : js.Array[ItemProperty]
    val requirements : js.Array[ItemRequirement]
    val descrText    : Nullable[js.String]
    val secDescrText : Nullable[js.String]
    val explicitMods : Nullable[js.Array[js.String]]
    val frameType    : js.Number
    val socketedItems: js.Array[AnyItem]

    //For items that are not socketed in other items
    val x          : js.Number
    //The top left corner, when in an item slot, this is 0,0 from what i can tell
    val y          : js.Number
    val inventoryId: js.String

    //For items that are socketed in other items
    val socket: Nullable[js.Number]
    val colour: Nullable[js.String]

  }

  trait Socket extends js.Object {
    val group: js.Number
    //used for socket groups, aka a 6 linked would have all sockets in group 0
    val attr : js.String //Seems to be DSI not sure what white is as I don't have a Tabula Rasa ... yet...
  }

  trait ItemProperty extends js.Object {
    val name       : js.String
    val values     : js.Array[js.Array[Number]]
    val displayMode: js.Number
  }

  trait ItemRequirement extends js.Object {
    val name       : Nullable[js.String]
    val values     : js.Array[js.Array[js.Array[js.String]]]
    val displayMode: js.Number
  }

}