enablePlugins(ScalaJSPlugin)

name := "flocking"

version := "0.1"

scalaVersion := "2.12.4"

scalaJSUseMainModuleInitializer := true

libraryDependencies ++= Seq("org.typelevel" %%% "cats-core"    % "1.0.1",
                            "org.scala-js"  %%% "scalajs-dom" % "0.9.1",
                            "org.scalatest" %% "scalatest"    % "3.0.5" % "test")
