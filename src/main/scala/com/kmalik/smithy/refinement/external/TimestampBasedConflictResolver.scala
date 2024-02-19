package com.kmalik.smithy.refinement.external

import com.kmalik.smithy.refinement.MyObject

class TimestampBasedConflictResolver extends ConflictResolver {

  override def resolve(existing: MyObject, incoming: MyObject): Boolean =
  {
    existing.id == incoming.id && existing.ts.epochSecond <= incoming.ts.epochSecond
  }
}
