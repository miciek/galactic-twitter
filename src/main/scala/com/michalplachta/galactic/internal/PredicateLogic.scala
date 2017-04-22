package com.michalplachta.galactic.internal

object PredicateLogic {
  implicit class PredicateWithLogic[A](p1: A ⇒ Boolean) {
    def and(p2: A ⇒ Boolean): A ⇒ Boolean = {
      a ⇒ p1(a) && p2(a)
    }

    def andNot(p2: A ⇒ Boolean): A ⇒ Boolean = {
      a ⇒ p1(a) && !p2(a)
    }
  }

  def not[A](p: A ⇒ Boolean): A ⇒ Boolean = a ⇒ !p(a)

  def always[A]: A ⇒ Boolean = _ ⇒ true
}
