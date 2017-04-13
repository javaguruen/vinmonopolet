package no.hamre.polet.parser

import no.hamre.polet.util.Slf4jLogger
import scala.io.Source

trait FileDownloader {
  def download(): String
}

class FileDownloaderImpl(url: String, encoding: String) extends FileDownloader with Slf4jLogger {
  override def download(): String = {
    val html = Source.fromURL(url, encoding)
    val s = html.mkString
    println(s)
    s
  }
}
