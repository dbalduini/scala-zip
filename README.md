scala-zip
=======================

## Example

```scala
  import com.github.scalazip._
```

## Writing Zips

### Compressing a single File
```scala
val myFile = new java.io.File("image.jpg")
val zip = myFile.zipAs("image.zip")
```

### Creating a ZipArchive
```scala
val file1 = new java.io.File("github1.jpg")
val file2 = new java.io.File("github2.jpg")
val file3 = new java.io.File("github3.jpg")
val files = file1 :: file2 :: file3 :: EmptyZip
```
#### Or

```scala
val files = ZipArchive(file1, file2, file3)
```

#### To compress the ZipArchive, you can choose between zipAs or zipAtSource
```scala
# To Zip where you are running the JVM
val zip = myFile.zipAs("images.zip")

# To Zip at the source of the original head file
val zip = files.zipAtSource("images.zip")
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
* Ajust the Zip Reader
