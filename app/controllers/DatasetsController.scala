package controllers

import actorsmessages.{Command, CreateDatasetCommand, DatasetResponse, DeleteDatasetCommand, GetAllDatasetsCommand, GetDatasetByIdCommand, UpdateDatasetCommand}

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import org.apache.pekko.actor.typed.{ActorRef, ActorSystem, Scheduler}
import org.apache.pekko.actor.typed.scaladsl.AskPattern._
import org.apache.pekko.util.Timeout
import play.api.libs.json.Json.JsValueWrapper

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import utils.Validator

@Singleton
class DatasetsController @Inject() (
                                     cc: ControllerComponents,
                                     @Named("datasets-actor") datasetsActor: ActorRef[Command],
                                     actorSystem: ActorSystem[Nothing],
                                     validator: Validator
                                   ) extends AbstractController(cc) {

  implicit val timeout: Timeout = 5.seconds
  implicit val scheduler: Scheduler = actorSystem.scheduler
  implicit val ec: ExecutionContext = cc.executionContext


  implicit val mapStringAnyWrites: Writes[Map[String, Any]] = new Writes[Map[String, Any]] {
    def writes(map: Map[String, Any]): JsValue = {

      Json.obj(
        map.map { case (k, v) =>
          (k, JsString(v.toString)): (String, JsValueWrapper)
        }.toSeq: _*
      )
    }
  }

  def getAllDatasets: Action[AnyContent] = Action.async {
    datasetsActor.ask(GetAllDatasetsCommand.apply).map {
      case dr: DatasetResponse =>
        Ok(Json.toJson(dr.response))
      case _ =>
        InternalServerError("Unexpected response type")
    }
  }

  def getDatasetById(id: String): Action[AnyContent] = Action.async {
    datasetsActor.ask(ref => GetDatasetByIdCommand(id, ref)).map {
      case dr: DatasetResponse =>
        val statusCode = dr.response.get("status").collect { case i: Int => i }.getOrElse(500)
        Status(statusCode)(Json.toJson(dr.response))
      case _ =>
        InternalServerError("Unexpected response type")
    }
  }

  def createDataset: Action[AnyContent] = Action.async { request =>
    val json = request.body.asJson.getOrElse(Json.obj())
    datasetsActor.ask(ref => CreateDatasetCommand(Json.stringify(json), ref)).map {
      case dr: DatasetResponse =>
        val statusCode = dr.response.get("status").collect { case i: Int => i }.getOrElse(500)
        Status(statusCode)(Json.toJson(dr.response))
      case _ =>
        InternalServerError("Unexpected response type")
    }
  }

  def updateDataset(id: String): Action[AnyContent] = Action.async { request =>
    val json = request.body.asJson.getOrElse(Json.obj())
    datasetsActor.ask(ref => UpdateDatasetCommand(id, Json.stringify(json), ref)).map {
      case dr: DatasetResponse =>
        val statusCode = dr.response.get("status").collect { case i: Int => i }.getOrElse(500)
        Status(statusCode)(Json.toJson(dr.response))
      case _ =>
        InternalServerError("Unexpected response type")
    }
  }

  def deleteDataset(id: String): Action[AnyContent] = Action.async {
    datasetsActor.ask(ref => DeleteDatasetCommand(id, ref)).map {
      case dr: DatasetResponse =>
        val statusCode = dr.response.get("status").collect { case i: Int => i }.getOrElse(500)
        Status(statusCode)(Json.toJson(dr.response))
      case _ =>
        InternalServerError("Unexpected response type")
    }
  }
}


