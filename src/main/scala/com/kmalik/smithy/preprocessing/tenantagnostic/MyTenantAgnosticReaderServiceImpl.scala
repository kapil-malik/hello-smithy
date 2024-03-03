package com.kmalik.smithy.preprocessing.tenantagnostic

import com.kmalik.smithy.preprocessing.{MyObject, MyTenantAgnosticReaderService, TenantsConfig}

import scala.collection.mutable
import scala.util.Try

class MyTenantAgnosticReaderServiceImpl(tenantId: String, tenantDb: mutable.Map[String, MyObject]) extends MyTenantAgnosticReaderService[Try] {

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
}
