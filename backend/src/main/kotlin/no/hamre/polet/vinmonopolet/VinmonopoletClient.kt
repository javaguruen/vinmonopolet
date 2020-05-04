package no.hamre.polet.vinmonopolet

import no.hamre.polet.ObjectMapperFactory
import no.hamre.polet.modell.Productline
import no.hamre.polet.vinmonopolet.model.Product
import org.slf4j.LoggerFactory
import javax.ws.rs.client.Client
import javax.ws.rs.core.Response

interface VinmonopoletClient {
  fun doRequest(start: Int = 0, maxResults: Int = 400): BatchData
}

class VinmonopoletClientImpl(
    private val url: String,
    private val jerseyClient: Client,
    private val apiKey: String
) : VinmonopoletClient {
  private val mapper = ObjectMapperFactory.create()
  private val LOG = LoggerFactory.getLogger(VinmonopoletClientImpl::class.java)


  override fun doRequest(start: Int, maxResults: Int): BatchData {
    val response: Response = jerseyClient.register(mapper).target(url)
        .path("products").path("v0").path("details-normal")
        .queryParam("maxResults", maxResults)
        .queryParam("start", start)
        .request()
        .header("Ocp-Apim-Subscription-Key", apiKey)
        .get()
    //.get(String::class.java)
    val status = response.status
    val totalCount = response.headers.getFirst("x-total-count") as String
    val links: List<String> = response.headers.get("link")?.map { it as String } ?: emptyList()
    LOG.info("Status: $status, totalcount: $totalCount, links: $links")
    val productsAsString = response.readEntity(String::class.java)
    //LOG.info(productsAsString)
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