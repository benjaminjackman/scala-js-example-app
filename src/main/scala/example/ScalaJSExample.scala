package example

import scala.scalajs.js
import js.Dynamic.{global => g}
import org.scalajs.jquery.{JQueryAjaxSettings, JQueryStatic}
import example.SerAjax.HttpRequestTypes

//Simple library for performing ajax calls in a semi-efficent way
object SerAjax {
  val jQuery: JQueryStatic = g.jQuery.asInstanceOf[JQueryStatic]

  object JsPromise {
//    implicit class JsPromiseExtensions[A](val promise : JsPromise[A]) extends AnyVal {
//      def map[B](f : A => B) = {
//        promise.`then`(f)
//      }
//    }
  }

  trait JsPromise[A] extends js.Object {
    def `then`(onFulfilled: js.Any) = ???
  }

  object HttpRequestTypes extends Enumeration {
    type HttpRequestType = Value
    val Get = Value("Get")
    val Post = Value("Post")
    val Put = Value("Put")
    val Delete = Value("Delete")
  }

  def apply(url: String, requestType: HttpRequestTypes.HttpRequestType, data: String) = {
    jQuery.ajax(js.Dictionary(
      "url" -> url,
      "type" -> requestType.toString,
      "data" -> data
    ).asInstanceOf[JQueryAjaxSettings]).asInstanceOf[JsPromise[js.Dynamic]]
  }


}

object ScalaJSExample {
  val jQuery: JQueryStatic = g.jQuery.asInstanceOf[JQueryStatic]

  def main(): Unit = {
    jQuery("#content").html("Hello World!")
    val url = "http://phi.portal.cgtanalytics.com/rpc/"
    val data = """{"id":"6","calls":[{"name":"spyder.positions","args":{"tday":20131203}}]}"""

//    g.eval("debugger")

    SerAjax(url, HttpRequestTypes.Post, data).then((x: js.Any) => g.console.log(x))

  }
}
