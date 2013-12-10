import sbt._
import Keys._
import scala.scalajs.sbtplugin.ScalaJSPlugin._
import ScalaJSKeys._

object Build extends sbt.Build {
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
      libraryDependencies += compilerPlugin(
          "org.scala-lang.plugins" % "continuations" % scalaVersion.value),
      scalacOptions += "-P:continuations:enable"
    ).dependsOn(Libs.dom, Libs.jQuery)

//  lazy val root = project.in(file(".")).settings(
//    scalaJSSettings: _*
//  ).settings(
//    name := "games",
//    unmanagedSources in (Compile, ScalaJSKeys.packageJS) +=
//      baseDirectory.value / "js" / "startup.js"
//  ).dependsOn(Libs.dom, Libs.jQuery)
//
  object Libs {
    lazy val dom = RootProject(file("lib/scala-js-dom"))
    lazy val jQuery = RootProject(file("lib/scala-js-jquery"))
  }
}