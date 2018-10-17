package no.hamre.polet.parser

import java.nio.{ByteBuffer, CharBuffer}
import java.nio.charset.{Charset, CharsetEncoder}

object CharsetConverter {

  def apply(string: String): String = {
    new CharsetConverter().convertToUtf8(string)
  }
}

class CharsetConverter {
  val encoder2: CharsetEncoder = Charset.forName("Windows-1252").newEncoder()
  val encoder3: CharsetEncoder = Charset.forName("UTF-8").newEncoder()

  def convertToUtf8(windowscp: String): String = {
    val conv1Bytes: ByteBuffer = encoder2.encode(CharBuffer.wrap(windowscp.toCharArray()))
    val retValue = new String(conv1Bytes.array(), Charset.forName("Windows-1252"))
    val convertedBytes = encoder3.encode(CharBuffer.wrap(retValue.toCharArray()))
    val convertValue2 = new String(convertedBytes.array(), Charset.forName("UTF-8"))
    convertValue2
  }
}