package com.github

package object scalazip {

  implicit def fromFileToScalaZip(f: java.io.File) = new UncompressedFile(f)
  
}