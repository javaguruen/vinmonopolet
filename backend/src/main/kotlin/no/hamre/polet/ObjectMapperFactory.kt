package no.hamre.polet

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule

object ObjectMapperFactory {
  fun configure(objectMapper: ObjectMapper): ObjectMapper {
    objectMapper.registerModule(JavaTimeModule())
    objectMapper.registerModule(KotlinModule())
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT)
    return objectMapper
  }

  fun create(): ObjectMapper {
    return configure(ObjectMapper())
  }
}