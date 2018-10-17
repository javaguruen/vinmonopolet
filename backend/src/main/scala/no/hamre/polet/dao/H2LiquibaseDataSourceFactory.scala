package no.hamre.polet.dao

import java.io.File
import java.sql.{Connection, SQLFeatureNotSupportedException}
import java.util.concurrent.atomic.AtomicInteger
import java.util.logging.Logger
import javax.sql.DataSource

import liquibase.Liquibase
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.{ClassLoaderResourceAccessor, FileSystemResourceAccessor, ResourceAccessor}
import org.h2.jdbcx.JdbcDataSource

object H2LiquibaseDataSourceFactory{
  val DB_NUMBER = new AtomicInteger(0)
  val liquibasePath = "migrations.xml"
  val DEFAULT_RESOURCE_ACCESSOR = new ClassLoaderResourceAccessor()
  var resourceAccessor: ResourceAccessor = DEFAULT_RESOURCE_ACCESSOR
  var h2DbName = "mem:test" + DB_NUMBER.incrementAndGet()

  def createDataSource(schemaName: String): DataSource = {
    createDataSource(schemaName, "mem:test" + DB_NUMBER.incrementAndGet())
  }

  def createDataSource(schemaName: String, h2DbName: String ): DataSource = {
    new H2LiquibaseDataSourceFactory().createDataSource(schemaName, h2DbName, liquibasePath, DEFAULT_RESOURCE_ACCESSOR)
  }

}

class H2LiquibaseDataSourceFactory {

  def createDataSource(schemaName: String , h2DbName: String , liquibasePath:String , resourceAccessor: ResourceAccessor ): DataSource = {
    val ds: JdbcDataSource  = new LiqDataSource(schemaName)
    var options = "MODE=PostgreSQL;INIT=CREATE SCHEMA IF NOT EXISTS " + schemaName
    if (h2DbName.startsWith("mem:")) {
      options = options + ";DB_CLOSE_DELAY=-1;"
    }

    ds.setURL("jdbc:h2:" + h2DbName + options)
    ds.setUser(schemaName)
    ds.setPassword("")
    var _liquibasePath = liquibasePath
    if (resourceAccessor.isInstanceOf[FileSystemResourceAccessor] && !new File(_liquibasePath).exists()) {
      _liquibasePath = "../" + _liquibasePath
      if (!new File(_liquibasePath).exists()) {
        throw new RuntimeException("Problems finding " + _liquibasePath)
      }
    }

    try {
      val e = ds.getConnection()
      val database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(e))
      val liquibase = new Liquibase(_liquibasePath, resourceAccessor, database)
      liquibase.update("test")
      ds
    } catch{
      case e: Exception =>
        throw new RuntimeException("Error bootstrapping H2-datasource using liquibase", e)
    }
  }

  class LiqDataSource(schemaName: String) extends JdbcDataSource {
    override def getParentLogger: Logger = {
      throw new SQLFeatureNotSupportedException()
    }

    override def getConnection: Connection = {
      val con = super.getConnection
      if (schemaName != null) {
        con.createStatement().execute("SET SCHEMA=" + schemaName)
      }

      con
    }
  }
}


