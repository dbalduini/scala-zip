package com.github.scalazip

import java.io.{ File, FileInputStream }
import java.util.zip.{ ZipEntry, ZipInputStream }

import scala.io.Source

case class CompressedFile(file: File) {

  val name = file.getName

  type ZipPred = ZipEntry => Boolean

  def this(name: String) = this(new File(name))

  def zipInputStream = new ZipInputStream(new FileInputStream(file))

  def unzipAs(name: String): File = ZipReader.uncompress(name, file)
  
  def unzipAtSource(name: String) = unzipAs(new File(file.getParentFile, name).getAbsolutePath)

  private def iteratee(zis: ZipInputStream): Stream[ZipEntry] = Stream.continually(zis.getNextEntry).takeWhile(_ != null)

  private def mapReduce[T, E](map: Stream[ZipEntry] => T, reduce: T => E)(zis: ZipInputStream) = reduce(map(iteratee(zis)))

  def getFiles = filter(_ => true)

  def find(p: ZipPred) = {
    val zis = zipInputStream
    mapReduce[Option[ZipEntry], Option[Iterator[String]]](_.find(p), _.map(entry => Source.fromInputStream(zis).getLines))(zis)
  }

  def filter(p: ZipPred) = {
    val zis = zipInputStream
    mapReduce[Stream[ZipEntry], Stream[Iterator[String]]](_.filter(p), _.map(entry => Source.fromInputStream(zis).getLines))(zis)
  }

}


