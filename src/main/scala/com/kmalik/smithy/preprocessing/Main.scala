package com.kmalik.smithy.preprocessing

import com.kmalik.smithy.preprocessing.TenantsConfig._

object Main {

  def main(args: Array[String]): Unit = {

    val service = new MyTenantAwareServiceImpl()
    listTenants().foreach { tenantId =>
      println(s"Tenant: $tenantId")
      println(s"Read allowed: ${isReadAllowed(tenantId)}")
      println(s"Write allowed: ${isWriteAllowed(tenantId, None)}")
      println(s"Delete allowed: ${isDeleteAllowed(tenantId)}")
      println()

      val tenantContext = MyTenantContext(tenantId)

      // Writing object with id 1
      val writeOutput1 = if (isWriteAllowed(tenantId, None)) {
        service.myWrite(MyWriteInput(tenantContext, MyObject("1", "data_1"), None))
      } else {
        service.myWrite(MyWriteInput(tenantContext, MyObject("1", "data_1"),
          Some(MyTenantDetails(true, TENANT_TIER.PREMIUM))))
      }
      println(writeOutput1)

      val readOutput1 = service.myRead(MyReadInput(tenantContext, "1"))
      println(readOutput1)

      if (isDeleteAllowed(tenantId)) {
        val deleteOutput1 = service.myDelete(MyDeleteInput(tenantContext, "1"))
        println(deleteOutput1)

        // read deleted object, should fail
        val readOutput2 = service.myRead(MyReadInput(tenantContext, "1"))
        println(readOutput2)
      }

    }
  }
}
