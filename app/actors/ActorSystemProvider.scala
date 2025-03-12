package actors

package actors


import actorsmessages.Command
import play.inject.ApplicationLifecycle
import services.DatasetsService
import org.apache.pekko.actor.typed.{ActorRef, ActorSystem}
import org.apache.pekko.actor.typed.scaladsl.Behaviors

import scala.concurrent.Future
import javax.inject._

@Singleton
class ActorSystemProvider @Inject()(lifecycle: ApplicationLifecycle, datasetsService: DatasetsService) {


  val actorSystem: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "datasets-actor-system")


  val datasetsActor: ActorRef[Command] =
    actorSystem.systemActorOf(DatasetsActor(datasetsService), "datasets-actor")



}

