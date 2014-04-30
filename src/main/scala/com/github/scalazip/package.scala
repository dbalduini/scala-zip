package com.github

import java.io.File
package object scalazip {

  implicit def fromFileToUncompressed(f: File) = new UncompressedFile(f)
  
  implicit def fromCompressedToFile(c: CompressedFile) = new File(c.name)
  
}