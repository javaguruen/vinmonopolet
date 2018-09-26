package no.hamre.polet

import com.fasterxml.jackson.databind.{ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.scala.DefaultScalaModule

object ObjectMapperFactory {
  def configure(objectMapper: ObjectMapper): ObjectMapper = {
    objectMapper.registerModule( DefaultScalaModule )
    objectMapper.registerModule(new JavaTimeModule())
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT)
  }
  def create : ObjectMapper = {
    configure( new ObjectMapper )
  }
}
