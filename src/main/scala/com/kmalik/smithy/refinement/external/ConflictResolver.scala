package com.kmalik.smithy.refinement.external

import com.kmalik.smithy.refinement.{ConflictResolverTag, MyObject}
import smithy4s.{Refinement, RefinementProvider}

trait ConflictResolver {
  def resolve(existingObj: MyObject, newObj: MyObject): Boolean

  def name: String = this.getClass.getSimpleName
}

object ConflictResolver {

  def apply(value: String): Either[String, ConflictResolver] =
    Right((existingObj: MyObject, newObj: MyObject) => {
      print(s"ConflictResolver: $value")
      existingObj.id == newObj.id
    })

  implicit val provider: RefinementProvider[ConflictResolverTag, String, ConflictResolver] =
    Refinement.drivenBy[ConflictResolverTag](
      ConflictResolver.apply,
      (e: ConflictResolver) => e.toString
    )
}
