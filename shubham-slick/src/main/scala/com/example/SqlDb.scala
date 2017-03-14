package com.example

import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

trait SqlDb extends DbData{

 override val driver=slick.jdbc.MySQLProfile
 import driver.api._
 override val db: Database = Database.forConfig("mysql")

}
