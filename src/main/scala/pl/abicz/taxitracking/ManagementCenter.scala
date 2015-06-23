package pl.abicz.taxitracking
import java.time.Instant

import akka.actor.{ActorLogging, Actor}
import akka.actor.Actor.Receive

object ManagementCenter  {
  case class ReportLocation(taxiId:String, loc: Gps.Location, inst : Instant)
  case class GetStorage()
}


class ManagementCenter extends Actor with ActorLogging {
  import ManagementCenter._
  var storage = Set.empty[ReportLocation]
  override def receive: Receive = {
    case  rep@ReportLocation(taxiId, loc, inst) => {
      log.info(s"$taxiId,${loc.latitude},${loc.longitude},${inst.getEpochSecond}")
      storage += rep
    }
    case GetStorage => sender ! storage

    case _ => context.stop(self)

  }
}
