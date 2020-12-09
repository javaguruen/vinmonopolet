package no.hamre.polet.graphql

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import no.hamre.polet.service.ProductDataService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class GraphQLDataFetchers(private val productService: ProductDataService) {
    fun produkterByName(): DataFetcher<*> {
        return DataFetcher { dataFetchingEnvironment: DataFetchingEnvironment ->
            val name = dataFetchingEnvironment.getArgument<String>("name")
            LOG.info("Find whisky by name = {}", name)
            productService.findProducts(name)
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(GraphQLDataFetchers::class.java)
    }
}