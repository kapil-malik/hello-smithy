package com.kmalik.smithy.preprocessing

import scala.util.Try

class MyServiceWithPreprocessingImpl extends MyServiceWithPreprocessing[Try] {

  private val _db = scala.collection.mutable.Map.empty[String, MyObject]

  override def myRead(input: MyReadInput): Try[MyReadOutput] = {
    Try {
      val obj = _db.getOrElse(input.id,
        throw new RuntimeException(s"Object with id ${input.id} not found"))
      MyReadOutput(obj)
    }
  }

  override def myWrite(input: MyWriteInput): Try[MyWriteOutput] = {
    Try {
      _db.put(input.obj.id, input.obj)
      MyWriteOutput(true)
    }
  }
}
