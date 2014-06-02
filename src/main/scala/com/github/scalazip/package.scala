package com.github

import java.io.File
package object scalazip {

  implicit def fromFile2ZipArchive(f: File) = f :: EmptyZip
  
  implicit def fromFile2CompressedFile(f: File) = new CompressedFile(f)
  
  implicit def fromCompressedToFile(c: CompressedFile) = new File(c.name)
  
}