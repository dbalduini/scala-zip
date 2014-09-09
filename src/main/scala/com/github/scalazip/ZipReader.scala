package com.github.scalazip
import java.util.zip.{ ZipEntry, ZipInputStream }
import java.io.FileInputStream
import scala.collection.immutable.Stream
import scala.io.Source
import java.io.File
import java.nio.file.Files
import java.io.FileOutputStream
import java.util.zip.ZipFile
import java.io.InputStream

object ZipReader {

  def uncompress(outputName: String, file: File): File = this.uncompress(outputName, new ZipInputStream(new FileInputStream(file)))

  def uncompress(outputName: String, zis: ZipInputStream): File = {
    val output = new File(outputName)
    Stream
      .continually(zis.getNextEntry)
      .takeWhile(_ != null)
      .foreach { entry =>
        val fileName = entry.getName
        val newFile = new File(output, fileName)
        new File(newFile.getParent).mkdir()
        if (! fileName.endsWith("/")) {
          val fos = new FileOutputStream(newFile)
          IOStream.stream(zis, fos)
          fos.close()
        }
      }
    zis.closeEntry()
    zis.close()
   output
  }

  def find(zis: ZipInputStream)(p: ZipEntry => Boolean): Option[InputStream] =
    Stream.continually(zis.getNextEntry).takeWhile(_ != null).find(p).map(_ => zis)

  def readLines(zis: ZipInputStream): Iterator[String] = {
    val entry = zis.getNextEntry
    Source.fromInputStream(zis).getLines
  }

}