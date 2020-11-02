package no.hamre.polet.parser

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.net.URL
import java.nio.charset.Charset

interface FileDownloader {
  fun download(url: String): String
}

@Component
class FileDownloaderImpl(@Value("\${vinmonopolet.encoding}") private val encoding: String) : FileDownloader {
  private val log = LoggerFactory.getLogger(this.javaClass)

  override fun download(url: String): String {
    log.info("Prepare to download from $url")
    val html = URL(url).readText(Charset.forName(encoding))
    log.debug(html)
    return html
  }


}
