import sbt._
import Keys._
import scala.scalajs.sbtplugin.ScalaJSPlugin._
import ScalaJSKeys._

object Build extends sbt.Build {

  val generateHtml = Def.task {
    val log = streams.value.log

    //Copy over the html files, while filling in the template sections
    val htmlSrcDir: File = (sourceDirectory in Compile).value / "html"
    val htmlSrcs: PathFinder = htmlSrcDir * "*.template.html"
    val outDir: File = (crossTarget in Compile).value
    val outMappings = htmlSrcs x Path.rebase(htmlSrcDir, outDir)
    outMappings.flatMap { case (in, out0) =>
      val content = IO.read(in)
      val outDir = out0.getParentFile
      val Re = """.*/([^/]+)\.template\.html""".r
      val basename = Re.findFirstMatchIn(in.getPath).get.group(1)
      //Make a dev version and a release version
      val devFile = outDir / (basename + "-dev.html")
      val releaseFile = outDir / (basename + ".html")

      val devScripts = """<script type="text/javascript" src="example-extdeps.js"></script>
                                  |<script type="text/javascript" src="example-intdeps.js"></script>
                                  |<script type="text/javascript" src="example.js"></script>""".stripMargin

      val releaseScripts = """<script type="text/javascript" src="example-opt.js"></script>"""

      val devOut = content.replace("<!-- insert scalajs -->", devScripts)
      val releaseOut = content.replace("<!-- insert scalajs -->", releaseScripts)

      IO.write(devFile, devOut.getBytes("UTF-8"))
      IO.write(releaseFile, releaseOut.getBytes("UTF-8"))
      List(devFile, releaseFile)

    }
    Seq[File]()
  }

  val copyAll = Def.task {
    val outDir = (crossTarget in Compile).value

    IO.copyFile((baseDirectory in Compile).value / "manifest.json", outDir / "manifest.json")
    IO.copyDirectory((resourceDirectory in Compile).value / "images", outDir / "images")
    IO.copyDirectory((baseDirectory in Compile).value / "jslib", outDir / "jslib")

    Seq[File]()

  }


  lazy val root = project.in(file(".")).settings(
    scalaJSSettings: _*).settings(
        scalacOptions += "-deprecation",
        scalacOptions += "-unchecked",
        scalacOptions += "-feature",
        scalacOptions += "-language:implicitConversions",
        scalacOptions += "-language:existentials",
        scalacOptions += "-language:higherKinds"
      ).settings(
        // Continuation plugin
        autoCompilerPlugins := true,
        libraryDependencies += compilerPlugin("org.scala-lang.plugins" % "continuations" % scalaVersion.value),
        scalacOptions += "-P:continuations:enable",
        resourceGenerators in Compile <+= generateHtml,
        resourceGenerators in Compile <+= copyAll
      ).dependsOn(Libs.dom, Libs.jQuery)

  object Libs {
    lazy val dom    = RootProject(file("lib/scala-js-dom"))
    lazy val jQuery = RootProject(file("lib/scala-js-jquery"))
  }


}


//  lazy val root = project.in(file(".")).settings(
//    scalaJSSettings: _*
//  ).settings(
//    name := "games",
//    unmanagedSources in (Compile, ScalaJSKeys.packageJS) +=
//      baseDirectory.value / "js" / "startup.js"
//  ).dependsOn(Libs.dom, Libs.jQuery)
//
