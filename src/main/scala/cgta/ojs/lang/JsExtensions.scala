package cgta.ojs
package lang

import scala.scalajs.js
import scala.concurrent.Future
import scala.util.{Failure, Success}
import scala.collection.mutable.ArrayBuffer


//////////////////////////////////////////////////////////////
// Copyright (c) 2013 Ben Jackman, Jeff Gomberg
// All Rights Reserved
// please contact ben@jackman.biz or jeff@cgtanalytics.com
// for licensing inquiries
// Created by bjackman @ 12/11/13 12:18 AM
//////////////////////////////////////////////////////////////


class TypeAExtensions[A](val a: A) extends AnyVal {
  def nullSafe: Option[A] = if (a == null || a.isInstanceOf[js.Undefined]) None else Some(a)
  def =?=[B](b: B)(implicit e: B =:= A) = a == b
  def =!=[B](b: B)(implicit e: B =:= A) = a != b
}

class JsAnyExtensions(val a: js.Any) extends AnyVal {
  def toJsonString: js.String = js.JSON.stringify(a)
  def toJsDic: js.Dictionary = a.asInstanceOf[js.Dictionary]
  def toJsObj: js.Object = a.asInstanceOf[js.Object]
  def toJsDyn: js.Dynamic = a.asInstanceOf[js.Dynamic]
  def toJsStr: js.String = a.asInstanceOf[js.String]
}

class JsFutureExtensions[A](val f: Future[A]) extends AnyVal {
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

class JsStringExtensions(val s: String) extends AnyVal {
  def cap = {
    if (s.isEmpty) {
      s
    } else {
      s.substring(0, 1).toUpperCase + s.substring(1)
    }
  }
}

class SeqExtensions[A](val xs: Iterable[A]) extends AnyVal {

  def minOpt[B >: A](implicit cmp: Ordering[B]): Option[A] = if (xs.isEmpty) None else Some(xs.min[B])
  def maxOpt[B >: A](implicit cmp: Ordering[B]): Option[A] = if (xs.isEmpty) None else Some(xs.max[B])

  def minByOpt[B](f: A => B)(implicit cmp: Ordering[B]): Option[A] = if (xs.isEmpty) None else Some(xs.minBy[B](f))
  def maxByOpt[B](f: A => B)(implicit cmp: Ordering[B]): Option[A] = if (xs.isEmpty) None else Some(xs.maxBy[B](f))


  /**
   * Removes duplicates from a sequence using a f to provide keys used for determining equality of elements
   * When a duplicate is removed a reducer function r is used to determine what to keep.
   *
   * Warning this class does NOT preserve ordering of the elements!
   *
   * Example (Not That Order was not preserved)
   *
   * xs = List(1->2, 1->3, 1->2, 2->1, 3->4, 4->1, 4-> -1, 5->1)
   * ys = RicherSeq(xs).removeDuplicatesBy(_._1)(List(_,_).maxBy(_._2))
   * ys == Vector((5,1), (1,3), (2,1), (3,4), (4,1))
   *
   * @param f Function that turns an element into some other type B used for equality checks
   * @param r Tie breaker function that chooses which to keep
   * @tparam B
   * @return
   */
  def removeDuplicatesBy[B](f: A => B)(r: (A, A) => A): Seq[A] = {
    xs.toVector.groupBy(f).values.map(_.reduce(r)).toVector
  }

  /**
   * Creates a new Sequence with start before the first element, sep between them, and end after the last element
   *
   * @param start Item to place before the first element
   * @param sep
   * @param end
   * @tparam B
   * @return
   */
  def intersperse[B >: A](start: => Option[B] = None, sep: => Option[B] = None, end: => Option[B] = None): Vector[B] = {
    val buf = new ArrayBuffer[B](xs.size * 2)
    if (start.isDefined) buf += start.get
    val itr = xs.toIterator
    var hasNext = itr.hasNext
    while (hasNext) {
      buf += itr.next
      hasNext = itr.hasNext
      if (hasNext && sep.isDefined) buf += sep.get
    }
    if (end.isDefined) buf += end.get
    buf.toVector
  }
}

trait JsExtensions {
  implicit def typeAExtensions[A](a: A): TypeAExtensions[A] = new TypeAExtensions[A](a)
  implicit def jsAnyExtensions(a: js.Any): JsAnyExtensions = new JsAnyExtensions(a)
  implicit def jsFutureExtensions[A](a: Future[A]): JsFutureExtensions[A] = new JsFutureExtensions[A](a)
  implicit def jsStringExtensions(a: String): JsStringExtensions = new JsStringExtensions(a)
  implicit def seqExtensions[A](a: Iterable[A]): SeqExtensions[A] = new SeqExtensions[A](a)
}