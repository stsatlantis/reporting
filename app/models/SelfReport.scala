package models

import javax.inject.Inject

import org.joda.time.DateTime
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import scala.concurrent.Future

/**
  * Created by Barni on 8/18/2016.
  */
case class SelfReport(id: Long, reporter: Long, mentor: Long, report: String, fillDate: DateTime) extends Report

class SelfReportRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val db = dbConfig.db

  import com.github.tototoshi.slick.H2JodaSupport._
  import dbConfig.driver.api._

  private val SelfReports = TableQuery[SelfReportTable]

  private def _findById(id: Long): DBIO[Option[SelfReport]] =
    SelfReports.filter(_.id === id).result.headOption

  private def _findByReporter(id: Long): Query[SelfReportTable, SelfReport, List] =
    SelfReports.filter(_.reporter === id).sortBy(_.fillDate.desc).to[List]

  private def _findByMentor(id: Long): Query[SelfReportTable, SelfReport, List] =
    SelfReports.filter(_.mentor === id).sortBy(_.fillDate.desc).to[List]

  def findById(id: Long): Future[Option[SelfReport]] =
    db.run(_findById(id))

  def findByReporter(id: Long): Future[List[SelfReport]] =
    db.run(_findByReporter(id).result)

  def findByMentor(id: Long): Future[List[SelfReport]] =
    db.run(_findByMentor(id).result)

  def create(reporter: Long, mentor: Long, report: String) = {
    val selfReport = SelfReport(0, reporter, mentor, report, DateTime.now())
    db.run(SelfReports returning SelfReports.map(_.id) += selfReport)
  }

  private class SelfReportTable(tag: Tag) extends Table[SelfReport](tag, "SELF_REPORT") {
    def id = column[Long]("ID", O.AutoInc, O.PrimaryKey)

    def reporter = column[Long]("REPORTER")

    def mentor = column[Long]("MENTOR")

    def report = column[String]("REPORT")

    def fillDate = column[DateTime]("FILL_DATE")

    def * = (id, reporter, mentor, report, fillDate) <> (SelfReport.tupled, SelfReport.unapply)

    def ? = (id.?, reporter.?, mentor.?, report.?, fillDate.?).shaped.<>({ r => import r._; _1.map(_ => SelfReport.tupled((_1.get, _2.get, _3.get, _4.get, _5.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))
  }

}
