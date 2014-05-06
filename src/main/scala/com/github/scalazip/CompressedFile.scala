package com.github.scalazip

import java.util.zip.ZipInputStream
import java.util.zip.ZipEntry
import java.io.File
import scala.io.Source
import java.io.FileInputStream

trait Unzippable {

  def file: File

  def unzipAs(name: String): UncompressedFile

  def unzipAtSource(name: String) = unzipAs(new File(file.getParentFile, name).getAbsolutePath)

}

case class CompressedFile(file: File) extends Unzippable {

  val name = file.getName

  type StreamPred = ZipEntry => Boolean

  def this(name: String) = this(new File(name))

  def zipInputStream = new ZipInputStream(new FileInputStream(file))

  def unzipAs(name: String) = ZipReader.uncompress(name, file)

  private def iteratee(zis: ZipInputStream) = Stream.continually(zis.getNextEntry).takeWhile(_ != null)

  def find[T](pred: StreamPred)(lines: Iterator[String] => T) = {
    val zis = zipInputStream
    iteratee(zis).find(pred).map(_ => lines(Source.fromInputStream(zis).getLines))
  }

  def filter[T](pred: StreamPred)(lines: Iterator[String] => T) = {
    val zis = zipInputStream
    iteratee(zis).filter(pred).map(_ => lines(Source.fromInputStream(zis).getLines))
  }

}
