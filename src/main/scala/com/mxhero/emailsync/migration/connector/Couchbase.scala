package com.mxhero.emailsync.migration.connector

import com.mxhero.emailsync.migration._

import java.util.concurrent.TimeUnit

import com.couchbase.client.java.CouchbaseCluster
import com.couchbase.client.java.document.JsonDocument
import rx.Observable

import scala.collection.JavaConverters._

object Couchbase {

  private val couchbase = CouchbaseCluster.create(config.getString("couchbase.host"))
  val accBucket = couchbase.openBucket(config.getString("couchbase.bucket"), "", 3000, TimeUnit.SECONDS)

  def insert(docs: Iterable[JsonDocument]): JsonDocument =
    Observable.from(docs.asJava).flatMap(accBucket.async().insert(_)).last().toBlocking().single()

  def close = couchbase.disconnect()

}
