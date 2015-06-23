package pl.abicz.taxitracking

import akka.actor.{ActorLogging, Actor}
import java.time.{Clock, Instant}
import scala.util.Random

object Gps {

  case class Location(latitude: Double, longitude: Double)

  case class CheckGps()

  case class CheckGpsResponse(loc: Location, inst : Instant)

}

class Gps(clock : Clock) extends Actor {

  import Gps._

  val rand = new Random

  override def receive: Receive = {
    case CheckGps => sender ! CheckGpsResponse(Location(rand.nextDouble() * 90, rand.nextDouble() * 360 - 180), clock.instant())
    case _ => context.stop(self)
  }
}
