package pl.abicz.taxitracking

import akka.actor.{ActorLogging, Cancellable, ActorRef, Actor}
import java.util.concurrent.Executor
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import java.time.Instant

class Taxi(taxiId : String, managementCenter: ActorRef, gps: ActorRef, tubeLocation: ActorRef) extends Actor with ActorLogging {
  implicit val executor = context.dispatcher.asInstanceOf[Executor with ExecutionContext]
  val CheckGps = "checkGpsScheduled"
  val cancellable =
    context.system.scheduler.schedule(0 milliseconds,
      1 seconds,
      self,
      CheckGps)

  override def receive: Receive = checkGps

  val checkGps: Receive = {
    case CheckGps => {
      log.info(s"$taxiId CheckGps")
      gps ! Gps.CheckGps
      context.become(checkTubeLocation)
     }
    case _ => {
      log.info(s"$taxiId got message to stop ")
      context.stop(self)
    }
  }

  val checkTubeLocation : Receive = {
    case Gps.CheckGpsResponse(location, inst) =>
      log.info(s"$taxiId CheckResponse $location, $inst")
      tubeLocation ! TubeLocation.CloseToTubeStation(location)
      context.become(sendToManagementCenter(location, inst))
  }

  def sendToManagementCenter(loc : Gps.Location, inst : Instant) : Receive = {
    case TubeLocation.CloseToTubeStationResponse(isClose) => {
      log.info(s"$taxiId CloseToTubeStationResponse $isClose")
      if(isClose)
        managementCenter ! ManagementCenter.ReportLocation(taxiId, loc, inst)
      context.become(checkGps)
    }
  }
}
