package no.hamre.polet.parser

import org.scalatest.FunSuite

class FileDownloaderTest extends FunSuite {
  ignore("Download file to String"){
    val downloader = new FileDownloaderImpl("https://www.vinmonopolet.no/medias/sys_master/products/products/hbc/hb0/8834253127710/produkter.csv", "windows-1252")
    println( downloader.download() )
  }
}
