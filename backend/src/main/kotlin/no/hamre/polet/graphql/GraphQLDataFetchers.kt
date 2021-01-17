package no.hamre.polet.graphql

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import no.hamre.polet.dao.Dao
import no.hamre.polet.modell.Whisky
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class GraphQLDataFetchers(private val dao: Dao) {

  fun soekPaaNavn(): DataFetcher<*> {
    return DataFetcher { dataFetchingEnvironment: DataFetchingEnvironment ->
      val navn = dataFetchingEnvironment.getArgument<String>("navn")
      LOG.info("Find whisky by name = {}", navn)
      dao.search(navn)
    }
  }

  fun sisteOppdateringer(): DataFetcher<*> {
    return DataFetcher {
      LOG.info("Find whiskies from latest release ")
      dao.findBySisteSlipp()
    }
  }

  fun priserForWhisky(): DataFetcher<*> {
    return DataFetcher { dataFetchingEnvironment: DataFetchingEnvironment ->
      val whisky: Whisky = dataFetchingEnvironment.getSource()
      val whiskyId = whisky.id
      dao.findPriser(whiskyId = whiskyId)
    }
  }

  fun whiskynavn(): DataFetcher<Any>? {
    return DataFetcher { dataFetchingEnvironment: DataFetchingEnvironment ->
      val whisky: Whisky = dataFetchingEnvironment.getSource()
      whisky.varenavn
    }
  }

  fun destilleri(): DataFetcher<Any>? {
    return DataFetcher { dataFetchingEnvironment: DataFetchingEnvironment ->
      val whisky: Whisky = dataFetchingEnvironment.getSource()
      whisky.produsent
    }
  }

  companion object {
    private val LOG = LoggerFactory.getLogger(GraphQLDataFetchers::class.java)
  }
}