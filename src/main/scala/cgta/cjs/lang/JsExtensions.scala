package cgta.cjs
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
    def =?=[B](b: B)(implicit e: B =:= A) = a == b
    def =!=[B](b: B)(implicit e: B =:= A) = a != b
  }

  implicit class JsAnyExtensions(val a: js.Any) extends AnyVal {
    def toJsonString: js.String = js.JSON.stringify(a)
    def toJsDic: js.Dictionary = a.asInstanceOf[js.Dictionary]
    def toJsObj: js.Object = a.asInstanceOf[js.Object]
    def toJsDyn: js.Dynamic = a.asInstanceOf[js.Dynamic]
    def toJsStr: js.String = a.asInstanceOf[js.String]
  }

  implicit class JsFutureExtensions[A](val f: Future[A]) extends AnyVal {
    def log(prefix: String = null): Future[A] = {
      val p = JsPromise[A]()
      f.onComplete {
        case Success(x) =>
          if (prefix != null) console.log(prefix, x) else console.log(x + "", x)
          p.success(x)
        case Failure(t) =>
          if (prefix != null) console.error(prefix, t) else console.error(t + "", t)
          p.failure(t)
      }
      p.future
    }
  }

  implicit class JsStringExtensions(val s: String) extends AnyVal {
    def cap = {
      if (s.isEmpty) {
        s
      } else {
        s.substring(0, 1).toUpperCase + s.substring(1)
      }
    }
  }
}

trait JsExtensions {
  import JsExtensions._

  implicit def typeAExtensions[A](a: A): TypeAExtensions[A] = new TypeAExtensions[A](a)
  implicit def jsAnyExtensions(a: js.Any): JsAnyExtensions = new JsAnyExtensions(a)
  implicit def jsFutureExtensions[A](a: Future[A]): JsFutureExtensions[A] = new JsFutureExtensions[A](a)
  implicit def jsStringExtensions(a: String): JsStringExtensions = new JsStringExtensions(a)

}