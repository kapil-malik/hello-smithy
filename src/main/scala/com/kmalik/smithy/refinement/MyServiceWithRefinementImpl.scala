package com.kmalik.smithy.refinement

import scala.util.Try

class MyServiceWithRefinementImpl extends MyServiceWithRefinement[Try] {

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
      val inputObj = input.obj
      val id = inputObj.id
      val conflictResolverOpt = input.conflictResolver

      if (_db.contains(id) &&
        !conflictResolverOpt.map(_.resolve(_db.get(id).get, inputObj)).getOrElse(true)) {
        throw new RuntimeException(s"Conflict resolver ${conflictResolverOpt.get.name} unsatisfied for id: $id")
      }
      _db.put(id, inputObj)
      MyWriteOutput(true)
    }
  }
}
