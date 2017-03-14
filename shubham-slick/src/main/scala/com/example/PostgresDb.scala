package com.example
import slick.jdbc.PostgresProfile.api._

trait PostgresDb extends DbData{
   override val driver=slick.jdbc.PostgresProfile
   import driver.api._
   override val db: Database = Database.forConfig("myPostgresDB")
}
