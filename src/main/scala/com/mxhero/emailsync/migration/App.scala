package com.mxhero.emailsync.migration

import com.amazonaws.services.dynamodbv2.document.Item
import com.couchbase.client.java.document.JsonDocument
import com.couchbase.client.java.document.json.JsonObject
import com.mxhero.emailsync.migration.connector.Couchbase._
import com.mxhero.emailsync.migration.connector.Dynamo._

object App {
  val messageUsage =
    """
      |Error calling command App
      |To call this command please run the following
      |cmd> command
    """.stripMargin

  val schema = "mxhero:emailsync:task"
  val schemaAccount = "mxhero:emailsync:task:account"

  def toCouchbaseItem: String => Item => Item = schemaType => item => {
    item.withString("schemaEntity", item.getString("domain"))
    item.withString("schemaType", schemaType)
    item.removeAttribute("domain")
    item.removeAttribute("domainStatus")
    item.removeAttribute("domainTaskId")
    item.removeAttribute("domainTaskIdStatus")
    item.removeAttribute("domainTaskIdStatusError")
    item
  }

  def toCouchbaseJsonDocument: (Item => String) => Item => JsonDocument = keyFn => item  =>
    JsonDocument.create(keyFn(item), JsonObject.fromJson(item.toJSON))

  def main(args: Array[String]): Unit = {
    val taskDocuments = tasks.map(toCouchbaseItem(schema)).map(toCouchbaseJsonDocument( item => s"${item.get("schemaType")}:${item.get("schemaEntity")}:${item.get("taskId")}"))
    val accountDocuments = accounts.map(toCouchbaseItem(schemaAccount)).map(toCouchbaseJsonDocument( item => s"${item.get("schemaType")}:${item.get("schemaEntity")}:${item.get("taskId")}:${item.get("account")}"))
    val allDocuments = taskDocuments ++ accountDocuments
    println(s"Documents to Insert ${allDocuments.size}")
    insert(allDocuments)
  }
}
