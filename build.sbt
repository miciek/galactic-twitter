name := "galactic-twitter"

organization := "miciek"

version := "1.0-SNAPSHOT"

scalaVersion := "2.12.2"

resolvers += Resolver.jcenterRepo

libraryDependencies ++= {
  val akkaV = "2.4.18"
  val akkaHttpV = "10.0.6"
  val vavrV = "0.9.0"
  val configV = "1.3.1"
  val logbackV = "1.1.5"
  val scalatestV = "3.0.1"
  val junitV = "4.12"
  val junitInterfaceV = "0.11"
  Seq(
    "com.typesafe.akka" %% "akka-http" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-jackson" % akkaHttpV,
    "io.vavr" % "vavr" % vavrV,
    "io.vavr" % "vavr-test" % vavrV,
    "com.typesafe" % "config" % configV,
    "com.typesafe.akka" %% "akka-slf4j" % akkaV,
    "ch.qos.logback" % "logback-classic" % logbackV,
    "org.scalatest" %% "scalatest" % scalatestV % Test,
    "junit" % "junit" % junitV % Test,
    "com.novocode" % "junit-interface" % junitInterfaceV % "test"
  )
}

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
