package no.hamre.polet.graphql

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import no.hamre.polet.dao.Dao
import no.hamre.polet.modell.Product
import no.hamre.polet.service.ProductDataService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class GraphQLDataFetchers(private val dao: Dao) {

  fun soek(): DataFetcher<*> {
    return DataFetcher { dataFetchingEnvironment: DataFetchingEnvironment ->
      val name = dataFetchingEnvironment.getArgument<String>("name")
      LOG.info("Find whisky by name = {}", name)
      dao.query(name)
    }
  }

  fun sisteSlipp(): DataFetcher<*> {
    return DataFetcher { dataFetchingEnvironment: DataFetchingEnvironment ->
      LOG.info("Find whiskies from latest release ")
      dao.findLatestReleases()
    }
  }

  fun pricesForProduct(): DataFetcher<*> {
    return DataFetcher { dataFetchingEnvironment: DataFetchingEnvironment ->
      val whisky: Product = dataFetchingEnvironment.getSource()
      val productId = whisky.id
      dao.findPrices(productId = productId)
    }
  }

  fun whiskyAktiv(): DataFetcher<*> {
    return DataFetcher { dataFetchingEnvironment: DataFetchingEnvironment ->
      val whisky: Product = dataFetchingEnvironment.getSource()
      whisky.active
    }
  }

  companion object {
    private val LOG = LoggerFactory.getLogger(GraphQLDataFetchers::class.java)
  }
}