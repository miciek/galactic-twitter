name := "galactic-twitter"

organization := "miciek"

version := "1.0-SNAPSHOT"

scalaVersion := "2.12.2"

resolvers += Resolver.jcenterRepo

libraryDependencies ++= {
  val akkaV = "2.4.17"
  val akkaHttpV = "10.0.5"
  val configV = "1.3.1"
  val scalatestV = "3.0.1"
  val logbackV = "1.1.5"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-http" % akkaHttpV,
    "com.typesafe" % "config" % configV,
    "com.typesafe.akka" %% "akka-slf4j" % akkaV,
    "ch.qos.logback" % "logback-classic" % logbackV,
    "org.scalatest" %% "scalatest" % scalatestV % Test,
    "com.typesafe.akka" %% "akka-testkit" % akkaV % Test
  )
}

fork := true
connectInput in run := true

import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys

SbtScalariform.scalariformSettings

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(AlignParameters, true)
  .setPreference(AlignArguments, true)
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(DoubleIndentClassDeclaration, true)
  .setPreference(DanglingCloseParenthesis, Preserve)
  .setPreference(RewriteArrowSymbols, true)
