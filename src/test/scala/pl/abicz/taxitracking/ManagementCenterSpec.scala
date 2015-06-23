package pl.abicz.taxitracking

import akka.actor.{Props, ActorSystem}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}
import java.time.Instant

class ManagementCenterSpec extends TestKit(ActorSystem("ManagementCenterSpec"))
with WordSpecLike with BeforeAndAfterAll with ImplicitSender {

  override def afterAll(): Unit = {
    system.shutdown()
  }

  "A ManagementCenter " must {

    "store taxi locations" in {

      val mnCent = system.actorOf(Props[ManagementCenter], "MnCent")
      val loc = ManagementCenter.ReportLocation("tx1", Gps.Location(0.0, 0.0), Instant.EPOCH)
      mnCent ! loc
      mnCent ! ManagementCenter.GetStorage
      expectMsg(Set(loc))
      mnCent ! "stop"
      watch(mnCent)
      expectTerminated(mnCent)
    }
  }
}
