package cgta.sjs.lang

import scala.scalajs.js



//////////////////////////////////////////////////////////////
// Copyright (c) 2013 Ben Jackman, Jeff Gomberg
// All Rights Reserved
// please contact ben@jackman.biz or jeff@cgtanalytics.com
// for licensing inquiries
// Created by bjackman @ 12/10/13 9:33 PM
//////////////////////////////////////////////////////////////

object Nullable {
  implicit class NullableExtensions[A](val a: Nullable[A]) extends AnyVal {
    def isNull = a == null || a.isInstanceOf[js.Undefined]
    def isDefined = !isNull
    def get: A = if (isDefined) a.asInstanceOf[A] else sys.error(s"get called on nullable when A is not defined")
    def map[B](f: A => B): Nullable[B] =
      if (isNull) f(a.get).asInstanceOf[Nullable[B]]
      else null.asInstanceOf[Nullable[B]]
    def toOption: Option[A] = if (isDefined) Some(get) else None
  }
}

trait Nullable[+A] extends js.Object
