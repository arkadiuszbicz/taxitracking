package pl.abicz.taxitracking

import java.time.Clock

import akka.actor.{ActorSystem, ReceiveTimeout, Props, Actor}
import scala.concurrent.duration._

class Main extends Actor {

  val managementCenter = context.actorOf(Props[ManagementCenter], "ManagementCenter")
  context.watch(managementCenter) // sign death pact
  val gps = context.actorOf(Props(new Gps(Clock.systemUTC())), "Gps")
  val tubeLoc = context.actorOf(Props[TubeLocation], "TubeLocation")

  val taxi1 = context.actorOf(Props(new Taxi("taxi1", managementCenter, gps, tubeLoc)), "taxi1")
  val taxi2 = context.actorOf(Props(new Taxi("taxi2", managementCenter, gps, tubeLoc)), "taxi2")
  val taxi3 = context.actorOf(Props(new Taxi("taxi3", managementCenter, gps, tubeLoc)), "taxi3")

  context.setReceiveTimeout(10.seconds)

  override def receive = {
    case ReceiveTimeout => {
      context.system.shutdown()
    }
  }
}


object Main extends App {

  val system = ActorSystem("TaxiTrackingSystem")
  system.actorOf(Props[Main])

}
