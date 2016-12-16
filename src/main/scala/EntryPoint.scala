
object EntryPoint {
  import scala.util.Try
  import scala.collection.JavaConverters._
  import scala.io.Source._
  import java.io.{File, IOException}
  import org.eclipse.jgit.api.Git
  import org.eclipse.jgit.api.errors.GitAPIException
  import org.eclipse.jgit.revwalk.filter.RevFilter
  import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider

  def main(args: Array[String]) = 
    args.headOption match {
      case None => println("No repository specified.")
      case Some(url) => 
        val localPath = File.createTempFile("TestGitRepository", "")
        if(!localPath.delete()) {
            throw new IOException(s"Could not delete temporary file $localPath")
        }

        // then clone
        println(s"Cloning from $url to $localPath")

        val credential = Try{ fromFile("GH_CREDENTIAL") }.toOption
          .flatMap(_.getLines.toStream.headOption)
          .flatMap(
            _.split(",").toSeq match {
              case Seq(u, p) => Some((u, p))
              case _ => None
            }
          )

        val cloneCommand = Git.cloneRepository()
                              .setURI(url)
                              .setDirectory(localPath)
        credential.foreach {
          case (u, p) => 
            cloneCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(u, p)) 
        }

        Try { cloneCommand.call() }.toOption match {
          case Some(git) =>
            println(s"Having repository: ${git.getRepository().getDirectory()}")
            git
              .log()
              .setRevFilter(RevFilter.ONLY_MERGES)
              .call()
              .asScala
              .foreach{ l => println(l.getShortMessage()) } 
          case _ => println("clone failed.")
        }
    }

}
