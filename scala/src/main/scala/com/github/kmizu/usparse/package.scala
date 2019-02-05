package com.github.kmizu

package object usparse {
  case class ~[+A, +B](a: A, b: B)
  sealed trait Result[+A] { def next: String }
  case class Success[+A](value: A, next: String) extends Result[A]
  case class Failure(next: String) extends Result[Nothing]
  type Parser[A] = String => Result[A]
  type P[A] = Parser[A]

  implicit class RichParser[A](val self: Parser[A]) {
    def * : Parser[List[A]] = {input: String =>
      def repeat(input: String): Result[List[A]] = self(input) match {
        case Success(value, next1) =>
          repeat(next1) match {
            case Success(result, next2) =>
              Success(value::result, next2)
            case r => sys.error("cannot reach here")
          }
        case Failure(next) =>
          Success(Nil, next)
      }
      repeat(input) match {
        case r@Success(_, _) => r
        case r@Failure(_) => r
      }
    }

    def ~[B](right: Parser[B]) : Parser[A ~ B] = {input =>
      self(input) match {
        case Success(value1, next1) =>
          right(next1) match {
            case Success(value2, next2) =>
              Success(new ~(value1, value2), next2)
            case failure@Failure(_) =>
              failure
          }
        case failure@Failure(_) =>
          failure
      }
    }

    def ? : Parser[Option[A]] = {input =>
      self(input) match {
        case Success(v, next) => Success(Some(v), next)
        case Failure(next) => Success(None, next)
      }
    }

    def |[B >: A](rhs: Parser[B]): Parser[B] = {input =>
      self(input) match {
        case success@Success(_, _) => success
        case Failure(_) => rhs(input)
      }
    }

    /**
      * Returns a parser that the result is converted from <code>T</code> to <code>U</code>
      * @param function the result converter
      */
    def map[B](function: A => B): Parser[B] = {input =>
      self(input) match {
        case Success(value, next) => Success(function(value), next)
        case failure@Failure(_) => failure
      }
    }
  }

  final def $(literal: String): Parser[String] = {input =>
    if(literal.length > 0 && input.length == 0) {
      Failure("")
    } else if(input.startsWith(literal)) {
      Success(literal, input.substring(literal.length))
    } else {
      Failure(input)
    }
  }

  final def rule[A](body: => Parser[A]): Parser[A] = body(_)
}
