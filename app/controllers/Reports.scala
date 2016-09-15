package controllers

import javax.inject.Inject

import models.{PeerReviewRepo, PersonRepo, SelfReportRepo}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future

/**
  * Created by Barni on 8/20/2016.
  */
class Reports @Inject()(peerReviewRepo: PeerReviewRepo, selfReportRepo: SelfReportRepo, personRepo: PersonRepo) extends Controller {

  def fillSelfReport(reporter: Long) = Action { implicit rs =>
    Ok(views.html.selfreport_fill(reporter))
  }

  def createSelfReport(reporter: Long, mentor: Long, report: String) = Action.async { implicit rs =>
    selfReportRepo create(reporter: Long, mentor: Long, report: String) map (id => Ok(s"Self report with $id created"))
  }

  def listSelfReports(reporter: Long) = Action.async { implicit rs =>
    selfReportRepo findByReporter reporter map (selfReports => Ok(views.html.selfreports(selfReports)))
  }

  def listSelfReportsForMentor(mentor: Long) = Action.async { implicit rs =>
    selfReportRepo findByMentor mentor map (selfReports => Ok(views.html.selfreports(selfReports)))
  }

  def viewSelfReport(selfReport: Long) = Action.async { implicit rs =>
    selfReportRepo findById selfReport map (selfReport => Ok(views.html.selfreport_view(selfReport)))
  }

  def createPeerReview(reportee: Long, report: String) = Action.async { implicit rs =>
    peerReviewRepo create(reportee: Long, report: String) map (id => Ok(s"Self report with $id created"))
  }

  def listPeerReviews(reportee: Long) = Action.async { implicit rs =>
    peerReviewRepo findByReportee reportee map (review => Ok(views.html.peerreviews(review)))
  }

  def viewPeerReview(peerReview: Long) = Action.async { implicit rs =>
    peerReviewRepo findById peerReview map (review => Ok(views.html.peerreview_view(review)))
  }

  def fillPeerReview(reportee: Long) = Action.async { implicit rs =>
    personRepo findById reportee map {
      case Some(person) => Ok(views.html.peerreview_fill(person))
      case None => Redirect(routes.Application.index())
    }
  }

  def savePeerReview(reportee: Long) = Action.async { implicit rs =>
    rs.body.asText.fold(Future(Redirect(routes.Application.index())))(peerReviewRepo create(reportee, _) map (id => Redirect(routes.Reports.viewPeerReview(id))))
  }
}
