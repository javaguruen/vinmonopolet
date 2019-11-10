package no.hamre.polet

import org.slf4j.LoggerFactory
import org.sql2o.Sql2o
import org.sql2o.quirks.PostgresQuirks
import javax.sql.DataSource

object TestdataLoader {
  private const val testdataFilename = "/testdata.sql"
  private val log = LoggerFactory.getLogger(javaClass)

  fun load(dataSource: DataSource) {
    log.info("Loading testdata from $testdataFilename")

    val text = javaClass.getResourceAsStream(testdataFilename).bufferedReader(Charsets.UTF_8).readText()
    val sql2o = Sql2o(dataSource, PostgresQuirks())
    val connection = sql2o.beginTransaction()
    try {
      text.split(";").forEach { sql: String ->
        val query = connection.createQuery(sql)
        query.executeUpdate()
      }
      connection.commit(true)
    } catch (e: Exception) {
      log.error(e.message)
      connection.rollback(true)
      throw e
    }
  }
}