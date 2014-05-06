package com.github.scalazip

import java.io.File
import java.io.FileInputStream
import java.util.zip.ZipInputStream
import java.util.zip.ZipEntry

abstract class Zippable {
  def file: File

  def zipAs(name: String): CompressedFile

  def zipAtSource(name: String) = zipAs(new File(file.getParentFile, name).getAbsolutePath)

}

trait Appendable {
  def ::(f: File): Appendable
}

case class ZipArchive(file: File, tail: Array[File]) extends Zippable with Appendable {
  val entries = file +: tail

  def ::(file: File) = new ZipArchive(file, entries)

  def zipAs(name: String) = ZipWriter.compress(name, entries: _*)
}

case object EmptyZip extends Appendable {

  def ::(file: File) = new UncompressedFile(file)

}

case class UncompressedFile(file: File) extends Zippable with Appendable {

  def zipAs(name: String) = ZipWriter.compress(name, file)

  def ::(file: File) = new ZipArchive(file, Array(this.file))

}



