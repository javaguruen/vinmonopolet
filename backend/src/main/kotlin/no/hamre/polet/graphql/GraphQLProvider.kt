package no.hamre.polet.graphql

import graphql.GraphQL
import graphql.schema.GraphQLSchema
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeRuntimeWiring.newTypeWiring
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.IOException
import javax.annotation.PostConstruct


@Configuration
class GraphQLProvider(private val graphQLDataFetchers: GraphQLDataFetchers) {

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
          .dataFetcher("soek", graphQLDataFetchers.soekPaaNavn())
          .dataFetcher("sisteOppdateringer", graphQLDataFetchers.sisteOppdateringer())
      )
      .type(
        newTypeWiring("Whisky")
          .dataFetcher("navn", graphQLDataFetchers.whiskynavn())
          .dataFetcher("destilleri", graphQLDataFetchers.destilleri())
          .dataFetcher("priser", graphQLDataFetchers.priserForWhisky())
      )
      .build()
  }
}