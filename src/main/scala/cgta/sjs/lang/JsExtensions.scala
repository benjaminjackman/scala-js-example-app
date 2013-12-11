package cgta.sjs
package lang

import scala.scalajs.js
import scala.concurrent.Future
import scala.util.{Failure, Success}


//////////////////////////////////////////////////////////////
// Copyright (c) 2013 Ben Jackman, Jeff Gomberg
// All Rights Reserved
// please contact ben@jackman.biz or jeff@cgtanalytics.com
// for licensing inquiries
// Created by bjackman @ 12/11/13 12:18 AM
//////////////////////////////////////////////////////////////


object JsExtensions {
  implicit class TypeAExtensions[A](val a: A) extends AnyVal {
    def nullSafe: Option[A] = if (a == null || a.isInstanceOf[js.Undefined]) None else Some(a)
  }

  implicit class JsAnyExtensions(val a: js.Any) extends AnyVal {
    def toJsonString: js.String = js.JSON.stringify(a)
  }

  implicit class JsFutureExtensions[A](val f: Future[A]) extends AnyVal {
    def log(prefix: String = null) = {
      f.onComplete {
        case Success(x) =>
          if (prefix != null) console.log(prefix, x) else console.log(x)
        case Failure(t) =>
          if (prefix != null) console.error(prefix, t) else console.error(t)
      }
    }
  }
}

trait JsExtensions {
  import JsExtensions._

  implicit def typeAExtensions[A](a: A): TypeAExtensions[A] = new TypeAExtensions[A](a)
  implicit def jsAnyExtensions(a: js.Any): JsAnyExtensions = new JsAnyExtensions(a)
  implicit def jsFutureExtensions[A](a: Future[A]): JsFutureExtensions[A] = new JsFutureExtensions[A](a)

}