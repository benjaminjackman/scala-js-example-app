package looty

import looty.poeapi.PoeRpcs
import looty.poeapi.PoeTypes.Leagues


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

//    val s = newObject
//    s.x = 5


    //js.eval("typeof(s.x)")

//    Storage.local.set(s)
//    decant[js.Any](Storage.local.get(null, _)).log("Storage has")
    
    //PoeSyncer.storeCharacterData()
//    val pc = new PoeCacher()
//    pc.getChars().log()
//
//    PoeRpcs.getStashTabInfos(Leagues.Standard).log()

//    PoeRpcs.get(url = "http://www.pathofexile.com/character-window/get-items", params = s).log()
    (0 to 100).foreach { i =>
      PoeRpcs.getStashTab(Leagues.Standard, i).log()
    }


//    PoeRpcs.getCharacters().log("CI")
//    PoeRpcs.getStashTab(league = Leagues.Standard.toString, tabIndex = 0).onComplete(x => console.log("ST", x))
//    PoeRpcs.getStashTabInfo(league = Leagues.Standard.toString).onComplete(x => console.log("SI", x))
//    PoeRpcs.getCharacterInventory(character = "BAM__SLAM").onComplete(x => console.log("CI", x))
  }

}