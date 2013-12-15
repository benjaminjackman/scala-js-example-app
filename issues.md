# Learning #

##  First read Sebastien's paper:
http://infoscience.epfl.ch/record/190834/

##  Converting scala types to js.Any
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



##  How to check for undefined?
use isInstanceOf[js.Undefined]


##  String interpolation breaks with undefined/null values

    val x = js.Dictionary().asInstanceOf[js.Dynamic]
    s"${x.y}" //throws:Uncaught TypeError: Cannot call method 'toString' of undefined

    val x = js.Dictionary("y" -> null).asInstanceOf[js.Dynamic]
    s"${x.y}" //throws:Uncaught TypeError: Cannot call method 'toString' of null

Versus Scala Behaviour

    scala> object Y {val x = null}
    defined module Y

    scala> s"${Y.x}"
    res1: String = null


##  How does one make a simple javascript object?
dictionaries seem to have issues when the types are mixed
for example
    val d = js.Dictionary (
      "x" -> "AString",
      "y" -> 4
    )
errors out with
    [error] No implicit view available from Any => scala.scalajs.js.Any.
    [error]     val d = js.Dictionary (
    [error]                           ^
    [error] one error found

Here is what I went with:
    val d = js.Object().asInstanceOf[js.Dynamic]
    d.x = "AString"
    d.y = 4

I will write a dsl to make this better i think


# Additions #

##  Promises and future
I have gotten an implemenation of the scala Future/Promises they convert over from APlus futures
this makes callback styled programming a lot better.

I also added implicits to them to assist with js-centric development they located at
/src/main/scala/cgta/sjs/lang/JsExtensions.scala

there is a .log() that will log the result of a future, which I have found to be very handy
for exploring apis.


##  Continuations
I would like to look into continuations as well as a well to make the code look like normal blocking
code

##  Decant a helper for converting callbacks to futures
I made a simple function that can convert basic structures to futures very easily
it's in /src/main/scala/cgta/sjs/lang/CjsDsl.scala
    //Converts a callback style into a future
    //el.on("click", (x) => console.log(x))
    //decant(el.on("click", _)).onSuccess(console.log(_))
    def decant[A](cb0 : ((A) => Unit) => Unit) : Future[A] = {
      val p = JsPromise[A]()
      def cb(a : A) {
        p.success(a)
      }
      cb0(cb)
      p.future
    }
Here is a sample usage:
    val s = newObject
    s.x = 5
    Storage.local.set(s)
    decant[js.Any](Storage.local.get(s, _)).log()
    //Prints Object {x: 5}


## Cast to js.Dictionary to access fields on js objects using strings
When you need to get a field by using a string that contains
its name do this
    val xs : js.Any = ???
    xs.asInstanceOf[js.Dictionary]
    xs("somefield") // becomes xs["somefield"] in js

# Bugs #
console.log("hi".capitalize)
Uncaught TypeError: Object hi has no method 'toCharArray'
(that is a java method, needs to be added)S

# Suggestions #

Would really be nice to have the asInstanceOf[js.SomeJsType] checks actually validate the types,
maybe isInstanceOf does this already? some sort of structural type checking would be useful
when interacting with APIs from websites to do some validation

I think when the abbreviation js needs to be capitalized in a camel case name, it should be done so as Js not JS
the reason is that in camel casing Js reads as one token whereas JS is two a J and then an S consider programmatically
converting camel case to _ notation JsFuture becomes js_future whereas JSFuture becomes j_s_future

# Tips #

##  debugger
Use this line to pause execution of the script and show a debugger in chrome when the dev tools are open
    js.eval("debugger")
    js.eval("typeof(x)")
Is there a way to encode these language constructs without resorting to hacks?

perhaps js"" blocks would be handy for inserting actual raw javascript into scala that the compiler
then emits at those locations


##  Misc
Install gen-idea plugin

