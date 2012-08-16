package it.sartimarco.play.registration

import java.util.Date
import play.api.mvc.Request
import play.api.mvc.PlainResult
import play.api.data.Form
import play.api.mvc.Call

trait RegistrationConfig[U, K] {

  def findAccountByEmail(email: String) : Option[U]

  def createDisabledAccount(email: String, name: Option[String], password: String) : Option[U]

  def createActivationKey(email: String, key: String): Option[K]

  def findActivationKey(email: String, key: String): Option[K]

  def consumeActivationKey(email: String, key: String): Option[K]

  def enableAccount(email: String): Option[U]

  def registrationFailed[A](form: Form[(String, Option[String], String, String)])(request: Request[A]): PlainResult
  def registrationSuccess[A](new_account: U)(request: Request[A]): PlainResult

  def confirmationCall(email: String, key: String) : Call


}