package it.sartimarco.play.signin

import java.util.Date
import play.api.mvc.Request
import play.api.mvc.PlainResult
import play.api.data.Form
import play.api.mvc.Call
import play.api.mvc.Content
import it.sartimarco.play.signin.views._
import play.api.mvc.Flash
import play.api.templates.Html
import play.api.mvc.Call

trait SignInConfig {
  
  type AccountType
  type ActivationKeyType

  def signInForm(form: Form[(String, Option[String], String, String)])(implicit flash: Flash): Html
  def onSignInResult(email: String, ok: Boolean) : PlainResult
  def onValidationResult(email: String, ok: Boolean) : PlainResult
  def onValidationRequest(email: String, key: String) : Call

  def findAccountByEmail(email: String) : Option[AccountType]
  def createDisabledAccount(email: String, name: Option[String], password: String) : Option[AccountType]
  def enableAccount(email: String): Option[AccountType]

  def createActivationKey(email: String, key: String, expires_on: Date): Option[ActivationKeyType]
  def findActivationKey(email: String, key: String): Option[ActivationKeyType]
  def consumeActivationKey(email: String, key: String): Option[ActivationKeyType]






  def mailTemplateTxt(confirmation_url: String, app_name: String): Content = txt.verificationMail(confirmation_url, app_name)
  def mailTemplateHtml(confirmation_url: String, app_name: String): Content = html.verificationMail(confirmation_url, app_name)

}