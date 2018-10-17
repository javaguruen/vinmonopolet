package no.hamre.polet.parser

import org.scalatest.FunSuite

class FileDownloaderTest extends FunSuite {
  ignore("Download file to String"){
    val url = "https://www.vinmonopolet.no/medias/sys_master/products/products/hbc/hb0/8834253127710/produkter.csv"
    val downloader = new FileDownloaderImpl("windows-1252")
    println( downloader.download(url) )
  }
}
