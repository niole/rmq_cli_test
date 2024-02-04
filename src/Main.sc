#!/usr/bin/env -S scala-cli -S 3
//> using dep com.rabbitmq:amqp-client:5.20.0
//> using dep org.slf4j:slf4j-api:2.0.11
//> using dep org.slf4j:slf4j-simple:2.0.11

//> using file ./Send.scala
//> using file ./Recv.scala
//> using file ./Channel.scala

import scala.concurrent.{Future, ExecutionContext}

args.toList match {
  case Nil =>
    Recv()
  case List("2") =>
    given ExecutionContext = ExecutionContext.global
    Future {
      Recv("worker3")
    }
    Future {
      Recv("worker2")
    }
    Recv("worker1")
  case msgs =>
    msgs.foreach { msg =>
      Send(msg)
    }
}
