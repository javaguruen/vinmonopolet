package no.hamre.polet.dao

import liquibase.Liquibase
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import liquibase.resource.FileSystemResourceAccessor
import liquibase.resource.ResourceAccessor
import java.io.File
import java.sql.Connection
import java.sql.SQLFeatureNotSupportedException
import java.util.logging.Logger
import javax.sql.DataSource

import org.h2.jdbcx.JdbcDataSource
import java.util.concurrent.atomic.AtomicInteger

class H2LiquibaseDataSourceFactory {

  companion object {
    val DB_NUMBER = AtomicInteger(0)
    val liquibasePath = "migrations.xml"
    val DEFAULT_RESOURCE_ACCESSOR = ClassLoaderResourceAccessor()
    var resourceAccessor: ResourceAccessor = DEFAULT_RESOURCE_ACCESSOR
    var h2DbName = "mem:test" + DB_NUMBER.incrementAndGet()

    fun createDataSource(schemaName: String): DataSource {
      return createDataSource(schemaName, "mem:test" + DB_NUMBER.incrementAndGet())
    }

    fun createDataSource(schemaName: String, h2DbName: String): DataSource {
      return H2LiquibaseDataSourceFactory().createDataSource(schemaName, h2DbName, liquibasePath, DEFAULT_RESOURCE_ACCESSOR)
    }

  }


  fun createDataSource(schemaName: String, h2DbName: String, liquibasePath: String, resourceAccessor: ResourceAccessor): DataSource {
    val ds: JdbcDataSource = LiqDataSource(schemaName)
    var options = "MODE=PostgreSQL;INIT=CREATE SCHEMA IF NOT EXISTS " + schemaName
    if (h2DbName.startsWith("mem:")) {
      options = options + ";DB_CLOSE_DELAY=-1;"
    }

    ds.setURL("jdbc:h2:" + h2DbName + options)
    ds.setUser(schemaName)
    ds.setPassword("")
    var _liquibasePath = liquibasePath
    if (resourceAccessor is FileSystemResourceAccessor && !File(_liquibasePath).exists()) {
      _liquibasePath = "../" + _liquibasePath
      if (!File(_liquibasePath).exists()) {
        throw RuntimeException("Problems finding " + _liquibasePath)
      }
    }

    try {
      val e = ds.getConnection()
      val database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(JdbcConnection(e))
      val liquibase = Liquibase(_liquibasePath, resourceAccessor, database)
      liquibase.update("test")
      return ds
    } catch (e: Exception) {
      throw RuntimeException("Error bootstrapping H2-datasource using liquibase", e)
    }
  }

  class LiqDataSource(private val schemaName: String) : JdbcDataSource() {
    override fun getParentLogger(): Logger {
      throw SQLFeatureNotSupportedException()
    }

    override fun getConnection(): Connection {
      val con = super.getConnection()
      if (schemaName != null) {
        con.createStatement().execute("SET SCHEMA=" + schemaName)
      }

      return con
    }
  }
}


