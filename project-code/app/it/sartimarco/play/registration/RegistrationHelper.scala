/**
 *
 * Copyright (C) 2012 Marco Sarti <marco.sarti@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 *
 */
package it.sartimarco.play.registration

import java.math.BigInteger
import java.security.SecureRandom
import com.typesafe.plugin.use
import com.typesafe.plugin.MailerPlugin
import play.api.Play.current
import play.api.data.Forms.email
import play.api.data.Forms.optional
import play.api.data.Forms.text
import play.api.data.Forms.tuple
import play.api.data.Form
import play.api.i18n.Messages
import play.api.mvc.Controller
import play.api.mvc.Request
import play.api.mvc.Action
import play.api.mvc.PlainResult
import scala.util.Random
import play.api.mvc.Call
import play.api.Logger





trait RegistrationHelper[U, K] {
  self: Controller with RegistrationConfig[U, K] =>


  def onRegistrationFailed[A](form: Form[(String, Option[String], String, String)]) (implicit request: Request[A]) : PlainResult = {
    registrationFailed(form)(request)
  }

  private val random = new Random(new SecureRandom())

  def generateKey(email: String) : String = {
    val table = "abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val key = Stream.continually(random.nextInt(table.size)).map(table).take(64).mkString
    if (findActivationKey(email, key).isDefined) generateKey(email) else key
  }

  def generateConfirmationUrl[A](email: String, key: String)(implicit request: Request[A]) : String = {
    confirmationCall(email, key).absoluteURL(false)
  }

  def onRegistrationSuccess[A] (form: (String, Option[String], String, String))(implicit request: Request[A]) : PlainResult = {
        val key = generateKey(form._1)
        val new_key = createActivationKey(form._1, key)
        val new_account = createDisabledAccount(form._1, form._2, form._3)
        sendVerificationMail(form._1, key)
        
        registrationSuccess(new_account.get)(request)
  }


  def sendVerificationMail[A](email: String, key: String) (implicit request: Request[A]) : Unit = {
    val mail = use[MailerPlugin].email
    val confirmationUrl = generateConfirmationUrl(email, key)
    Logger.info(confirmationUrl)
  }




  val registerForm = Form(
    tuple(
      "email" -> email.verifying(Messages("error.mailAlreadyPresent"), m => findAccountByEmail(m.toLowerCase).isEmpty),
      "name" -> optional(text),
      "password" -> text,
      "password_confirm" -> text).verifying(Messages("error.passwordDontMatch"), t => t._2 == t._3))

  def registrationAction[A](implicit request: Request[A]) = {
    registerForm.bindFromRequest.fold(
      formWithErrors => onRegistrationFailed(formWithErrors),
      result => onRegistrationSuccess(result)
    )
  }

}