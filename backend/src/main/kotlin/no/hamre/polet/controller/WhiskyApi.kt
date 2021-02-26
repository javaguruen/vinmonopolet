package no.hamre.polet.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import no.hamre.polet.modell.Whisky
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping(
  path = ["/api/v1/whiskies"],
  produces = [MediaType.APPLICATION_JSON_VALUE]
)
@RestController("Whiskies")
@Tag(name = "Whisky", description = "Whisky-relaterte operasjoner")
interface WhiskyApi {

  @GetMapping(
    path = ["/{id}"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
  )
  @Operation(summary = "Finn en whisky basert på id")
  @ApiResponses(
    value = [
      ApiResponse(
        responseCode = "200", description = "Fant whiskyen",
        content = [Content(
          mediaType = "application/json",
          schema = Schema(implementation = Whisky::class)
        )
        ]
      ),
      ApiResponse(
        responseCode = "404",
        description = "Ugyldig id",
        content = [Content(schema = Schema(hidden = true))]
      )]
  )
  fun findWhiskyById(
    @Parameter(
      description = "id til whisky",
      required = true,
      `in` = ParameterIn.PATH,
      schema = Schema(implementation = Long::class)
    )
    @PathVariable(name = "id", required = true)
    id: Long
  ): ResponseEntity<Whisky>

  @GetMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE]
  )
  @Operation(summary = "Søk etter whisky")
  @ApiResponses(
    value = [
      ApiResponse(
        responseCode = "200", description = "Ok",
        content = [Content(
          mediaType = "application/json",
          schema = Schema(implementation = Whisky::class)
        )
        ]
      )
    ]
  )
  fun findWhiskies(
    @Parameter(
      description = "Søkekriteriet",
      required = true,
      `in` = ParameterIn.QUERY,
      schema = Schema(implementation = String::class)
    )
    @RequestParam(name = "q", required = true) q: String?
  )
      : ResponseEntity<List<Whisky>>
}