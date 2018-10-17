package no.hamre.polet.parser

import io.dropwizard.testing.FixtureHelpers

class MockFileDownloader extends FileDownloader {
  override def download(filename: String): String = {
    FixtureHelpers.fixture(filename)
  }
}
