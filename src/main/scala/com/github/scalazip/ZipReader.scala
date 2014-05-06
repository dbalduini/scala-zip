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

object ZipReader extends ZipStream {

  def uncompress(outputName: String, file: File): UncompressedFile = this.uncompress(outputName, new ZipInputStream(new FileInputStream(file)))

  def uncompress(outputName: String, zis: ZipInputStream) = {
    val output = new File(outputName)
    Stream
      .continually(zis.getNextEntry)
      .takeWhile(_ != null)
      .foreach { entry =>
        val fileName = entry.getName
        val newFile = new File(output, fileName)
        new File(newFile.getParent).mkdir()
        val fos = new FileOutputStream(newFile)
        stream(zis, fos)
        fos.close()
      }
    zis.closeEntry()
    zis.close()
    new UncompressedFile(output)
  }

  def find(file: File)(p: ZipEntry => Boolean): Option[InputStream] = {
    val zf = new ZipFile(file)
    val e = zf.entries
    val maybeZentry = Stream.continually(e.nextElement).takeWhile(_ => e.hasMoreElements).find(p)
    maybeZentry.map(zf.getInputStream)
  }

  def readLines(zis: ZipInputStream): Iterator[String] = {
    val entry = zis.getNextEntry
    Source.fromInputStream(zis).getLines
  }

}