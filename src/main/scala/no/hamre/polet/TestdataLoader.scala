package no.hamre.polet

import javax.sql.DataSource
import no.hamre.polet.util.Slf4jLogger
import org.sql2o.Sql2o
import org.sql2o.quirks.PostgresQuirks

import scala.io.Source

object TestdataLoader extends Slf4jLogger {
  val testdataFilename = "/testdata.sql"

  def load(dataSource: DataSource): Unit = {
    log.info(s"Loading testdata from $testdataFilename")
    val source = Source.fromInputStream(getClass.getResourceAsStream(testdataFilename), "utf-8")
    val text = source.mkString
    source.close()
    val sql2o = new Sql2o(dataSource, new PostgresQuirks())
    val connection = sql2o.beginTransaction()
    try {
      text.split(";").foreach { sql: String =>
        val query = connection.createQuery(sql)
        query.executeUpdate()
      }
      connection.commit(true)
    } catch {
      case e: Exception =>
        log.error(e.getMessage)
        connection.rollback(true)
        throw e
    }
  }
}