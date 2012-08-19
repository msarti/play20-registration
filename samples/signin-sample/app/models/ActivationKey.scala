package models

import java.util.Date
import play.api.cache.Cache
import play.api.Play.current
import play.api._
import collection.mutable.Map


case class ActivationKey (
		key: String,
		email: String,
		expires_on: Date
)


object ActivationKey {

  def createActivationKey(email: String, key: String, expires_on: Date): Option[ActivationKey]  = {
    val activation_key = ActivationKey(email, key, expires_on)
    val activation_keys = Cache.getAs[Map[String, ActivationKey]]("activation_keys").getOrElse[Map[String, ActivationKey]](Map[String, ActivationKey]())
    activation_keys.put(key, activation_key)
    Cache.set("activation_keys", activation_keys)
    Some(activation_key)
  }
  def findActivationKey(email: String, key: String): Option[ActivationKey]  = {
    val activation_keys = Cache.getAs[Map[String, ActivationKey]]("activation_keys").getOrElse[Map[String, ActivationKey]](Map[String, ActivationKey]())
    val result = activation_keys.get(key)
    result match {
      case None => {
	    Logger.info(email + " verification key not found. Something went wrong")
      }
      case _ => {
	    Logger.info(email + " found")
      }
    }
    result
  }
  def consumeActivationKey(email: String, key: String): Option[ActivationKey]  = {
    val activation_keys = Cache.getAs[Map[String, ActivationKey]]("activation_keys").getOrElse[Map[String, ActivationKey]](Map[String, ActivationKey]())
    val result =activation_keys.remove(key)
    result match {
      case None => {
	    Logger.warn(email + " verification key not found. Something went wrong?")
      }
      case _ => {
	    Logger.info(email + " found")
      }
    }
    result
  }
  
  
}