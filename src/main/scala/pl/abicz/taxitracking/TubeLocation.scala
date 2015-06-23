package pl.abicz.taxitracking

import akka.actor.{ActorLogging, Actor}
import Gps._

object TubeLocation {

  case class CloseToTubeStation(loc: Location)

  case class CloseToTubeStationResponse(isClose: Boolean)

}

class TubeLocation extends Actor with ActorLogging {

  import TubeLocation._

  override def receive: Receive = {
    case CloseToTubeStation(loc) =>
      if (loc.latitude > 90 || loc.longitude > 90)
        sender ! CloseToTubeStationResponse(false)
      else
        sender ! CloseToTubeStationResponse(true)
    case _ => context.stop(self)
  }
}
