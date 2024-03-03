package com.kmalik.smithy.preprocessing.tenantagnostic

import com.kmalik.smithy.preprocessing.{MyObject, MyTenantAgnosticService, TenantsConfig}

import scala.collection.mutable
import scala.util.Try

class MyTenantAgnosticServiceImpl(tenantId: String, tenantDb: mutable.Map[String, MyObject]) extends MyTenantAgnosticService[Try] {

  private val _db = tenantDb

  override def myRead(input: MyReadInput): Try[MyReadOutput] = {
    Try {
      if (!TenantsConfig.isReadAllowed(tenantId)) {
        throw new RuntimeException(s"Read not allowed for tenant ${tenantId}")
      }
      val obj = _db.getOrElse(input.id,
        throw new RuntimeException(s"Object with id ${input.id} not found"))
      MyReadOutput(obj)
    }
  }

  override def myWrite(input: MyWriteInput): Try[MyWriteOutput] = {
    Try {
      if (!TenantsConfig.isWriteAllowed(tenantId)) {
        throw new RuntimeException(s"Write not allowed for tenant ${tenantId}")
      }
      _db.put(input.obj.id, input.obj)
      MyWriteOutput(true)
    }
  }
}
