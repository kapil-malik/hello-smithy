package com.kmalik.smithy.refinement.external

import com.kmalik.smithy.refinement.MyObject

class VersionBasedConflictResolver extends ConflictResolver {

  override def resolve(existing: MyObject, incoming: MyObject): Boolean =
  {
    existing.id == incoming.id && existing.version <= incoming.version
  }
}
