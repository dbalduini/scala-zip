package com.github.scalazip

import org.specs2.mutable._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import scala.collection.immutable.Stream
import java.util.zip.ZipEntry
import java.io.File
import java.util.zip.ZipInputStream
import java.io.FileInputStream
import scala.io.Source

@RunWith(classOf[JUnitRunner])
class ZipReaderTest extends Specification {

  def filterCsv(s: Stream[ZipEntry]) = s.filter(_.getName endsWith ".csv")

  def csvSplitter(line: String) = line.split(",") mkString "\t"

  "ZipReaderTest" should {
    "Unzip a zipped file" in {
      val zip = new CompressedFile("C:\\tmp\\livrotail.zip")
      val uncompressed = zip.unzipAs("C:\\tmp\\livrotail-1")
      uncompressed.file.getName must_== "livrotail-1"
    }

    "Unzip at source a zipped file" in {
      val zip = new CompressedFile("C:\\tmp\\livrotail.zip")
      val uncompressed = zip.unzipAtSource("livrotail-2")
      uncompressed.file.getName must_== "livrotail-2"
    }

    "Unzip a zipped file" in {
      val zip = new CompressedFile("C:\\tmp\\tmp.zip")
      val uncompressed = zip.unzipAtSource("tmp-test")
      uncompressed.file.getName must_== "tmp-test"
    }

    "Find a csv file and read its lines" in {
      val zip = new CompressedFile("C:\\tmp\\tmp.zip")
      val maybeCsv = zip.find(_.getName endsWith ".csv") { lines =>
        //Drop the header and split the csv by `,`
        lines.drop(1).map(_.split(","))
      }
      maybeCsv match {
        case Some(lines) => lines.take(10).map(_ mkString "\t") foreach println
        case None => println("No .csv file found")
      }
      maybeCsv.isDefined must_== true
    }

    "Filter all csv files and read their lines" in {
      val zip = new CompressedFile("C:\\tmp\\tmp.zip")
      val files = zip.filter(_.getName endsWith ".csv") { lines =>
        //Drop the header and split the csv by `,`
        lines.drop(1).map(_.split(","))
      }
      val allLines = for {
        csv <- files
        line <- csv
      } yield {
        line mkString "\t"
      }
      allLines must not beEmpty
    }

    "Find a csv file return its InputStream" in {
      val found = ZipReader.find(new File("C:\\tmp\\tmp.zip"))(_.getName endsWith ".csv")
      found.map {
        is =>
          Source.fromInputStream(is).getLines.take(10) foreach println
      }
      found.isDefined must_== true
    }

    "Find a csv file in a ZipInputStream and return its InputStream" in {
      val zis = new ZipInputStream(new FileInputStream(new File("C:\\tmp\\tmp.zip")))
      val found = ZipReader.find(zis)(_.getName endsWith ".csv")
      found.map {
        is =>
          Source.fromInputStream(is).getLines.take(10) foreach println
      }
      found.isDefined must_== true
    }

  }

}
