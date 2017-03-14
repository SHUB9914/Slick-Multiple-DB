package com.example

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

case class Employee(id: Int, name: String, expr: Double)

trait EmployeeTable {

  val employeeTableQuery = TableQuery[EmployeeTable]

  class EmployeeTable(tag: Tag) extends Table[Employee](tag, "employee") {
    val id = column[Int]("id", O.PrimaryKey)
    val name = column[String]("name")
    val experience = column[Double]("expr")

    def * = (id, name, experience) <>(Employee.tupled, Employee.unapply)
  }

}

trait EmployeeRepo extends EmployeeTable {
  this: DbData =>

  import driver.api._

  //val db=this.config
  def create: Future[Unit] = db.run(employeeTableQuery.schema.create)

  def insert(emp: Employee): Future[Int] = db.run {
    employeeTableQuery += emp
  }

  def delete(emp: Double): Future[Int] = {
    val query = employeeTableQuery.filter(x => x.experience === 4.0)
    val action = query.delete
    db.run(action)
  }

  def updateName(id: Int, name: String): Future[Int] = {
    val query = employeeTableQuery.filter(_.id === id).map(_.name).update(name)
    db.run(query)
  }
  def find(id: Int) =
    db.run((for (emp <- employeeTableQuery if emp.id === id) yield emp).result.headOption)
  def upsert(emp: Employee): String ={
    val search=find(emp.id)
    search.map(x=> x match {
      case Some(i) => updateName(i.id, i.name)
      case _ => insert(emp)
    }
    )
    "success"
  }
}

object EmployeeRepo extends EmployeeRepo with SqlDb