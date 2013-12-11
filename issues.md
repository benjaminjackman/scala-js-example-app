# First read Sebastien's paper:
http://infoscience.epfl.ch/record/190834/

# Converting scala types to js.Any
example:
    case class Point(x: Int, y:      Int)
    import scala.scalajs.js
    import js.Dynamic.{ global => g }
        g.console.log(p)
    [error]  found   : example.Point
    [error]  required: scala.scalajs.js.Any
    [error]     g.console.                log(p)
    [error]                   ^

use .asInstanceOf[js.Any]



# How to check for undefined?
use isInstanceOf[js.Undefined]



# String interpolation breaks with undefined/null values

    val x = js.Dictionary().asInstanceOf[js.Dynamic]
    s"${x.y}" //throws:Uncaught TypeError: Cannot call method 'toString' of undefined

    val x = js.Dictionary("y" -> null).asInstanceOf[js.Dynamic]
    s"${x.y}" //throws:Uncaught TypeError: Cannot call method 'toString' of null

Versus Scala Behaviour

    scala> object Y {val x = null}
    defined module Y

    scala> s"${Y.x}"
    res1: String = null

# Misc
Install gen-idea plugin

