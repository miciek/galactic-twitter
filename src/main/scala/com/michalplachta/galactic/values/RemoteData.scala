package com.michalplachta.galactic.values

sealed trait RemoteData[A]

object RemoteData {
  final case class NotRequestedYet[A]() extends RemoteData[A]
  final case class Loading[A]() extends RemoteData[A]
  final case class Failed[A](errorMessage: String) extends RemoteData[A]
  final case class Fetched[A](data: A) extends RemoteData[A]
}
