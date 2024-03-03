package com.kmalik.preprocessors

import software.amazon.smithy.build.{ProjectionTransformer, TransformContext}
import software.amazon.smithy.model.Model
import software.amazon.smithy.model.shapes.{MemberShape, Shape, ShapeId}

import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`

class TenantAgnosticTransformation extends ProjectionTransformer {

  private val TENANT_AGNOSTIC_TRAIT = ShapeId.from("com.kmalik.smithy.preprocessing#tenantAgnostic")
  private val IS_TENANT_AWARE_TRAIT = ShapeId.from("com.kmalik.smithy.preprocessing#isTenantAware")

  def getName() = {
    "TenantAgnosticTransformation"
  }

  private def generateTenantAgnosticId(id: ShapeId) = {
    ShapeId.from(id.toString.replace("#", ".tenantagnostic#"))
  }

  private def isTenantAware(ctx: TransformContext, member: MemberShape): Boolean = {
    val target = member.getTarget()
    val shape = ctx.getModel().getShape(target).get()
    member.hasTrait(IS_TENANT_AWARE_TRAIT) || shape.hasTrait(IS_TENANT_AWARE_TRAIT)
  }

  def transform(ctx: TransformContext) : Model = {
    val modelBuilder = ctx.getModel.toBuilder

    val tenantAgnosticTaggedServices = ctx.getModel()
                                          .getShapesWithTrait(TENANT_AGNOSTIC_TRAIT)
                                          .toSeq
                                          .filter(_.isServiceShape)
                                          .map(_.asServiceShape().get())

    val tenantAgnosticServicesMap = tenantAgnosticTaggedServices.flatMap(service => {
      println(s"service: $service")

      val ops = service.getAllOperations.toSeq
      val tenantOpsPairs = ops.flatMap(opId => {
        val op = ctx.getModel().getShape(opId).get().asOperationShape().get()
        println(s"op: $op")

        val inputId = op.getInputShape()
        val input = ctx.getModel().getShape(inputId).get().asStructureShape().get()
        val outputId = op.getOutputShape()
        val output = ctx.getModel().getShape(outputId).get().asStructureShape().get()

        val inputMembers = input.getAllMembers().values().toSeq

        val tenantAwareMembers = inputMembers.filter(m => isTenantAware(ctx, m))

        if (tenantAwareMembers.size > 0) {
          val tenantAgnosticInputShapeId = generateTenantAgnosticId(inputId)
          val tenantAgnosticInputBuilder = input.toBuilder().id(tenantAgnosticInputShapeId)
          tenantAwareMembers.foreach(contextMember => tenantAgnosticInputBuilder.removeMember(contextMember.getMemberName))
          val tenantAgnosticInput = tenantAgnosticInputBuilder.build()
          modelBuilder.addShape(tenantAgnosticInput)
          println(s"Added tenant agnostic input: $tenantAgnosticInput")

          val tenantAgnosticOutputShapeId = generateTenantAgnosticId(outputId)
          val tenantAgnosticOutput = output.toBuilder().id(tenantAgnosticOutputShapeId).build()
          modelBuilder.addShape(tenantAgnosticOutput)
          println(s"Added tenant agnostic output: $tenantAgnosticOutput")

          val tenantAgnosticOpId = generateTenantAgnosticId(opId)
          val tenantAgnosticOp = op.toBuilder().id(tenantAgnosticOpId).input(tenantAgnosticInputShapeId).output(tenantAgnosticOutputShapeId).build()
          modelBuilder.addShape(tenantAgnosticOp)
          println(s"Added tenant agnostic op: $tenantAgnosticOp")

          Seq((op, tenantAgnosticOp))
        } else {
          Seq()
        }
      })

      if (tenantOpsPairs.size > 0) {
        val tenantAgnosticServiceBuilder = service.toBuilder
        tenantOpsPairs.foreach(pair => {
          val originalOp = pair._1
          val newOp = pair._2
          tenantAgnosticServiceBuilder.removeOperation(originalOp.getId)
            .addOperation(newOp.getId)
        })
        val tenantAgnosticService = tenantAgnosticServiceBuilder.build()
        println(s"Updated tenant agnostic service: $tenantAgnosticService")
        Seq((service, tenantAgnosticService))
      } else {
        Seq()
      }
    }).toMap

    val newModel = modelBuilder.build()
    if (tenantAgnosticServicesMap.size > 0) {
      val fn = new java.util.function.Function[Shape, Shape]() {
        override def apply(shape: Shape): Shape = {
          if (shape.isServiceShape) {
            tenantAgnosticServicesMap.getOrElse(shape.asServiceShape().get(), shape)
          } else {
            shape
          }
        }
      }
      ctx.getTransformer().mapShapes(newModel, fn)
    } else {
      ctx.getModel
    }
  }
}
