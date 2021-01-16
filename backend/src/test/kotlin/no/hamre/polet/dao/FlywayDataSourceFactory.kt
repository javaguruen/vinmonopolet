package no.hamre.polet.dao

import org.flywaydb.core.Flyway
import java.util.concurrent.atomic.AtomicInteger
import javax.sql.DataSource


object FlywayDataSourceFactory {
  val DB_NUMBER = AtomicInteger(0)

  fun create(): DataSource {
    val configuration = Flyway.configure()
        .dataSource("jdbc:h2:mem:polet${DB_NUMBER.incrementAndGet()};MODE=PostgreSQL;DB_CLOSE_DELAY=-1", "polet", "polet")
    val flyway = configuration // (url, user, password)
        .load() // Returns a `Flyway` object.

    // Start the migration
    flyway.migrate()
    return configuration.dataSource
  }
}