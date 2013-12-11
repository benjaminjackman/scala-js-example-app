package looty

import looty.poeapi.PoeRpcs
import looty.poeapi.PoeTypes.Leagues
import cgta.sjs.chrome.Storage
import scala.scalajs.js


//////////////////////////////////////////////////////////////
// Copyright (c) 2013 Ben Jackman, Jeff Gomberg
// All Rights Reserved
// please contact ben@jackman.biz or jeff@cgtanalytics.com
// for licensing inquiries
// Created by bjackman @ 12/9/13 11:22 PM
//////////////////////////////////////////////////////////////


object LootyMain {

  def main(args: Array[String]) {
    console.log("Hello World! Looty Main!")

    Storage.local.clear()
    val s = newObject
    s.x = 5
    Storage.local.set(s)
    Storage.local.get(s, (x : js.Any) => console.log(x))

    PoeRpcs.getCharacters().log("CI")
    PoeRpcs.getStashTab(league = Leagues.Standard.toString, tabIndex = 0).onComplete(x => console.log("ST", x))
    PoeRpcs.getStashTabInfo(league = Leagues.Standard.toString).onComplete(x => console.log("SI", x))
    PoeRpcs.getCharacterInventory(character = "BAM__SLAM").onComplete(x => console.log("CI", x))
  }

}