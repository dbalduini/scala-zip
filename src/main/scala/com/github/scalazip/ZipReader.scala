package com.github.scalazip

import java.util.zip.{ ZipEntry, ZipInputStream }
import java.io.FileInputStream
import scala.collection.immutable.Stream
import scala.io.Source

class ZipReader(zis: ZipInputStream) {

  type Consumer = Stream[ZipEntry]

  lazy val iteratee: Stream[ZipEntry] = Stream.continually(zis.getNextEntry).takeWhile(_ != null)
  
  def stream[T](consumer: Consumer => Consumer, producer: Iterator[String] => Iterator[T]): Stream[Iterator[T]] =
    consumer(iteratee.takeWhile(_ != null)).map {
      entry => producer(Source.fromInputStream(zis).getLines)
    }

  def getLines: Iterator[String] = {
    val entry = zis.getNextEntry
    Source.fromInputStream(zis).getLines
  }

}
