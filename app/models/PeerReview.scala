package models

import javax.inject.Inject

import org.joda.time.DateTime
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import scala.concurrent.Future

/**
  * Created by Barni on 8/20/2016.
  */
case class PeerReview(id: Long, reportee: Long, report: String, fillDate: DateTime) extends Report

class PeerReviewRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val db = dbConfig.db

  import com.github.tototoshi.slick.H2JodaSupport._
  import dbConfig.driver.api._

  private val PeerReviews = TableQuery[PeerReviewTable]

  private def _findById(id: Long): DBIO[Option[PeerReview]] =
    PeerReviews.filter(_.id === id).result.headOption

  private def _findByReportee(id: Long): Query[PeerReviewTable, PeerReview, List] =
    PeerReviews.filter(_.reportee === id).sortBy(_.fillDate.desc).to[List]


  def findById(id: Long): Future[Option[PeerReview]] =
    db.run(_findById(id))

  def findByReportee(id: Long): Future[List[PeerReview]] =
    db.run(_findByReportee(id).result)

  def create(reportee: Long, report: String): Future[Long] = {
    val peerReview = PeerReview(id = 0, reportee = reportee, report = report, fillDate = DateTime.now())
    db.run(PeerReviews returning PeerReviews.map(_.id) += peerReview)
  }

  private class PeerReviewTable(tag: Tag) extends Table[PeerReview](tag, "PEER_REVIEW") {
    def id = column[Long]("ID", O.AutoInc, O.PrimaryKey)

    def reportee = column[Long]("REPORTEE")

    def report = column[String]("REPORT")

    def fillDate = column[DateTime]("FILL_DATE")

    def * = (id, reportee, report, fillDate) <> (PeerReview.tupled, PeerReview.unapply)

    def ? = (id.?, reportee.?, report.?, fillDate.?).shaped.<>({ r => import r._; _1.map(_ => PeerReview.tupled((_1.get, _2.get, _3.get, _4.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))
  }

}
