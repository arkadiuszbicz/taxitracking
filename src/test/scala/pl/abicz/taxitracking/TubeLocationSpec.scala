package pl.abicz.taxitracking

import akka.actor.{Props, ActorSystem, Actor}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}

class TubeLocationSpec extends TestKit(ActorSystem("TubeLocationSpec"))
with WordSpecLike with BeforeAndAfterAll with ImplicitSender {

  import TubeLocation._

  override def afterAll(): Unit = {
    system.shutdown()
  }

  "A TubeLocation" must {

    "return the right close to Tube Station" in {
      val tubeLoc = system.actorOf(Props[TubeLocation], "TubeLocation")
      tubeLoc ! CloseToTubeStation(new Gps.Location(0.0, 0.0))
      expectMsg(CloseToTubeStationResponse(true))
      tubeLoc ! "stop"
      watch(tubeLoc)
      expectTerminated(tubeLoc)
    }

    "properly finish in case of errors" in {
      val tubeLoc = system.actorOf(Props[TubeLocation], "TubeLocation")
      tubeLoc ! CloseToTubeStation(new Gps.Location(100.0, 100.0))
      expectMsg(CloseToTubeStationResponse(false))
      tubeLoc ! "stop"
      watch(tubeLoc)
      expectTerminated(tubeLoc)
    }
  }
}
