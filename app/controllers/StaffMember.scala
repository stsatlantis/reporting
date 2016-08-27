package controllers

import javax.inject.Inject

import models.StaffMemberRepo
import play.api.mvc.{Action, Controller}

/**
  * Created by Barni on 8/27/2016.
  */
class StaffMember @Inject()(staffMemberRepo: StaffMemberRepo) extends Controller {
  def addStaffMember(name: String, email: String) = Action.async {
    staffMemberRepo.create(name, email).map(id => Ok(s"Staff member with $id created"))
  }

}
