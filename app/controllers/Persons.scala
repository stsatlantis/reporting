package controllers

import javax.inject.Inject

import models.PersonRepo
import play.api.mvc.{Action, Controller}
import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
  * Created by Barni on 8/27/2016.
  */
class Persons @Inject()(personRepo: PersonRepo) extends Controller {
  def addPerson(name: String, email: String) = Action.async { implicit rs =>
    personRepo.create(name, email).map(id => Ok(s"Staff member with $id created"))
  }

}
