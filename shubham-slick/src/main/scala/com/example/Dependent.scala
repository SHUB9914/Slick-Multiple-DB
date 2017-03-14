package com.example

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future


case class Dependent(id: Int, name: String, relation: String, age: Int)

trait DependentTable extends EmployeeTable{
  val dependentTableQuery = TableQuery[DependentTable]

  class DependentTable(tag: Tag) extends Table[Dependent](tag, "dependent") {
    val id = column[Int]("emp_id")
    val name = column[String]("name")
    val relation = column[String]("relation")
    val age = column[Int]("age")
    def dependentForeignKey_FK = foreignKey("emp_proj_fk", id, employeeTableQuery)(_.id)
    def * = (id, name, relation, age) <>(Dependent.tupled, Dependent.unapply)
  }

}

trait DependentRepo extends DependentTable {
  this: DbData =>

  import driver.api._

  //val db=this.config
  def create: Future[Unit] = db.run(dependentTableQuery.schema.create)

  def insert(dependent: Dependent): Future[Int] = db.run {
    dependentTableQuery += dependent
  }

  def delete(id: Int): Future[Int] = {
    val query = dependentTableQuery.filter(x => x.id === id)
    val action = query.delete
    db.run(action)
  }

  def updateName(id: Int, name: String): Future[Int] = {
    val query = dependentTableQuery.filter(_.id === id).map(_.name).update(name)
    db.run(query)
  }

}

object DependentRepo extends DependentRepo with SqlDb