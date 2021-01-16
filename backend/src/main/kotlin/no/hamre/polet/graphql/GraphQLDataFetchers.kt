package no.hamre.polet.graphql

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import no.hamre.polet.dao.Dao
import no.hamre.polet.modell.Whisky
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class GraphQLDataFetchers(private val dao: Dao) {

  fun produkterByName(): DataFetcher<*> {
    return DataFetcher { dataFetchingEnvironment: DataFetchingEnvironment ->
      val name = dataFetchingEnvironment.getArgument<String>("name")
      LOG.info("Find whisky by name = {}", name)
      dao.search(name)
    }
  }

  fun produkterFromLatestRelease(): DataFetcher<*> {
    return DataFetcher { dataFetchingEnvironment: DataFetchingEnvironment ->
      LOG.info("Find whiskies from latest release ")
      dao.findBySisteSlipp()
    }
  }

  fun pricesForProduct(): DataFetcher<*> {
    return DataFetcher { dataFetchingEnvironment: DataFetchingEnvironment ->
      val whisky: Whisky = dataFetchingEnvironment.getSource()
      val productId = whisky.id
      dao.findPriser(whiskyId = productId)
    }
  }

  companion object {
    private val LOG = LoggerFactory.getLogger(GraphQLDataFetchers::class.java)
  }
}