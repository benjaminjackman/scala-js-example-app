package cgta

import scala.scalajs.js
import cgta.SjsDsl.TypeAExtensions

//////////////////////////////////////////////////////////////
// Copyright (c) 2013 Ben Jackman, Jeff Gomberg
// All Rights Reserved
// please contact ben@jackman.biz or jeff@cgtanalytics.com
// for licensing inquiries
// Created by bjackman @ 12/9/13 11:12 PM
//////////////////////////////////////////////////////////////

object SjsDsl {

  implicit class TypeAExtensions[A](val a: A) extends AnyVal {
    def nullSafe: Option[A] = if (a == null) None else Some(a)
  }

}

trait SjsDsl {
  def global = js.Dynamic.global
  def console = global.console

  object PRINT {
    def |(s: js.Any) {
      global.console.log(s)
    }
  }

  implicit def typeAExtensions[A](a: A): TypeAExtensions[A] = new TypeAExtensions[A](a)

}

package object sjs extends SjsDsl {

}
