package no.hamre.polet.util

import org.slf4j.{Logger, LoggerFactory}

trait Slf4jLogger {
  val log: Logger = LoggerFactory.getLogger( this.getClass )
}
