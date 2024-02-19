package com.kmalik.smithy.refinement

import com.kmalik.smithy.refinement.external.{TimestampBasedConflictResolver, VersionBasedConflictResolver}
import smithy4s.Timestamp

object Main {

  def main(args: Array[String]): Unit = {

    val service = new MyServiceWithRefinementImpl()

    val versionResolver = Some(new VersionBasedConflictResolver())
    val tsResolver = Some(new TimestampBasedConflictResolver())

    val ts = Timestamp.nowUTC()
    val tsPlus1 = Timestamp.fromEpochSecond(ts.epochSecond + 1)
    val tsMinus1 = Timestamp.fromEpochSecond(ts.epochSecond - 1)

    // Writing object with id 1
    println(service.myWrite(MyWriteInput(MyObject("1", "Hello", 1, ts))))

    // VERSION BASED RESOLVER
    // Updating object with id 1 with version 2
    println(service.myWrite(MyWriteInput(MyObject("1", "Hello", 2, ts), versionResolver)))

    // Updating object with id 1 with version 1 again (should fail)
    println(service.myWrite(MyWriteInput(MyObject("1", "Hello", 1, ts), versionResolver)))

    // TIMESTAMP BASED RESOLVER
    // Updating object with id 1 with version 1 but later timestamp
    println(service.myWrite(MyWriteInput(MyObject("1", "Hello", 1, tsPlus1), tsResolver)))

    // Updating object with id 1 with older timestamp (should fail)
    println(service.myWrite(MyWriteInput(MyObject("1", "Hello", 3, tsMinus1), tsResolver)))

    // Reading object with id 1
    val readOutput1 =
    println(service.myRead(MyReadInput("1")))

    // Reading object with id 2 (should fail)
    println(service.myRead(MyReadInput("2")))
  }
}
