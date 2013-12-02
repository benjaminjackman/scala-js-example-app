package example

import scala.scalajs.js
import js.Dynamic.{ global => g }

case class Point(val x: Int, val y: Int) {

}

object JsPoint {
  implicit class PointOps(val p : JsPoint) extends AnyVal {
    def tot = p.x+p.y
  }
}

trait JsPoint extends js.Object {
  def x() : Int = ???
  def y() : Int = ???
}

object ScalaJSExample {
  def main(): Unit = {
    val p = new Point(3,4).asInstanceOf[JsPoint]
    val paragraph = g.document.createElement("p")
    paragraph.innerHTML = s"<strong>${Map(1->"Hi",2->"2",3->"Three")}It works!</strong>"
    g.document.getElementById("playground").appendChild(paragraph)
    g.console.log(p.tot)

  }
}
