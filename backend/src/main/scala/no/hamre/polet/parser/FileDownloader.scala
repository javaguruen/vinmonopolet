package no.hamre.polet.parser

import no.hamre.polet.util.Slf4jLogger
import scala.io.Source

trait FileDownloader {
  def download(url: String): String
}

class FileDownloaderImpl(encoding: String) extends FileDownloader with Slf4jLogger {

  override def download(url: String): String = {
    log.info(s"Prepare to download from $url")
    val html = Source.fromURL(url, encoding)
    val s = html.mkString
    log.debug(s)
    s
  }


}
