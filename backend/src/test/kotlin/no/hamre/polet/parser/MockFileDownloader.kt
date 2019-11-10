package no.hamre.polet.parser

import io.dropwizard.testing.FixtureHelpers

class MockFileDownloader : FileDownloader {
  override fun download(url: String): String {
    return FixtureHelpers.fixture(url)
  }
}
