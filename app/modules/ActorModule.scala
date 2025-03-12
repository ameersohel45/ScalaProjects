package modules


import actors.actors.ActorSystemProvider
import actorsmessages.Command
import com.google.inject.AbstractModule
import com.google.inject.Provides

import javax.inject.Singleton
import org.apache.pekko.actor.typed.{ActorRef, ActorSystem}

import javax.inject.Named

class ActorModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[ActorSystemProvider]).asEagerSingleton()
  }

  @Provides
  @Singleton
  def provideActorSystem(provider: ActorSystemProvider): ActorSystem[Nothing] =
    provider.actorSystem

  @Provides
  @Singleton
  @Named("datasets-actor")
  def provideDatasetsActor(provider: ActorSystemProvider): ActorRef[Command] =
    provider.datasetsActor
}
