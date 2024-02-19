package com.kmalik.smithy.service

import cats.effect.IO
import com.kmalik.smithy.common.Types.{FutureTry, IOEither}

import scala.concurrent.Future
import scala.util.Try

// Try based service definition
trait MyServiceTry extends MyService[Try] {
  def myRead(input: MyReadInput): Try[MyReadOutput]
  def myWrite(input: MyWriteInput): Try[MyWriteOutput]
}

// Future based service definition
trait MyServiceFuture extends MyService[Future] {
  def myRead(input: MyReadInput): Future[MyReadOutput]
  def myWrite(input: MyWriteInput): Future[MyWriteOutput]
}

// Future[Try] based service definition
trait MyServiceFutureTry extends MyService[FutureTry] {
  def myRead(input: MyReadInput): Future[Try[MyReadOutput]]
  def myWrite(input: MyWriteInput): Future[Try[MyWriteOutput]]
}

// IO based service definition
trait MyServiceIO extends MyService[IO] {
  def myRead(input: MyReadInput): IO[MyReadOutput]
  def myWrite(input: MyWriteInput): IO[MyWriteOutput]
}

// IO[Either[Throwable, A]] based service definition
trait MyServiceIOEither extends MyService[IOEither] {
  def myRead(input: MyReadInput): IO[Either[Throwable, MyReadOutput]]
  def myWrite(input: MyWriteInput): IO[Either[Throwable, MyWriteOutput]]
}
