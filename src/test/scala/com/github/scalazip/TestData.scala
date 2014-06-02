package com.github.scalazip

import scala.io.Source

trait TestData {

  type Closeable = { def close(): Unit }

  def getResourceName(filename: String) = s"./src/test/resources/$filename"

  def getResource(filename: String) = new java.io.File(getResourceName(filename))

  def closeResources(r: Closeable*) = r foreach (_.close())

}