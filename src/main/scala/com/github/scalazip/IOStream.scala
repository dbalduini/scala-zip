package com.github.scalazip

import scala.annotation.tailrec
import java.io.OutputStream
import java.io.InputStream
import java.io.ByteArrayOutputStream
import java.io.ByteArrayInputStream

object IOStream {

  def stream(is: InputStream, os: OutputStream, bufferSize: Int = 4096) = {
    val buffer = new Array[Byte](bufferSize)
    @tailrec
    def doStream(total: Int): Int = {
      val n = is.read(buffer)
      if (n == -1)
        total
      else {
        os.write(buffer, 0, n)
        doStream(total + n)
      }
    }
    doStream(0)
  }

  def copy(is: InputStream, bufferSize: Int = 4096): InputStream = {
    val baos = new ByteArrayOutputStream
    val buffer = new Array[Byte](bufferSize)
    stream(is, baos)
    new ByteArrayInputStream(baos.toByteArray())
  }

}