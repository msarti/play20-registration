package it.sartimarco.play.registration

import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.Messages
import java.util.Date


trait RegistrationHelper extends Controller {
  
  type ActivationKey <: ActivationKeyRecord
  
  def findAccountByEmail[A](email: String): Option[A]
  
  def createActivationKey(email: String, key: String, expiration: Date): ActivationKey
  
  def consumeActivationKey[A](key: String): Option[A]

  val registerForm = Form(
    tuple(
      "email" -> email.verifying(Messages("error.mail.present"), m => findAccountByEmail(m.toLowerCase).isEmpty),
      "name" -> text,
      "password" -> text,
      "password_confirm" -> text).verifying("Passwords do not match", t => t._3 == t._4))
  
  
  def registerFormPage(form: Form[(String, String, String, String)])
  

}