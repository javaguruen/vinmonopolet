package no.hamre.polet.controller


import no.hamre.polet.modell.Whisky
import no.hamre.polet.service.ProductDataService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class WhiskyController(private val service: ProductDataService) : WhiskyApi {

  override fun findWhiskyById(@PathVariable(name = "id", required = true) id: Long): ResponseEntity<Whisky> {
    log.info("GET /api/v1/whiskies/$id")
    return try {
      service.findWhiskyById(id)
        ?.let { p -> ResponseEntity.ok(p) }
        ?: ResponseEntity.notFound().build()
    } catch (e: Exception) {
      log.error("Exception getting whisky with id: $id. ${e.message}")
      ResponseEntity.status(500).build()
    }
  }

  override fun findWhiskies(q: String?): ResponseEntity<List<Whisky>> {
    log.info("GET /api/v1/whiskies?q=$q")
    if (q.isNullOrEmpty()) {
      return ResponseEntity.badRequest().build()
    }
    return try {
      val whiskies = service.findWhiskies(q)
      when (whiskies.size) {
        0 -> ResponseEntity.notFound().build()
        else -> ResponseEntity.ok(whiskies)
      }
    } catch (e: Exception) {
      log.error("Exception searching for whiskies")
      ResponseEntity.status(500).build()
    }
  }

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }
}
