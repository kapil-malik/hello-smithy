package com.kmalik.smithy.preprocessing

object TenantsConfig {

  private val READ = "read"
  private val WRITE = "write"
  private val DELETE = "delete"

  val TENANT_PERMISSIONS_MAP = Map(
    "tenant1" -> Seq(READ, WRITE, DELETE),
    "tenant2" -> Seq(READ, WRITE),
    "tenant3" -> Seq(READ)
  )

  def listTenants(): Seq[String] = {
    TENANT_PERMISSIONS_MAP.keys.toSeq
  }

  def isReadAllowed(tenantId: String): Boolean = {
    TENANT_PERMISSIONS_MAP.getOrElse(tenantId, Seq.empty).contains(READ)
  }

  def isWriteAllowed(tenantId: String, tenantDetails: Option[MyTenantDetails] = None): Boolean = {
    tenantDetails.exists(t => t.isProduction && t.tier == TENANT_TIER.PREMIUM) ||
      TENANT_PERMISSIONS_MAP.getOrElse(tenantId, Seq.empty).contains(WRITE)
  }

  def isDeleteAllowed(tenantId: String): Boolean = {
    TENANT_PERMISSIONS_MAP.getOrElse(tenantId, Seq.empty).contains(DELETE)
  }

}
