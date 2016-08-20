package models

import javax.inject.Inject

import org.joda.time.DateTime
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import scala.concurrent.Future

/**
  * Created by Barni on 8/20/2016.
  */
case class PeerReview(id: Long, staffMember: Long, report: String, fillDate: DateTime) extends Report

class PeerReviewRepo @Inject()(taskRepo: StaffMemberRepo)(protected val dbConfigProvider: DatabaseConfigProvider) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val db = dbConfig.db

  import dbConfig.driver.api._
  import com.github.tototoshi.slick.H2JodaSupport._

  private val PeerReviews = TableQuery[PeerReview]

  private def _findById(id: Long): DBIO[Option[PeerReview]] =
    PeerReviews.filter(_.id === id).result.headOption

  private def _findByStaffMember(id: Long): Query[PeerReviewTable, PeerReview, List] =
    PeerReviews.filter(_.staffMember === id).sortBy(_.fillDate.desc).to[List]


  def findById(id: Long): Future[Option[PeerReview]] =
    db.run(_findById(id))

  def findByStaffMember(id: Long): Future[List[PeerReview]] =
    db.run(_findByStaffMember(id).result)


  private class PeerReviewTable(tag: Tag) extends Table[PeerReview](tag, "PEER_REVIEW") {
    def id = column[Long]("ID", O.AutoInc, O.PrimaryKey)

    def staffMember = column[Long]("STAFF_MEMBER")

    def report = column[String]("REPORT")

    def fillDate = column[DateTime]("FILL_DATE")

    def * = (id, staffMember, report, fillDate) <> (PeerReview.tupled, PeerReview.unapply)

    def ? = (id.?, staffMember.?, report.?, fillDate.?).shaped.<>({ r => import r._; _1.map(_ => PeerReview.tupled((_1.get, _2.get, _3.get, _4.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))
  }

}
