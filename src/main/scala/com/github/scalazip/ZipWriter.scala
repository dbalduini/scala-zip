package com.github.scalazip

import scala.annotation.tailrec
import java.util.zip.{ ZipOutputStream, ZipEntry }
import java.io._

object ZipWriter extends ZipStream {

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
    CompressedFile(output)
  }

}