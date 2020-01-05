package no.hamre.polet.vinmonopolet.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import no.hamre.polet.ObjectMapperFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class ModelParserTest {
  @Test
  @Disabled
  internal fun `skip empty values when deserializing`() {
    val mapper = poletMapper()
    val model = mapper.readValue(json, Model::class.java)
    assertNull(model.name)
  }

  @Test
  internal fun `skip empty values when serializing`() {
    val mapper = poletMapper()
    val model = Model("", "", "")
    val serialized = mapper.writeValueAsString(model)
    println(serialized)
    assertEquals("{ }", serialized)
  }

  private fun poletMapper(): ObjectMapper {
    var mapper = ObjectMapperFactory.create()
    mapper = mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
    mapper = mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY)
    return mapper
  }

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  data class Model(val name: String?, val date: String?, val number: String?)

  val json = """
    {
      "name": "",
      "date": "0000-00-00",
      "number": "0"
    }
  """.trimIndent()
}