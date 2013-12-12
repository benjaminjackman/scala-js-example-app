package looty

import scala.scalajs.js
import cgta.cjs.lang.Optional


//////////////////////////////////////////////////////////////
// Copyright (c) 2013 Ben Jackman, Jeff Gomberg
// All Rights Reserved
// please contact ben@jackman.biz or jeff@cgtanalytics.com
// for licensing inquiries
// Created by bjackman @ 12/9/13 11:22 PM
//////////////////////////////////////////////////////////////


object LootyMain {

  class JsSettings extends js.Object {
        var x: Optional[js.Number] = ???

      }



  def main(args: Array[String]) {
    console.log("Hello World! Looty Main!")

      val y = js.Object().asInstanceOf[JsSettings]
      y.x = 6 : js.Number //extra typing needed here to double implicit conversion
      console.log(y.x.get) //prints 6


//    PoeRpcs.getCharacters().log("CI")
//    PoeRpcs.getStashTab(league = Leagues.Standard.toString, tabIdx = 0).onComplete(x => console.log("ST", x))
//    PoeRpcs.getStashTabInfo(league = Leagues.Standard.toString).onComplete(x => console.log("SI", x))
//    PoeRpcs.getCharacterInventory(character = "BAM__SLAM").onComplete(x => console.log("CI", x))
  }

}