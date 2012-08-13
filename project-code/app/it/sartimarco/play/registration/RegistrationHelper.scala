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

import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.Messages
import java.util.Date
import play.api.mvc.Action
import play.api.mvc.Result
import java.security.SecureRandom
import java.math.BigInteger

trait RegistrationHelper
  extends Controller {

  type KeyType <: ActivationKeyRecord
  type KeyCompanionType <: ActivationKeyCompanion

  type AccountCompanionType <: RegistrableAccountCompanion

  type AccountType <: RegistrableAccount

  val accountCompanion: AccountCompanionType

  val activationKeyCompanion: KeyCompanionType

  def onRegistrationForm(form: Form[(String, String, String)]): Result

  def getEmailBody()

  def onConfirmRequestSent : Result




  val registerForm = Form(
    tuple(
      "email" -> email.verifying(Messages("error.mailAlreadyPresent"), m => accountCompanion.findAccountByEmail(m.toLowerCase).isEmpty),
      "password" -> text,
      "password_confirm" -> text).verifying(Messages("error.passwordDontMatch"), t => t._2 == t._3))

  def registrationPage = Action { implicit request =>
    onRegistrationForm(registerForm)
  }

  def registrationAction = Action { implicit request =>
    registerForm.bindFromRequest.fold(
      formWithErrors => onRegistrationForm(formWithErrors),
      req => {
        val random = new SecureRandom
        val key = new BigInteger(130, random).toString(32)
        val new_key = activationKeyCompanion.createActivationKey(req._1, key, new Date)
        val new_account = accountCompanion.createDisabledAccount(req._1, None, req._2)
        Ok
      })
  }

}