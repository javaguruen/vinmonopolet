package no.hamre.polet.vinmonopolet

import no.hamre.polet.ObjectMapperFactory
import no.hamre.polet.modell.Productline
import no.hamre.polet.vinmonopolet.model.Product
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder


interface VinmonopoletClient {
  fun doRequest(start: Int = 0, maxResults: Int = 400): BatchData
}

@Component
class VinmonopoletClientImpl(
    @Value("\${vinmonopolet.url}") private val url: String,
    @Value("\${vinmonopolet.apiKey}") private val apiKey: String
) : VinmonopoletClient {
  private val mapper = ObjectMapperFactory.create()
  private val LOG = LoggerFactory.getLogger(VinmonopoletClientImpl::class.java)


  override fun doRequest(start: Int, maxResults: Int): BatchData {
    val uriString = UriComponentsBuilder
        .fromUriString(url)
        .path("/products")
        .path("/v0")
        .path("/details-normal")
        .queryParam("maxResults", maxResults)
        .queryParam("start", start)
        .build().toUriString()
    println("Calling $uriString")
    val headers = HttpHeaders()
    headers["Ocp-Apim-Subscription-Key"] = apiKey
    val entity: HttpEntity<*> = HttpEntity<Any>(headers)

    val response = RestTemplate()
        .exchange(uriString, HttpMethod.GET, entity, String::class.java)


      //.get(String::class.java)
      val status = response.statusCodeValue
      val totalCount = response.headers.getFirst("x-total-count") as String
      val links: List<String> = response.headers.get("link")?.map { it as String } ?: emptyList()
      LOG.info("Status: $status, totalcount: $totalCount, links: $links")
      val productsAsString = response.body
      LOG.info(productsAsString)
      val products = mapper.readValue(productsAsString, Array<Product>::class.java)
      return BatchData(
          MetaData(
              start = start,
              max = maxResults,
              totalCount = totalCount.toInt()
          ),
          products
              .asSequence()
              .filter { it.isWhisky() }
              .map { VinmonopoletApiMapper.map2ProductLine(it) }
              .toList()
      )
  }

  fun getNextBatchOfWhiskies(metaData: MetaData = MetaData()): BatchData? {
    if (metaData.totalCount == Int.MAX_VALUE) {
      //first call
      return doRequest(metaData.start, metaData.max)
    } else if (metaData.isDone) {
      return null
    } else {
      //Increase and get next batch
      return doRequest(metaData.start + metaData.max, metaData.max)
    }

  }
}

data class MetaData(
    val start: Int = 0,
    val max: Int = 400,
    val totalCount: Int = Int.MAX_VALUE
) {
  val isDone: Boolean
    get() = (start + max) >= totalCount
}

data class BatchData(
    val metaData: MetaData,
    val whiskies: List<Productline>
)