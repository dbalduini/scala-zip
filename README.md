scala-zip
=======================

## Example

```scala
  import com.github.scalazip._
```

### Compressing a File
```scala
  val myFile = new java.io.File("image.jpg")
  val zip = myFile.zipAs("image.zip")
```

### Uncompressing the file
```scala
  val zip = new CompressedFile("image.zip")
  val uncompressed = zip.unzipAtSource("images")
```

### Reading Zip Files

```scala
  val zip = new CompressedFile("datasource.zip")
  val maybeFound = zip.find(e => e.getName endsWith ".txt")
  maybeFound match {
    case Some(lines) => lines.take(10) foreach println
    case None => println("No .txt file found")
  }
```

### All Togheter
```scala
val file1 = new File("image1.jpg")
val file2 = new File("image2.jpg")
val file3 = new File("atextfile.txt")

val files = file1 :: file2 :: file3 :: EmptyZip

val compressed = files.zipAs("stuff.zip")
val uncompressed = compressed.unzipAs("stuff")
```

================
### TODO
* Revise all the tests.
