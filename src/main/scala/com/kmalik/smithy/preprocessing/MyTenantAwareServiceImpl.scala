package com.kmalik.smithy.preprocessing

import scala.util.Try

class MyTenantAwareServiceImpl extends MyTenantAwareService[Try] {

  private val _db = scala.collection.mutable.Map.empty[String, MyObject]

  override def myRead(input: MyReadInput): Try[MyReadOutput] = {
    Try {
      if (!TenantsConfig.isReadAllowed(input.tenantContext.tenantId)) {
        throw new RuntimeException(s"Read not allowed for tenant ${input.tenantContext.tenantId}")
      }
      val obj = _db.getOrElse(input.id,
        throw new RuntimeException(s"Object with id ${input.id} not found"))
      MyReadOutput(obj)
    }
  }

  override def myWrite(input: MyWriteInput): Try[MyWriteOutput] = {
    Try {
      if (!TenantsConfig.isWriteAllowed(input.tenantContext.tenantId, input.tenantDetails)) {
        throw new RuntimeException(s"Write not allowed for tenant ${input.tenantContext.tenantId}")
      }
      _db.put(input.obj.id, input.obj)
      MyWriteOutput(true)
    }
  }

  override def myDelete(input: MyDeleteInput): Try[Unit] = {
    Try {
      if (!TenantsConfig.isDeleteAllowed(input.tenantContext.tenantId)) {
        throw new RuntimeException(s"Delete not allowed for tenant ${input.tenantContext.tenantId}")
      }
      _db.remove(input.id)
    }
  }
}
