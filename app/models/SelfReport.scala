package models

import javax.inject.Inject

import org.joda.time.DateTime
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import scala.concurrent.Future

/**
  * Created by Barni on 8/18/2016.
  */
case class SelfReport(id: Long, staffMember: Long, mentor: Long, report: String, fillDate: DateTime) extends Report

class SelfReportRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val db = dbConfig.db

  import com.github.tototoshi.slick.H2JodaSupport._
  import dbConfig.driver.api._

  private val SelfReports = TableQuery[SelfReportTable]

  private def _findById(id: Long): DBIO[Option[SelfReport]] =
    SelfReports.filter(_.id === id).result.headOption

  private def _findByStaffMember(id: Long): Query[SelfReportTable, SelfReport, List] =
    SelfReports.filter(_.staffMember === id).sortBy(_.fillDate.desc).to[List]

  private def _findByMentor(id: Long): Query[SelfReportTable, SelfReport, List] =
    SelfReports.filter(_.mentor === id).sortBy(_.fillDate.desc).to[List]

  def findById(id: Long): Future[Option[SelfReport]] =
    db.run(_findById(id))

  def findByStaffMember(id: Long): Future[List[SelfReport]] =
    db.run(_findByStaffMember(id).result)

  def findByMentor(id: Long): Future[List[SelfReport]] =
    db.run(_findByMentor(id).result)

  def create(staffMember: Long, mentor: Long, report: String) = {
    val selfReport = SelfReport(0, staffMember, mentor, report, DateTime.now())
    db.run(SelfReports returning SelfReports.map(_.id) += selfReport)
  }

  private class SelfReportTable(tag: Tag) extends Table[SelfReport](tag, "SELF_REPORT") {
    def id = column[Long]("ID", O.AutoInc, O.PrimaryKey)

    def staffMember = column[Long]("STAFF_MEMBER")

    def mentor = column[Long]("MENTOR")

    def report = column[String]("REPORT")

    def fillDate = column[DateTime]("FILL_DATE")

    def * = (id, staffMember, mentor, report, fillDate) <> (SelfReport.tupled, SelfReport.unapply)

    def ? = (id.?, staffMember.?, mentor.?, report.?, fillDate.?).shaped.<>({ r => import r._; _1.map(_ => SelfReport.tupled((_1.get, _2.get, _3.get, _4.get, _5.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))
  }

}
