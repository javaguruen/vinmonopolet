package no.hamre.polet.dao

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SqlInputCleanerTest{
  @Test
  fun `remove non-letters and non-digits`() {
    assertEquals( "ardbeg", SqlInputCleaner.clean("Ardbeg"))
    assertEquals( "area42", SqlInputCleaner.clean("Area42'xy"))
  }
}