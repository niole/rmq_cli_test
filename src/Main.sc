#!/usr/bin/env -S scala-cli -S 3
//> using dep com.rabbitmq:amqp-client:5.20.0
//> using dep org.slf4j:slf4j-api:2.0.11
//> using dep org.slf4j:slf4j-simple:2.0.11

//> using file ./Send.scala
//> using file ./Recv.scala

args.toList match {
  case Nil =>
    Recv()
  case msgs =>
    Send(msgs.mkString(" "))
}
