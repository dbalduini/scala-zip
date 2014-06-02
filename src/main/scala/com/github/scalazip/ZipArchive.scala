package com.github.scalazip

import java.io.File
import scala.annotation.tailrec

abstract class ZipArchive {
  def isEmpty: Boolean
  def head: File
  def tail: ZipArchive
  def incl(file: File): ZipArchive
  def size: Int

  def zipAs(name: String): File = {
    @tailrec
    def loop(z: ZipArchive, acc: List[File]): List[File] = z match {
      case EmptyZip => acc
      case NonEmptyZip(head, tail) => loop(tail, head :: acc)
    }
    ZipWriter.compress(name, loop(this, Nil): _*)
  }

  def zipAtSource(name: String) = this match {
    case EmptyZip => throw new Exception("Nothing to Zip")
    case NonEmptyZip(head, _) => zipAs(new File(head.getParentFile, name).getAbsolutePath)
  }

  def ::(file: File): ZipArchive = this incl file
}

case object EmptyZip extends ZipArchive {
  def isEmpty = true
  def size = 0
  def head = throw new NoSuchElementException("EmptyZip.head")
  def tail = throw new NoSuchElementException("EmptyZip.tail")
  def incl(file: File): ZipArchive = new NonEmptyZip(file, EmptyZip)
  override def toString = "."
}

case class NonEmptyZip(head: File, tail: ZipArchive) extends ZipArchive {
  def isEmpty = false
  def size = 1 + tail.size
  def incl(file: File): ZipArchive = new NonEmptyZip(head, tail incl file)
  override def toString = head.getName + ", " + tail.toString
}

object ZipArchive {
  def apply(files: File*) = {
    @tailrec
    def loop(xs: List[File], acc: ZipArchive): ZipArchive = xs match {
      case Nil => acc
      case head :: tail => loop(tail, acc incl head)
    }
    loop(files.toList, EmptyZip)
  }
}


