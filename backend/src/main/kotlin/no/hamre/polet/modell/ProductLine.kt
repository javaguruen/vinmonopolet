package no.hamre.polet.modell

import org.slf4j.LoggerFactory
import java.time.LocalDateTime

object ProductLineHelper {
  private val LOG = LoggerFactory.getLogger(ProductLineHelper::class.java)
  private fun toInt(s: String?): Int? {
    return if (s != null && s.isNotEmpty()) s.toInt() else null
  }

  private fun toDouble(s: String): Double {
    return s.replace(",", ".").toDouble()
  }

  private fun toLocalDateTime(s: String): LocalDateTime {
    return LocalDateTime.parse(s)
  }

  fun create(line: List<String>): Productline? {
    try {
      return Productline(
          id = null,
          datotid = toLocalDateTime(line[0]),
          varenummer = line[1],
          varenavn = line[2],
          volum = toDouble(line[3]),
          pris = toDouble(line[4]),
          literpris = toDouble(line[5]),
          varetype = line[6],
          produktutvalg = line[7],
          butikkategori = line[8],
          farge = line[14],
          lukt = line[15],
          smak = line[16],
          land = line[20],
          distrikt = line[21],
          underdistrikt = line[22],
          aargang = toInt(line[23]),
          raastoff = line[24],
          metode = line[25],
          alkohol = toDouble(line[26]),
          produsent = line[30],
          grossist = line[31],
          distributor = line[32],
          vareurl = line[35].trim(),
          updated = LocalDateTime.now()
      )
    } catch (e: Exception) {
      LOG.error("Error parsing: $line", e)
      return null
    }
  }
}

data class Productline(
    val id: Long?,
    val datotid: LocalDateTime,
    val varenummer: String,
    val varenavn: String,
    val volum: Double,
    val pris: Double,
    val literpris: Double,
    val varetype: String,
    val produktutvalg: String,
    val butikkategori: String,
    val farge: String?,
    val lukt: String?,
    val smak: String?,
    val land: String,
    val distrikt: String?,
    val underdistrikt: String?,
    val aargang: Int?,
    val raastoff: String?,
    val metode: String?,
    val alkohol: Double,
    val produsent: String,
    val grossist: String,
    val distributor: String,
    val vareurl: String,
    val updated: LocalDateTime? = LocalDateTime.now()
)

//2014-10-22T00:56:50;1101;L�iten Linie;0,70;399,90;571,30;
//Akevitt;Basisutvalget;Butikkategori 3;
//0;0;0;0;0;;;;;;;Norge;�vrige;�vrige;;Poteter, krydder;16 mnd p� fat;
// 41,50;5,00;Ukjent;;;Arcus AS;Vectura AS;Engangsflasker av glass;;http://www.vinmonopolet.no/vareutvalg/varedetaljer/sku-1101
