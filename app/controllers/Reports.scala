package controllers

import javax.inject.Inject

import models.{PeerReviewRepo, SelfReportRepo}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.{Action, Controller}

/**
  * Created by Barni on 8/20/2016.
  */
class Reports @Inject()(peerReviewRepo: PeerReviewRepo, selfReportRepo: SelfReportRepo) extends Controller {

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

  def fillPeerReview = Action { implicit rs =>
    Ok(views.html.peerreview_fill())
  }
}
