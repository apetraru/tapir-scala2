package demo

import cats.effect.IO
import demo.model.Exceptions.CustomException
import demo.model.Library.Book
import io.circe.generic.auto._
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._
import sttp.tapir.server.ServerEndpoint.Full

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.control.NonFatal
import scala.util.{Failure, Success, Try}

object Endpoints {
  private val tryEndpoint: Full[Unit, Unit, Unit, CustomException, Book, Any, IO] =
    endpoint.get
      .in("book-try")
      .out(jsonBody[Book])
      .errorOut(jsonBody[CustomException])
      .serverLogic(_ => {
        val result = Try(BookService.getBookThrowsException)
        match {
          case Failure(exception) => Left(CustomException(exception.getMessage))
          case Success(book) => Right(book)
        }
        IO(result)
      })

  private val eitherEndpoint: Full[Unit, Unit, Unit, CustomException, Book, Any, IO] =
    endpoint.get
      .in("book-either")
      .out(jsonBody[Book])
      .errorOut(jsonBody[CustomException])
      .serverLogic(_ => {
        IO(BookService.getBookEither)
      })

  private val ioEndpoint: Full[Unit, Unit, Unit, CustomException, Book, Any, IO] =
    endpoint.get
      .in("book-io")
      .out(jsonBody[Book])
      .errorOut(jsonBody[CustomException])
      .serverLogic(_ => {
        BookService.getBookIO.attempt.map {
          case Left(error) => Left(CustomException(error.getMessage))
          case Right(book) => Right(book)
        }
      })

  private val futureEndpoint: Full[Unit, Unit, Unit, CustomException, Book, Any, IO] = endpoint.get
    .in("book-future")
    .out(jsonBody[Book])
    .errorOut(jsonBody[CustomException])
    .serverLogic(_ => {
      IO.fromFuture(
        IO(
          BookService.getBookFuture
            .map(Right(_))
            .recover {
              case NonFatal(e) => Left(CustomException(e.getMessage))
            }
        )
      )
    })

  val all: List[Full[Unit, Unit, Unit, CustomException, Book, Any, IO]] = List(eitherEndpoint, tryEndpoint, ioEndpoint, futureEndpoint)

}



