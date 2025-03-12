package actors


import actorsmessages.{Command, CreateDatasetCommand, DatasetResponse, DeleteDatasetCommand, GetAllDatasetsCommand, GetDatasetByIdCommand, UpdateDatasetCommand}
import org.apache.pekko.actor.typed.Behavior
import org.apache.pekko.actor.typed.scaladsl.Behaviors
import services.DatasetsService

import scala.jdk.CollectionConverters._

object DatasetsActor {
  def apply(datasetsService: DatasetsService): Behavior[Command] =
    Behaviors.receive { (context, message) =>
      message match {
        case cmd: GetAllDatasetsCommand =>
          val javaMap = datasetsService.getAllDatasets()
          val scalaMap = javaMap.asScala.toMap

          cmd.replyTo ! DatasetResponse(scalaMap)
          Behaviors.same

        case cmd: GetDatasetByIdCommand =>
          val javaMap = datasetsService.getDatasetById(cmd.id)
          val scalaMap = javaMap.asScala.toMap
          cmd.replyTo ! DatasetResponse(scalaMap)
          Behaviors.same

        case cmd: CreateDatasetCommand =>
          val javaMap = datasetsService.createDataset(cmd.jsonData)
          val scalaMap = javaMap.asScala.toMap
          cmd.replyTo ! DatasetResponse(scalaMap)
          Behaviors.same

        case cmd: UpdateDatasetCommand =>
          val javaMap = datasetsService.updateDataset(cmd.id, cmd.jsonData)
          val scalaMap = javaMap.asScala.toMap
          cmd.replyTo ! DatasetResponse(scalaMap)
          Behaviors.same

        case cmd: DeleteDatasetCommand =>
          val javaMap = datasetsService.deleteDataset(cmd.id)
          val scalaMap = javaMap.asScala.toMap
          cmd.replyTo ! DatasetResponse(scalaMap)
          Behaviors.same
      }
    }
}
