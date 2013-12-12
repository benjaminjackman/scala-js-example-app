package cgta.sjs
package lang

import scala.scalajs.js


//////////////////////////////////////////////////////////////
// Copyright (c) 2013 Ben Jackman, Jeff Gomberg
// All Rights Reserved
// please contact ben@jackman.biz or jeff@cgtanalytics.com
// for licensing inquiries
// Created by bjackman @ 12/11/13 12:17 AM
//////////////////////////////////////////////////////////////


trait JSConsole extends js.Object {
  def log(xs : Any*)
  def error(xs : Any*)
}
