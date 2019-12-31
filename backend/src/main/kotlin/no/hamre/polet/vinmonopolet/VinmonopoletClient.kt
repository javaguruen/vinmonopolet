package no.hamre.polet.vinmonopolet

import no.hamre.polet.ObjectMapperFactory
import no.hamre.polet.vinmonopolet.model.Product
import javax.ws.rs.client.Client

interface VinmonopoletClient {

}

class VinmonopoletClientImpl(
    private val url: String,
    private val jerseyClient: Client,
    private val apiKey: String
) {
  private val mapper = ObjectMapperFactory.create()

  fun doRequest(maxResults: Int = 100): List<Product> {
    val response: String = jerseyClient.target(url)
        .path("products").path("v0").path("details-normal")
        .queryParam("maxResults", maxResults)
        .request()
        .header("Ocp-Apim-Subscription-Key", apiKey)
        .get(String::class.java)
    println("Received: \n$response")
    return mapper.readValue(response, Array<Product>::class.java).toList()
  }

}