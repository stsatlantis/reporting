package models

import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import scala.concurrent.Future

/**
  * Created by Barni on 8/18/2016.
  */
case class StaffMember(id: Long, name: String, email: String, password: String)

class StaffMemberRepo(protected val dbConfigProvider: DatabaseConfigProvider) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val db = dbConfig.db

  import dbConfig.driver.api._

  private val StaffMembers = TableQuery[StaffMemberTable]

  private def _findById(id: Long): DBIO[Option[StaffMember]] =
    StaffMembers.filter(_.id === id).result.headOption

  private def _findByName(name: String): Query[StaffMemberTable, StaffMember, List] =
    StaffMembers.filter(_.name === name).to[List]

  def findById(id: Long): Future[Option[StaffMember]] =
    db.run(_findById(id))

  def findByName(name: String): Future[List[StaffMember]] =
    db.run(_findByName(name).result)

  private class StaffMemberTable(tag: Tag) extends Table[StaffMember](tag, "STAFF_MEMBER") {
    def id = column[Long]("ID", O.AutoInc, O.PrimaryKey)

    def name = column[String]("NAME")

    def email = column[String]("EMAIL")

    def password = column[String]("PASSWORD")

    def * = (id, name, email, password) <>(StaffMember.tupled, Project.unapply)

    def ? = (id.?, name.?, email.?, password.?).shaped.<>({ r => import r._; _1.map(_ => StaffMember.tupled((_1.get, _2.get, _3.get, _4.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

  }

}

