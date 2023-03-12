package demo

import cats.effect.IO
import demo.model.Exceptions.CustomException
import demo.model.Library.{Author, Book}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


object BookService {
  private var counter = 0
  private val SampleBook = Book("The Sorrows of Young Werther", 1774, Author("Johann Wolfgang von Goethe"))

  def getBookEither: Either[CustomException, Book] = {
    if (incrementAndIsEven()) {
      Right(SampleBook)
    }
    else {
      Left(CustomException("Either custom exception"))
    }
  }

  def getBookThrowsException: Book = {
    if (incrementAndIsEven()) {
      SampleBook
    }
    else {
      throw CustomException("Thrown custom exception")
    }
  }

  def getBookFuture: Future[Book] = {
    Future(getBookThrowsException)
  }

  def getBookIO: IO[Book] = {
    if (incrementAndIsEven()) {
      IO(SampleBook)
    }
    else {
      IO.raiseError(CustomException("IO Raise error"))
    }

  }

  private def incrementAndIsEven(): Boolean = {
    counter += 1
    counter % 2 == 0
  }
}
