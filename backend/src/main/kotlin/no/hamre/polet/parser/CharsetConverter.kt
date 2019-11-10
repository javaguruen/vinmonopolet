package no.hamre.polet.parser

import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.charset.Charset
import java.nio.charset.CharsetEncoder

class CharsetConverter {
  companion object {

    fun create(string: String): String {
      return CharsetConverter().convertToUtf8(string)
    }
  }


  val encoder2: CharsetEncoder = Charset.forName("Windows-1252").newEncoder()
  val encoder3: CharsetEncoder = Charsets.UTF_8.newEncoder()

  fun convertToUtf8(windowscp: String): String {
    val conv1Bytes: ByteBuffer = encoder2.encode(CharBuffer.wrap(windowscp.toCharArray()))
    val retValue = String(conv1Bytes.array(), Charset.forName("Windows-1252"))
    val convertedBytes = encoder3.encode(CharBuffer.wrap(retValue.toCharArray()))
    val convertValue2 = String(convertedBytes.array(), Charsets.UTF_8)
    return convertValue2
  }
}