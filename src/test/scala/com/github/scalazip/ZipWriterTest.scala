package com.github.scalazip

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ZipWriterTest extends Specification with TestData {

  "ZipWriterTest" should {

    "Compress a Single File with implicit conversion" in {
      val myFile = getResource("csv/data.csv")
      val zip = myFile.zipAs("data.zip")
      zip.delete must_== true
    }

    "Compress a Single File at Source" in {
      val myFile = getResource("csv/data.csv")
      val zip = myFile.zipAtSource("data.zip")
      zip.getName must_== "data.zip"
    }

    "Compress Multiple Files with cons" in {
      val file1 = getResource("img/github1.jpg")
      val file2 = getResource("img/github2.jpg")
      val file3 = getResource("img/github3.jpg")
      val files = file1 :: file2 :: file3 :: EmptyZip
      println(files)
      val zip = files.zipAtSource("images.zip")
      zip.getName must_== "images.zip"
      files.size must_== 3
    }

    "Compress Multiple Files with apply" in {
      val file1 = getResource("txt/lorem5.txt")
      val file2 = getResource("txt/lorem10.txt")
      val files = ZipArchive(file1, file2)
      println(files)
      val zip = files.zipAtSource("lorem.zip")
      zip.getName must_== "lorem.zip"
      files.size must_== 2
    }

  }

}