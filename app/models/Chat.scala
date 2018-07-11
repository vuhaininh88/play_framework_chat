package models

import akka.actor._

// our domain message protocol
case object Join
case object Leave
final case class ClientSentMessage(text: String)

// Chat actor
class Chat extends Actor {
  // initial message-handling behavior
  def receive = process(Set.empty)

  def process(subscribers: Set[ActorRef]): Receive = {
    case Join =>
      // replaces message-handling behavior by the new one
      context become process(subscribers + sender)
      (subscribers).foreach { _ ! "join room" }

    case Leave =>
      context become process(subscribers - sender)
      (subscribers).foreach { _ ! "leave room" }

    case msg: ClientSentMessage =>
      // send messages to all subscribers except sender
      (subscribers - sender).foreach { _ ! msg }
  }
}
