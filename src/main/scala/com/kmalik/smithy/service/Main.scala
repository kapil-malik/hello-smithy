package com.kmalik.smithy.service

object Main {

  def main(args: Array[String]): Unit = {

    val service = new MyServiceTryImpl()

    // Writing object with id 1
    val writeOutput1 = service.myWrite(MyWriteInput(MyObject("1", "data_1")))
    println(writeOutput1)

    // Reading object with id 1
    val readOutput1 = service.myRead(MyReadInput("1"))
    println(readOutput1)

    // Reading object with id 2 (should fail)
    val readOutput2 = service.myRead(MyReadInput("2"))
    println(readOutput2)
  }
}
