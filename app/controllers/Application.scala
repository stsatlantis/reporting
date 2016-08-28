package controllers

import javax.inject.Inject

import play.api.mvc.{Action, Controller}

class Application @Inject()() extends Controller {
  def index = Action { implicit rs =>
    Ok(views.html.index())
  }
}
