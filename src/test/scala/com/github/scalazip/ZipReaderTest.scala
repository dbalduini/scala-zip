package com.github.scalazip

import java.io._
import java.util.zip._
import scala.io.Source
import org.specs2.mutable._
import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class ZipReaderTest extends Specification with TestData {

  def filterCsv(s: Stream[ZipEntry]) = s.filter(_.getName endsWith ".csv")

  def csvSplitter(line: String) = line.split(",") mkString "\t"

  "ZipReaderTest" should {

    "Unzip a zipped file" in {
      val zip = new CompressedFile(getResourceName("csv/data.zip"))
      val file = zip.unzipAs("data")
      file.getName must_== "data"
    }

    "Unzip at source a zipped file" in {
      val zip = new CompressedFile(getResourceName("csv/data.zip"))
      val file = zip.unzipAtSource("data")
      file.getName must_== "data"
    }

    "Get all files inside a zip archive" in {
      val zip = new CompressedFile(getResource("txt/lorem.zip"))
      val allFiles = zip.getFiles
      allFiles.size must_== 2
    }

    "Find a csv file and read its lines" in {
      val zip = new CompressedFile(getResourceName("csv/data.zip"))
      val maybe = zip.find(e => e.getName endsWith ".csv")
      maybe match {
        case Some(lines) => lines.take(10) foreach println
        case None => println("No .csv file found")
      }
      maybe.isDefined must_== true
    }

    "Filter all txt files and read their lines" in {
      val zip = new CompressedFile(getResourceName("txt/lorem.zip"))
      val files = zip.filter(_.getName endsWith ".txt")
      val allLines = for {
        csv <- files
        line <- csv
      } yield {
        line mkString "\t"
      }
      allLines must not beEmpty
    }

    "Find a jpg in a ZipInputStream and write it to a local file" in {
      val zis = new ZipInputStream(new FileInputStream(getResource("img/images.zip")))
      val newImage = getResource("output.jpg")
      val fos = new FileOutputStream(newImage)
      val found = ZipReader.find(zis)(_.getName endsWith ".jpg")
      found.map {
        is =>
          IOStream.stream(is, fos)
          closeResources(is, fos, zis)
      }
      newImage.exists must_== true
    }
  }

}
