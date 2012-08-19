package controllers

import play.api.Play.current
import it.sartimarco.play.signin.SignInHelper
import play.api.mvc.Controller
import models.ActivationKey
import models.Account
import it.sartimarco.play.signin.SignInConfig
import play.api.mvc.Flash
import play.api.templates.Html
import play.api.mvc.Call
import java.util.Date
import play.api.data.Form
import models.ActivationKey
import play.api.cache.Cache
import collection.mutable.Map
import play.api.mvc.Action
import play.api.Logger

object SignInController extends Controller with SignInHelper with SignInConfigImpl {
	
  def showResult = Action { implicit request =>
    Ok(views.html.signInResult(""))
  }
}