package pl.abicz.taxitracking

import java.time.Clock

import akka.actor.{Props, ActorSystem, Actor}
import akka.testkit.{TestProbe, ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}
import java.time.Instant
import scala.concurrent.duration._

class TaxiSpec extends TestKit(ActorSystem("TaxiSpec"))
with WordSpecLike with BeforeAndAfterAll with ImplicitSender {


  override def afterAll(): Unit = {
    system.shutdown()
  }

  def checkTaxi(closeToTube: Boolean) = {
      val taxiId  = s"mytaxiId$closeToTube"
      val loc = Gps.Location(0,0)
      val inst = Instant.now()
      val testManProble = TestProbe()
      val testGpsProbe = TestProbe()
      val testTubeProbe = TestProbe()
      val taxi = system.actorOf(Props(new Taxi(taxiId, testManProble.ref, testGpsProbe.ref, testTubeProbe.ref)), taxiId)

      testGpsProbe.expectMsg(Gps.CheckGps)
      taxi ! Gps.CheckGpsResponse(loc, inst)
      testTubeProbe.expectMsg(TubeLocation.CloseToTubeStation(loc))
      taxi ! TubeLocation.CloseToTubeStationResponse(closeToTube)
      if(closeToTube)
        testManProble.expectMsg(ManagementCenter.ReportLocation(taxiId, loc, inst))
      else
        testManProble.expectNoMsg(100 millis)
      taxi
  }

  "A Taxi " must {

    "return the right id and location and time when close to station" in {
      val taxi = checkTaxi(true)
      taxi ! "stop"
      watch(taxi)
      expectTerminated(taxi)
    }

    "return the right id and location and time when not close to station" in {
      val taxi = checkTaxi(false)
      taxi ! "stop"
      watch(taxi)
      expectTerminated(taxi)
    }
  }
}
