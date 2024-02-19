package com.kmalik.smithy.common

import cats.effect.IO

import scala.concurrent.Future
import scala.util.Try

object Types {
  type FutureTry[A] = Future[Try[A]]

  type IOEither[A] = IO[Either[Throwable, A]]
}
