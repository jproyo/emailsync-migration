package com.mxhero.emailsync.migration.connector

import com.mxhero.emailsync.migration._

import scala.collection.JavaConverters._
import com.amazonaws.services.dynamodbv2.document.{DynamoDB, Item}
import com.amazonaws.services.dynamodbv2.document.spec.{QuerySpec, ScanSpec}
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClient

object Dynamo {

  private val dynamoDB = new DynamoDB(AmazonDynamoDBAsyncClient.asyncBuilder().build())
  val tasksTable = dynamoDB.getTable(s"${config.getString("dynamodb.leadingTable")}EmailSync");
  val accountsTable = dynamoDB.getTable(s"${config.getString("dynamodb.leadingTable")}EmailSyncAccount");


  val tasksAccounts = for {
    task <- getAllTasks
    account <- accountForTask(task)
  } yield (task, account)


  def tasks = tasksAccounts.map( pair => pair._1 ).toList.distinct

  def accounts = tasksAccounts.map( pair => pair._2 )

  def getAllTasks = {
    val scanSpec = new ScanSpec()
    scanSpec.setMaxPageSize(1000)
    scanSpec.setMaxResultSize(100000)
    tasksTable.scan(scanSpec).asScala
  }

  def accountForTask(task: Item) = {
    val querySpec = new QuerySpec()
    querySpec.setMaxPageSize(1000)
    querySpec.setMaxResultSize(100000)
    querySpec.withHashKey("domainTaskId", s"${task.get("domain")}-${task.get("taskId")}")
    accountsTable.query(querySpec).asScala
  }



}
