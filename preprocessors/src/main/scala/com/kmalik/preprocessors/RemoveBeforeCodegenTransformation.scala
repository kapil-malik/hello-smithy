package com.kmalik.preprocessors

import software.amazon.smithy.build.{ProjectionTransformer, TransformContext}
import software.amazon.smithy.model.Model
import software.amazon.smithy.model.shapes.ShapeId

class RemoveBeforeCodegenTransformation extends ProjectionTransformer {

  def getName() = {
    "RemoveBeforeCodegenTransformation"
  }

  def transform(ctx: TransformContext) : Model = {
    val toRemove = ctx
      .getModel()
      .getShapesWithTrait(ShapeId.from("com.kmalik.smithy.preprocessing#removeBeforeCodegen"))

    ctx.getTransformer().removeShapes(ctx.getModel(), toRemove)
  }
}
