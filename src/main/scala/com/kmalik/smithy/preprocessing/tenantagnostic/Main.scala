package com.kmalik.smithy.preprocessing.tenantagnostic

import com.kmalik.smithy.preprocessing.MyObject
import com.kmalik.smithy.preprocessing.TenantsConfig.{isReadAllowed, isWriteAllowed, listTenants}

object Main {

  def main(args: Array[String]): Unit = {

    val store = new MultiTenantStore()

    listTenants().foreach { tenantId =>
      println(s"Tenant: $tenantId")
      println(s"Read allowed: ${isReadAllowed(tenantId)}")
      println(s"Write allowed: ${isWriteAllowed(tenantId, None)}")
      println()

      val rwService = new MyTenantAgnosticServiceImpl(tenantId, store.getTenantDb(tenantId))
      val rService = new MyTenantAgnosticReaderServiceImpl(tenantId, store.getTenantDb(tenantId))
      if (isWriteAllowed(tenantId, None)) {
        val writeOutput1 = rwService.myWrite(MyWriteInput(MyObject("1", "data_1")))
        println(writeOutput1)

        val readOutput1RWService = rwService.myRead(MyReadInput("1"))
        println(readOutput1RWService)

        val readOutput1RService = rService.myRead(MyReadInput("1"))
        println(readOutput1RService)

        // reading object with id 2, should fail
        val readOutput2RWService = rwService.myRead(MyReadInput("2"))
        println(readOutput2RWService)

        val readOutput2RService = rService.myRead(MyReadInput("2"))
        println(readOutput2RService)
      } else {
        println(s"Write not allowed for tenant ${tenantId}")
      }
    }

  }
}
