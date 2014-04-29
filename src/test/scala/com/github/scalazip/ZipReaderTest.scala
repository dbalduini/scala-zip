package com.github.scalazip

import org.specs2.mutable._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import scala.collection.immutable.Stream
import java.util.zip.ZipEntry

@RunWith(classOf[JUnitRunner])
class ZipReaderTest extends Specification {

  def filterCsv(s: Stream[ZipEntry]) = s.filter(_.getName endsWith ".csv")

  "ZipReaderTest" should {

    "Read all lines of a zipped file" in {
      val reader = new CompressedFile("C:\\tmp\\livrotail.zip").read
      val lines = reader.getLines.take(10)
      lines foreach println
      lines.isEmpty must beTrue
      reader.iteratee.isEmpty must beTrue
    }

    "Read all files inside the Zip" in {
      val myZipFile = new CompressedFile("C:\\tmp\\tmp.zip").read
      myZipFile.iteratee foreach println
      myZipFile.iteratee.isEmpty must beFalse
    }

    "Filter only CSV Files when reading" in {
      val myZipFile = new CompressedFile("C:\\tmp\\tmp.zip").read
      val filter = myZipFile.iteratee.filter(_.getName endsWith ".csv")
      filter foreach println
      filter.size must_== 1
    }

    "Read CSV contents" in {
      val myZipFile = new CompressedFile("C:\\tmp\\tmp.zip").read
      val stream = myZipFile.stream(
        consumer =>
          // I WANT ONLY CSV FILES INSIDE THE ZIP
          consumer.filter(_.getName endsWith ".csv"),
        producer =>
          //DROP EACH CSV FILE HEADER AND SPLIT
          producer.drop(1).map(_.split(",").mkString("\t")))

      //      stream.foreach {
      //        csv => csv.foreach {
      //          line =>
      //            println(line)
      //        }
      //      }

      stream.isEmpty must beFalse
    }

  }

}
