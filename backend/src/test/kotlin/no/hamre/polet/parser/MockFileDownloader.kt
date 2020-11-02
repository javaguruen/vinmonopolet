package no.hamre.polet.parser

class MockFileDownloader : FileDownloader {
  override fun download(url: String): String {
  return MockFileDownloader::class.java.getResource(url).readText()
//    return "" //FixtureHelpers.fixture(url)
  }
}
