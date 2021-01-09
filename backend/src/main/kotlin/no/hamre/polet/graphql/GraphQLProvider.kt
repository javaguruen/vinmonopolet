package no.hamre.polet.graphql

import graphql.GraphQL
import graphql.schema.GraphQLSchema
import org.springframework.context.annotation.Bean
import java.io.IOException
import javax.annotation.PostConstruct
import graphql.schema.idl.SchemaGenerator

import graphql.schema.idl.RuntimeWiring

import graphql.schema.idl.SchemaParser

import graphql.schema.idl.TypeRuntimeWiring.newTypeWiring
import no.hamre.polet.service.ProductDataService
import org.springframework.context.annotation.Configuration


@Configuration
class GraphQLProvider(
  private val graphQLDataFetchers: GraphQLDataFetchers) {

  private var graphQL: GraphQL? = null

  @Bean
  fun graphQL(): GraphQL {
    return graphQL!!
  }

  @PostConstruct
  @Throws(IOException::class)
  fun init() {
    val sdl: String = this::class.java.getResource("/schema.graphqls").readText(Charsets.UTF_8)
    val graphQLSchema: GraphQLSchema = buildSchema(sdl)
    graphQL = GraphQL.newGraphQL(graphQLSchema).build()
  }

  private fun buildSchema(sdl: String): GraphQLSchema {
    val typeRegistry = SchemaParser().parse(sdl)
    val runtimeWiring: RuntimeWiring = buildWiring()
    val schemaGenerator = SchemaGenerator()
    return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring)
  }

  private fun buildWiring(): RuntimeWiring {
    return RuntimeWiring.newRuntimeWiring()
      .type(
        newTypeWiring("Query")
          .dataFetcher("soek", graphQLDataFetchers.soek())
          .dataFetcher("sisteSlipp", graphQLDataFetchers.sisteSlipp())
      )
      .type(
        newTypeWiring("Whisky")
          .dataFetcher("aktiv", graphQLDataFetchers.whiskyAktiv())
          .dataFetcher("priser", graphQLDataFetchers.pricesForProduct())
      )
      .build()
  }
}