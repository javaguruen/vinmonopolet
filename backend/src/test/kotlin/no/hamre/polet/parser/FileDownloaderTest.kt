package no.hamre.polet.parser

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class FileDownloaderTest {
  @Test
  @Disabled("For manual run only")
  fun `Download file to String`() {
    val url = "https://www.vinmonopolet.no/medias/sys_master/products/products/hbc/hb0/8834253127710/produkter.csv"
    val downloader = FileDownloaderImpl("windows-1252")
    println(downloader.download(url))
  }
}
