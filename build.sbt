organization := "opt"
name := "hogehoge"
scalaVersion := "2.12.0"

resolvers ++= Seq(
  Classpaths.typesafeReleases,
  Resolver.jcenterRepo,
  "sonatype-snapshot" at "https://oss.sonatype.org/content/repositories/snapshots/"
)

libraryDependencies ++= Seq(
  "org.eclipse.jgit" % "org.eclipse.jgit" % "4.5.0.201609210915-r",
  "org.eclipse.jgit" % "org.eclipse.jgit.archive" % "4.5.0.201609210915-r",
  "org.eclipse.mylyn.github" % "org.eclipse.egit.github.core" % "2.1.5"
)

