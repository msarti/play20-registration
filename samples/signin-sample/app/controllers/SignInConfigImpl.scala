package controllers

import play.api.Play.current
import it.sartimarco.play.signin.SignInConfig
import models.Account
import models.ActivationKey
import play.api.data.Form
import play.api.mvc._
import play.api.cache.Cache
import play.api.templates._
import play.api.Logger
import java.util.Date
import collection.mutable.Map
import play.api.mvc.Results._

trait SignInConfigImpl extends SignInConfig {
  type AccountType = Account
  type ActivationKeyType = ActivationKey
  
  
  def signInForm(form: Form[(String, Option[String], String, String, Boolean)])(implicit flash: Flash): Html = views.html.form(form)
  def onValidationResult(email: String, ok: Boolean): PlainResult = Redirect(routes.SignInController.showResult)
  def onSignInResult(email: String, ok: Boolean): PlainResult = Redirect(routes.SignInController.showResult)
  def onValidationRequest(email: String, key: String): Call  = routes.SignInController.validate(email, key)

  def findAccountByEmail(email: String): Option[Account] = Account.findAccountByEmail(email)
  def createDisabledAccount(email: String, name: Option[String], password: String): Option[Account]  = Account.createDisabledAccount(email, name, password)
  def enableAccount(email: String): Option[Account]  = Account.enableAccount(email)
  
  def createActivationKey(email: String, key: String, expires_on: Date): Option[ActivationKey]  = ActivationKey.createActivationKey(email, key, expires_on)
  def findActivationKey(email: String, key: String): Option[ActivationKey]  = ActivationKey.findActivationKey(email, key)
  def consumeActivationKey(email: String, key: String): Option[ActivationKey]  = ActivationKey.consumeActivationKey(email, key)
   

}