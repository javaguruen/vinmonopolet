package no.hamre.polet.dao

object SqlInputCleaner {
  fun clean(sqlParam: String): String {
    return sqlParam.takeWhile { it.isLetterOrDigit() }
        .toLowerCase()
  }
}