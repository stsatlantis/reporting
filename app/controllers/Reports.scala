package controllers

import javax.inject.Inject

import models.{PeerReviewRepo, SelfReportRepo}
import play.api.mvc.Controller

/**
  * Created by Barni on 8/20/2016.
  */
class Reports @Inject()(peerReviewRepo: PeerReviewRepo, selfReportRepo: SelfReportRepo) extends Controller {
  def mySelfReports(staffMember: Long) = {
    selfReportRepo.
  }
}
