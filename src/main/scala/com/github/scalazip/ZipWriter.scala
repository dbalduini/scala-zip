package com.github.scalazip

import scala.annotation.tailrec
import java.util.zip.{ ZipOutputStream, ZipEntry }
import java.io._

object ZipWriter {

  private[this] final def stream(is: InputStream, os: OutputStream) = {
    val buffer = new Array[Byte](16384)
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

  def compress(filename: String, files: File*) = {
    val output = new File(filename)
    val fos = new FileOutputStream(output)
    val zos = new ZipOutputStream(new BufferedOutputStream(fos))
    files foreach {
      input =>
        val fis = new FileInputStream(input)
        val entry = new ZipEntry(input.getName)
        zos.putNextEntry(entry)
        stream(fis, zos)
        zos.flush()
        fis.close()
    }
    zos.close()
    fos.close()
    new CompressedFile(output)
  }

}