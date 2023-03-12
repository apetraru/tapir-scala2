package demo.model

object Exceptions {

  case class CustomException(message: String, errorCode: Int = 404) extends RuntimeException(message: String)
}
