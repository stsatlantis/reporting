package models

import javax.inject.Inject

import org.joda.time.DateTime
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import scala.concurrent.Future

/**
  * Created by Barni on 9/6/2016.
  */
case class Feedback(id: Long, reporter: Long, reportee: Long, report: String, fillDate: DateTime) extends Report


class FeedbackRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val db = dbConfig.db

  import com.github.tototoshi.slick.H2JodaSupport._
  import dbConfig.driver.api._

  private val Feedbacks = TableQuery[FeedbackTable]

  private def _findById(id: Long): DBIO[Option[Feedback]] =
    Feedbacks.filter(_.id === id).result.headOption

  private def _findByReporter(id: Long): Query[FeedbackTable, Feedback, List] =
    Feedbacks.filter(_.reporter === id).sortBy(_.fillDate.desc).to[List]

  private def _findByReportee(id: Long): Query[FeedbackTable, Feedback, List] =
    Feedbacks.filter(_.mentor === id).sortBy(_.fillDate.desc).to[List]

  def findById(id: Long): Future[Option[Feedback]] =
    db.run(_findById(id))

  def findByReporter(id: Long): Future[List[Feedback]] =
    db.run(_findByReporter(id).result)

  def findByReportee(id: Long): Future[List[Feedback]] =
    db.run(_findByReportee(id).result)

  def create(reporter: Long, reportee: Long, report: String) = {
    val selfReport = Feedback(0, reporter, reportee, report, DateTime.now())
    db.run(Feedbacks returning Feedbacks.map(_.id) += selfReport)
  }

  private class FeedbackTable(tag: Tag) extends Table[Feedback](tag, "FEEDBACK") {
    def id = column[Long]("ID", O.AutoInc, O.PrimaryKey)

    def reporter = column[Long]("REPORTER")

    def mentor = column[Long]("REPORTEE")

    def report = column[String]("REPORT")

    def fillDate = column[DateTime]("FILL_DATE")

    def * = (id, reporter, mentor, report, fillDate) <> (Feedback.tupled, Feedback.unapply)

    def ? = (id.?, reporter.?, mentor.?, report.?, fillDate.?).shaped.<>({ r => import r._; _1.map(_ => Feedback.tupled((_1.get, _2.get, _3.get, _4.get, _5.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))
  }

}


