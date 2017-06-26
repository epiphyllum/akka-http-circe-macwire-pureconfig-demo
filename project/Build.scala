import sbt.Keys._
import sbt._
import sbtassembly.{Assembly, MergeStrategy, PathList}
import spray.revolver.RevolverPlugin.autoImport.Revolver

object Resolvers {
}

object Dependencies {
  // private val akka = "2.4.19"
  private val akka = "2.5.3"
  private val akkaHttp = "10.0.8"

  private val leveldb = "0.7"
  private val leveldbjniAll = "1.8"
  private val akkaPersistenceRedis = "0.6.0"

  private val slick = "3.2.0"
  private val mysqlConnectorJava = "6.0.5"
  private val hikaricp = "2.4.5"

  private val accordCore = "0.6.1"
  private val flywayCore = "4.2.0"

  private val freemarker = "2.3.23"
  private val thymeleaf = "3.0.2.RELEASE"

  private val scalazCore = "7.2.11"
  private val commonsCompress = "1.2"
  private val commonsIO = "2.5"
  private val quicklens = "1.4.8"

  private val camelJetty = "2.19.1"
  private val camelQuartz = "2.19.1"

  // private val scalapbRuntime = "0.5.34"

  private val logbackClassic = "1.1.3"

  private val scalaTest = "3.0.0"

  private val jacksonFormat = "2.7.6"

  private val neo4j = "3.1.3"

  private val mongodbScala = "2.1.0"


  val appDependencies = Seq(
    // akka and cluster
    "com.typesafe.akka" %% "akka-actor" % akka,
    "com.typesafe.akka" %% "akka-persistence" % akka,
    "com.typesafe.akka" %% "akka-cluster" % akka,
    "com.typesafe.akka" %% "akka-cluster-tools" % akka,
    "com.typesafe.akka" %% "akka-cluster-sharding" % akka,
    "com.typesafe.akka" %% "akka-multi-node-testkit" % akka % "test",
    "com.typesafe.akka" %% "akka-contrib" % akka,
    "com.typesafe.akka" %% "akka-remote" % akka,
    "com.typesafe.akka" %% "akka-stream" % akka,
    "com.typesafe.akka" %% "akka-stream-testkit" % akka % "test",
    "com.typesafe.akka" %% "akka-slf4j" % akka,
    "com.typesafe.akka" %% "akka-camel" % akka,

    // camel integration
    "org.apache.camel" % "camel-jetty" % camelJetty,
    "org.apache.camel" % "camel-quartz" % camelQuartz,

    // akka http now is production-ready
    "com.typesafe.akka" %% "akka-http-core" % akkaHttp,
    "com.typesafe.akka" %% "akka-http" % akkaHttp,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttp % "test",
    "com.typesafe.akka" %% "akka-http-xml" % akkaHttp,


    //    "com.softwaremill.akka-http-session" %% "core" % akkaHttpSession,

    // akka-persistence
    //"com.hootsuite" %% "akka-persistence-redis" % akkaPersistenceRedis excludeAll (
    //  ExclusionRule(organization = "com.typesafe.akka")
    //  ),
    "org.iq80.leveldb" % "leveldb" % leveldb,
    "org.fusesource.leveldbjni" % "leveldbjni-all" % leveldbjniAll,

    // compiler scalameta
    "org.scala-lang" % "scala-reflect" % "2.11.11",
    "org.scala-lang" % "scala-compiler" % "2.11.11" % "provided",
    "org.scalameta" %% "scalameta" % "1.7.0" % "provided",

    //    // database: slick and flyway
    "com.typesafe.slick" %% "slick" % slick,
    "org.flywaydb" % "flyway-core" % flywayCore,
    "mysql" % "mysql-connector-java" % mysqlConnectorJava,

    // test
    "org.scalatest" %% "scalatest" % scalaTest % "test",

    // validation
    "com.wix" %% "accord-core" % accordCore,

    // lens
    "com.softwaremill.quicklens" %% "quicklens" % quicklens,

    // logger
    "ch.qos.logback" % "logback-classic" % logbackClassic,
    //    "org.slf4j" % "slf4j-nop" % "1.6.4",

    // neo4j
    //     "org.neo4j" % "neo4j" % neo4j,
    "commons-codec" % "commons-codec" % "1.10",
    // "org.anormcypher" %% "anormcypher" % "0.10.0",

    // mongodb
    "org.mongodb.scala" %% "mongo-scala-driver" % mongodbScala,

    // scalaz
    "org.scalaz" %% "scalaz-core" % scalazCore,

    //files tar.gz
    "org.apache.commons" % "commons-compress" % commonsCompress,
    "commons-io" % "commons-io" % commonsIO,

    // backend template
    "org.freemarker" % "freemarker" % freemarker,
    "org.thymeleaf" % "thymeleaf" % thymeleaf,

    // scala protobuf
    // "com.trueaccord.scalapb" %% "scalapb-runtime" % scalapbRuntime % "protobuf",

    // redis client
    "com.github.etaty" %% "rediscala" % "1.8.0",

    // scala-async
    "org.scala-lang.modules" %% "scala-async" % "0.9.6",

    // jwt
    "com.pauldijou" % "jwt-core_2.11" % "0.12.1",

    // recaptcha
    "com.github.axet" % "kaptcha" % "0.0.9",

    // pure config
    "com.github.pureconfig" %% "pureconfig" % "0.7.2",

    // slick
    "com.typesafe.slick" %% "slick-hikaricp" % "3.2.0",
    "io.underscore" %% "slickless"  % "0.3.2",

    // 识别文件类型
    "org.apache.tika" % "tika" % "1.14",

    // circe json序列化
    "io.circe" %% "circe-core" % "0.8.0",
    "io.circe" %% "circe-generic" % "0.8.0",
    "io.circe" %% "circe-parser" % "0.8.0",
    "de.heikoseeberger" %% "akka-http-circe" % "1.16.1",


    "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided",
    "com.softwaremill.macwire" %% "macrosakka" % "2.3.0" % "provided",
    "com.softwaremill.macwire" %% "util" % "2.3.0",
    "com.softwaremill.macwire" %% "proxy" % "2.3.0",

    "nl.grons" %% "metrics-scala" % "3.5.8",

  // shapeless
    "com.chuusai" %% "shapeless" % "2.3.2"

  )
}

object BuildSettings {

  val buildOrganization = "com.yimei.jfc"
  val appName = "jfc"
  val buildVersion = "0.0.5"
  val buildScalaVersion = "2.11.11"
  val buildScalaOptions = Seq("-unchecked", "-deprecation", "-feature", "-encoding", "utf8")

  // neo4j
  resolvers ++= Seq(
    Resolver.bintrayRepo("scalameta", "maven"),
    "anormcypher" at "http://repo.anormcypher.org/",
    "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"
  )

  import Dependencies._

  val buildSettings = Defaults.coreDefaultSettings ++ Seq(
    organization := buildOrganization,
    version := buildVersion,
    scalaVersion := buildScalaVersion,
    libraryDependencies ++= appDependencies,
    scalacOptions := buildScalaOptions,
    fork in Test := true,
    javaOptions += "-Ddev=true"
  ) ++ Revolver.settings
}


object PublishSettings {

  // publish settings
  val publishSettings = Seq(
    credentials += Credentials("Sonatype Nexus Repository Manager", "maven.yimei180.com", "admin", "admin123"),
    publishMavenStyle := true,
    isSnapshot := false,
    publishArtifact in Test := false,
    pomIncludeRepository := { (repo: MavenRepository) => false },
    pomExtra := pomXml,
    publishTo := {
      if (isSnapshot.value)
        Some("Sonatype Nexus Repository Manager" at "http://maven.yimei180.com/content/repositories/snapshots")
      else
        Some("Sonatype Nexus Repository Manager" at "http://maven.yimei180.com/content/repositories/releases")
    }
  )

  lazy val pomXml = {
    <url>https://github.com/epiphyllum/zflow</url>
      <licenses>
        <license>
          <name>Apache License 2.0</name>
          <url>http://www.apache.org/licenses/</url>
          <distribution>repo</distribution>
        </license>
      </licenses>
      <scm>
        <url>git@github.com:epiphyllum/zflow.git</url>
        <connection>scm:git:git@github.com:epiphyllum/zflow.git</connection>
      </scm>
      <developers>
        <developer>
          <id>hary</id>
          <name>hary</name>
          <url>http://github.com/epiphyllum</url>
        </developer>
      </developers>
  }
}


object AssemblySettings {

  import sbtassembly.AssemblyKeys._

  mainClass in assembly := Some("com.yimei.template.JfcApp")

  val mergeSetting = assemblyMergeStrategy in assembly := {
    case PathList(ps@_*) if ps.last endsWith "StaticLoggerBinder.class" => MergeStrategy.deduplicate
    case PathList(ps@_*) if ps.last endsWith "StaticMDCBinder.class" => MergeStrategy.deduplicate
    case PathList(ps@_*) if ps.last endsWith "StaticMarkerBinder.class" => MergeStrategy.deduplicate
    case PathList(ps@_*) if Assembly.isReadme(ps.last) || Assembly.isLicenseFile(ps.last) => MergeStrategy.rename
    case PathList(ps@_*) if ps.last endsWith "MANIFEST.MF" => MergeStrategy.rename
    case PathList(ps@_*) if ps.last endsWith "DEPENDENCIES" => MergeStrategy.rename
    case PathList(ps@_*) if ps.last endsWith "INDEX.LIST" => MergeStrategy.rename
    case PathList(ps@_*) if ps.last endsWith "LICENSES.txt" => MergeStrategy.rename
    case PathList(ps@_*) if ps.last endsWith "BCKEY.DSA" => MergeStrategy.concat
    case PathList(ps@_*) if ps.last endsWith "BCKEY.SF" => MergeStrategy.concat
    case PathList(ps@_*) if ps.last endsWith "pom.properties" => MergeStrategy.rename
    case PathList(ps@_*) if ps.last endsWith "reference.conf" => MergeStrategy.concat
    case PathList(ps@_*) if ps.last endsWith "pom.xml" => MergeStrategy.concat
    case PathList(ps@_*) if ps.last endsWith "component.properties" => MergeStrategy.concat
    case PathList(ps@_*) if ps.last endsWith "com.fasterxml.jackson.databind.Module" => MergeStrategy.concat
    case PathList(ps@_*) if ps.last endsWith "org.neo4j.kernel.Version" => MergeStrategy.rename
    case PathList(ps@_*) if ps.last endsWith "org.neo4j.kernel.extension.KernelExtensionFactory" => MergeStrategy.rename
    case PathList(ps@_*) if ps.last endsWith "TypeConverter" => MergeStrategy.rename
    case PathList(ps@_*) if ps.last endsWith "rootdoc.txt" => MergeStrategy.rename
    case PathList(ps@_*) if ps.last endsWith "com.fasterxml.jackson.core.JsonFactory" => MergeStrategy.concat
    case PathList(ps@_*) if ps.last endsWith "com.fasterxml.jackson.core.ObjectCodec" => MergeStrategy.concat
    case "application.conf" => MergeStrategy.last
    case _ => MergeStrategy.deduplicate
  }

}
