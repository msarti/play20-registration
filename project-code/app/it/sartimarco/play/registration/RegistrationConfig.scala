package it.sartimarco.play.registration

import java.util.Date
import play.api.mvc.Request
import play.api.mvc.PlainResult
import play.api.data.Form
import play.api.mvc.Call

trait RegistrationConfig {

  type Account
  type ActivationKey

  def findAccountByEmail(email: String) : Option[Account]

  def createDisabledAccount(email: String, name: Option[String], password: String) : Option[Account]

  def createActivationKey(email: String, key: String): Option[ActivationKey]

  def findActivationKey(email: String, key: String): Option[ActivationKey]

  def consumeActivationKey(email: String, key: String): Option[ActivationKey]

  def enableAccount(email: String): Option[Account]

  def registrationFailed[A](form: Form[(String, Option[String], String, String)])(request: Request[A]): PlainResult
  def registrationSuccess[A](new_account: Account)(request: Request[A]): PlainResult

  def confirmationCall(email: String, key: String) : Call


}