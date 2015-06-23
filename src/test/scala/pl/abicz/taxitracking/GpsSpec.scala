package pl.abicz.taxitracking

import java.time.Clock

import akka.actor.{Props, ActorSystem, Actor}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}

class GpsSpec extends TestKit(ActorSystem("GpsSpec"))
with WordSpecLike with BeforeAndAfterAll with ImplicitSender {

  import Gps._

  override def afterAll(): Unit = {
    system.shutdown()
  }

  "A Gps " must {

    "return the right location" in {
      val gps = system.actorOf(Props(new Gps(Clock.systemUTC())), "Gps")
      gps ! CheckGps

      expectMsgPF() {
        case CheckGpsResponse(location, inst)
          if (location.latitude >= 0 && location.latitude <= 90 &&
            location.longitude >= -180 && location.longitude <= 180) =>
          true
      }

      gps ! "stop"
      watch(gps)
      expectTerminated(gps)
    }
  }
}
