
object EntryPoint {
  import scala.util.Try
  import java.io.{File, IOException}
  import org.eclipse.jgit.api.Git
  import org.eclipse.jgit.api.errors.GitAPIException
  import org.eclipse.jgit.revwalk.filter.RevFilter
  import scala.collection.JavaConverters._

  def main(args: Array[String]) = 
    args.headOption match {
      case Some(url) => 
        val localPath = File.createTempFile("TestGitRepository", "")
        if(!localPath.delete()) {
            throw new IOException(s"Could not delete temporary file $localPath")
        }

        // then clone
        println(s"Cloning from $url to $localPath")
        Try {
          Git.cloneRepository()
                .setURI(url)
                .setDirectory(localPath)
                .call()
        }.foreach{ git =>
        // Note: the call() returns an opened repository already which needs to be closed to avoid file handle leaks!
          println(s"Having repository: ${git.getRepository().getDirectory()}")

          git.log().setRevFilter(RevFilter.ONLY_MERGES)//.addPath("--merges")
            .call().asScala.foreach{ l => println(l.getShortMessage()) } 
        }
      case None => println("No repository specified.")
    }

}
