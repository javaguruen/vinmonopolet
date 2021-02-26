package no.hamre.polet.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import no.hamre.polet.modell.LatestProductchange
import no.hamre.polet.service.Oppdateringsstatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping(path = ["/api/v1/releases"], produces = [MediaType.APPLICATION_JSON_VALUE])
@Tag(name = "Release", description = "Operasjoner relatert til releaser eller slipp.")
interface ReleaseApi {

  @GetMapping("/latest")
  @Operation(summary = "Sist endrede whiskyer")
  @ApiResponses(
    value = [
      ApiResponse(
        responseCode = "200", description = "Whiskyer som ble lagt til eller endret pris ved siste oppdatering.",
        content = [Content(
          array = ArraySchema(schema = Schema(implementation = LatestProductchange::class)),

          )]
      )
    ]
  )
  fun sisteEndringer(): ResponseEntity<List<LatestProductchange>>


  @PutMapping("/update")
  @Operation(summary = "Oppdater med data fra vinmonopolet")
  @ApiResponses(
    value = [
      ApiResponse(
        responseCode = "200", description = "Oppdatert data",
        content = [Content(
          schema = Schema(implementation = Oppdateringsstatus::class)
        )
        ]
      )
    ]
  )
  fun updateFromVinmonopolet(): ResponseEntity<Oppdateringsstatus>
}