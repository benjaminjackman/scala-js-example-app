package example

import scala.scalajs.js
import js.Dynamic.{global => g}
import scala.scalajs.js.annotation.JSName

case class Point(val x: Int, val y: Int) {

}

object JsPoint {

  implicit class PointOps(val p: JsPoint) extends AnyVal {
    def tot = p.x + p.y
  }

}

trait JsPoint extends js.Object {
  def x(): Int = ???

  def y(): Int = ???
}

@JSName("d3")
trait D3 extends js.Object {
  def max[A](xs: js.Array[A]): A

  def max[A, B](xs: js.Array[A], f: A => B): B
}

object ScalaJSExample {
  def main(): Unit = {
    //    val p = Point(3,4).asInstanceOf[JsPoint]

    val paragraph = js.Globals.document.createElement("p")

    paragraph.innerHTML = s"<strong>${Map(1 -> "!Hi", 2 -> "2", 3 -> "Three")}It works!</strong>"
    g.document.getElementById("playground").appendChild(paragraph)
    //    g.console.log(p.tot)
    val d3 = g.d3.asInstanceOf[D3]
    g.console.log(d3.max(js.Array(1, 2, 3)))

  }
}
