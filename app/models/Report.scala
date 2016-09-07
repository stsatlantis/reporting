package models

import org.joda.time.DateTime

/**
  * Created by Barni on 8/20/2016.
  */

trait Report {
  def report: String

  def fillDate: DateTime
}
