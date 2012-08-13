package it.sartimarco.play.registration

trait RegistrableAccountCompanion {

  type AccountType <: RegistrableAccount

  def createDisabledAccount(email:String, name: Option[String], password: String): AccountType

  def enableAccount(account: AccountType): Unit

  def findAccountByEmail(email: String): Option[AccountType]

}