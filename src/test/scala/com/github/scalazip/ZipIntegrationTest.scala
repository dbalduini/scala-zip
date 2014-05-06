package com.github.scalazip

import org.specs2.mutable._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import scala.collection.immutable.Stream
import java.util.zip.ZipEntry
import java.io.File

@RunWith(classOf[JUnitRunner])
class ZipIntegrationTest extends Specification {

  "ZipIntegrationTest" should {
    "Compress a File, and them uncompress it" in {
      val file1 = new File("C:\\tmp\\download.jpg")
      val file2 = new File("C:\\tmp\\Tard2.jpg")
      val files = file1 :: file2 :: EmptyZip
      val compressed = files.zipAs("stuff.zip")
      val uncompressed = compressed.unzipAs("stuff")
      uncompressed.file.exists must_== true
    }

  }

}
