package com.kmalik.smithy.preprocessing.tenantagnostic

import com.kmalik.smithy.preprocessing.MyObject

import scala.collection.mutable

class MultiTenantStore {

  private val _db = mutable.Map.empty[String, mutable.Map[String, MyObject]]

  def getTenantDb(tenantId: String): mutable.Map[String, MyObject] = {
    _db.getOrElseUpdate(tenantId, mutable.Map.empty)
  }
}
