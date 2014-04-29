package com.github.scalazip

import org.specs2.mutable._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import java.io.File

@RunWith(classOf[JUnitRunner])
class ZipWriterTest extends Specification {

  "ZipWriterTest" should {

    "Compress a Single File" in {
      val myFile = new java.io.File("C:\\tmp\\livrotail.csv")
      val zip = myFile.zipAs("livrotail.zip")
      zip.name must_== "livrotail.zip"
    }

    "Compress Multiple Files" in {
      val file1 = new File("C:\\tmp\\download.jpg")
      val file2 = new File("C:\\tmp\\Tard2.jpg")
      val file3 = new File("C:\\tmp\\livrotail.csv")
      val files = file1 :: file2 :: file3 :: EmptyZip
      val zip = files.zipAtSource("stuff.zip")
      zip.name must_== "stuff.zip"
      files.entries.size must_== 3
    }

  }

}