package com.github.scalazip

import java.io._
import java.util.zip._
import scala.io.Source
import org.specs2.mutable._
import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class ZipReaderTest extends Specification {

  def filterCsv(s: Stream[ZipEntry]) = s.filter(_.getName endsWith ".csv")

  def csvSplitter(line: String) = line.split(",") mkString "\t"

  val filename = "test-data/tmp.zip"
  def newCompressedFile = new CompressedFile(filename)

  "ZipReaderTest" should {

    "Unzip a zipped file" in {
      val zip = newCompressedFile
      val uncompressed = zip.unzipAs("test-data/livrotail-1")
      uncompressed.file.getName must_== "livrotail-1"
    }

    "Unzip at source a zipped file" in {
      val zip = newCompressedFile
      val uncompressed = zip.unzipAtSource("livrotail-2")
      uncompressed.file.getName must_== "livrotail-2"
    }

    "Unzip a zipped file" in {
      val zip = newCompressedFile
      val uncompressed = zip.unzipAtSource("tmp-test")
      uncompressed.file.getName must_== "tmp-test"
    }

    "Get all files inside a zip archive" in {
      val zip = newCompressedFile
      val allFiles = zip.getFiles
      allFiles.size must_== 6
    }

    "Find a csv file and read its lines" in {
      val zip = newCompressedFile
      val maybe = zip.find(e => e.getName endsWith ".csv")
      maybe match {
        case Some(lines) => lines.take(10) foreach println
        case None => println("No .csv file found")
      }
      maybe.isDefined must_== true
    }

    "Filter all csv files and read their lines" in {
      val zip = newCompressedFile
      val files = zip.filter(_.getName endsWith ".csv")
      val allLines = for {
        csv <- files
        line <- csv
      } yield {
        line mkString "\t"
      }
      allLines must not beEmpty
    }

    "Find a csv file return its InputStream" in {
      val found = ZipReader.find(new File(filename))(_.getName endsWith ".csv")
      found.map {
        is =>
          Source.fromInputStream(is).getLines.take(10) foreach println
      }
      found.isDefined must_== true
    }

    "Find a jpg in a ZipInputStream and write it to a local file" in {
      val zis = new ZipInputStream(new FileInputStream(new File(filename)))
      val newImage = new File("test-data", "test.jpg")
      val fos = new FileOutputStream(newImage)
      val found = ZipReader.find(zis)(_.getName endsWith ".jpg")
      found.map {
        is =>
          IOStream.stream(is, fos)
      }
      newImage.exists must_== true
    }

    "Find two files in the same Input Stream" in {
      val is: InputStream = new FileInputStream(new File(filename))
      val zis = new ZipInputStream(is)
      val found1 = ZipReader.find(zis)(_.getName == "LoremIpsum.txt")
      val found2 = ZipReader.find(zis)(_.getName == "LoremIpsum2.txt")
      println("TEXTO 1")
      found1.map {
        is =>
          Source.fromInputStream(is).getLines.take(10) foreach println
      }
      println("TEXTO 2")
      found2.map {
        is =>
          Source.fromInputStream(is).getLines.take(10) foreach println
      }

      zis.close()
      zis.getNextEntry must throwAn[IOException]
    }

  }

}
