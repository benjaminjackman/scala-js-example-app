package cgta

import cgta.ojs.lang.{JsObjectBuilder, OJsDsl}
import scala.scalajs.js

//////////////////////////////////////////////////////////////
// Copyright (c) 2013 Ben Jackman, Jeff Gomberg
// All Rights Reserved
// please contact ben@jackman.biz or jeff@cgtanalytics.com
// for licensing inquiries
// Created by bjackman @ 12/9/13 11:12 PM
//////////////////////////////////////////////////////////////


package object ojs extends OJsDsl {
  val obj = new JsObjectBuilder
  def arr(xs: js.Any*) = js.Array(xs: _*)
}
