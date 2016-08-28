package controllers

import javax.inject.Inject

import models.{PeerReviewRepo, SelfReportRepo}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.{Action, Controller}

/**
  * Created by Barni on 8/20/2016.
  */
class Reports @Inject()(peerReviewRepo: PeerReviewRepo, selfReportRepo: SelfReportRepo) extends Controller {

  def fillSelfReport(staffMember: Long) = Action { implicit rs =>
    Ok(views.html.selfreport_fill(staffMember))
  }

  def createSelfReport(staffMember: Long, mentor: Long, report: String) = Action.async { implicit rs =>
    selfReportRepo create(staffMember: Long, mentor: Long, report: String) map (id => Ok(s"Self report with $id created"))
  }

  def listSelfReports(staffMember: Long) = Action.async { implicit rs =>
    selfReportRepo findByStaffMember staffMember map (selfReports => Ok(views.html.selfreports(selfReports)))
  }

  def listSelfReportsForMentor(mentor: Long) = Action.async { implicit rs =>
    selfReportRepo findByMentor mentor map (selfReports => Ok(views.html.selfreports(selfReports)))
  }

  def viewSelfReport(staffMember: Long, selfReport: Long) = Action.async { implicit rs =>
    selfReportRepo findById selfReport map (selfReport => Ok(views.html.selfreport_view(selfReport)))
  }

  def createPeerReview(staffMember: Long, mentor: Long, report: String) = Action.async { implicit rs =>
    peerReviewRepo create(staffMember: Long, report: String) map (id => Ok(s"Self report with $id created"))
  }

  def listPeerReviews(staffMember: Long) = Action.async { implicit rs =>
    peerReviewRepo findByStaffMember staffMember map (review => Ok(views.html.peerreviews(review)))
  }

  def viewPeerReview(staffMember: Long, peerReview: Long) = Action.async { implicit rs =>
    peerReviewRepo findById peerReview map (review => Ok(views.html.peerreview_view(review)))
  }

  def fillPeerReview(staffMember: Long) = Action { implicit  rs =>
    Ok(views.html.peerreview_fill(staffMember))
  }
}
