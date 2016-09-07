package models

import javax.inject.Inject
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import scala.concurrent.Future

/**
  * Created by Barni on 8/18/2016.
  */
case class Person(id: Long, name: String, email: String, password: String = "12345")

class PersonRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val db = dbConfig.db

  import dbConfig.driver.api._

  private val Persons = TableQuery[PersonTable]

  private def _findById(id: Long): DBIO[Option[Person]] =
    Persons.filter(_.id === id).result.headOption

  private def _findByName(name: String): Query[PersonTable, Person, List] =
    Persons.filter(_.name === name).to[List]

  def findById(id: Long): Future[Option[Person]] =
    db.run(_findById(id))

  def findByName(name: String): Future[List[Person]] =
    db.run(_findByName(name).result)

  def create(name: String, email: String) = {
    val person = Person(0, name, email)
    db.run(Persons returning Persons.map(_.id) += person)
  }

  private class PersonTable(tag: Tag) extends Table[Person](tag, "PERSON") {
    def id = column[Long]("ID", O.AutoInc, O.PrimaryKey)

    def name = column[String]("NAME")

    def email = column[String]("EMAIL")

    def password = column[String]("PASSWORD")

    def * = (id, name, email, password) <> (Person.tupled, Person.unapply)

    def ? = (id.?, name.?, email.?, password.?).shaped.<>({ r => import r._; _1.map(_ => Person.tupled((_1.get, _2.get, _3.get, _4.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

  }

}

