package cgta.ojs.lang

import scala.scalajs.js



//////////////////////////////////////////////////////////////
// Copyright (c) 2013 Ben Jackman, Jeff Gomberg
// All Rights Reserved
// please contact ben@jackman.biz or jeff@cgtanalytics.com
// for licensing inquiries
// Created by bjackman @ 12/10/13 9:33 PM
//////////////////////////////////////////////////////////////

object Optional {
  implicit class OptionalExtensions[A](val a: Optional[A]) extends AnyVal {
    def isNull = a == null
    def isUndefined = a.isInstanceOf[js.Undefined]
    def isEmpty = isNull || isUndefined
    def isNonEmpty = !isEmpty
    def get: A = if (isNonEmpty) a.asInstanceOf[A] else sys.error(s"get called on nullable when A is not defined")
    def toOption: Option[A] = if (isNonEmpty) Some(get) else None
  }
  implicit def AToOptional[A](a : A) = a.asInstanceOf[Optional[A]]
}

trait Optional[+A] extends js.Object
