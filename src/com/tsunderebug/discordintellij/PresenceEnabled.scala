package com.tsunderebug.discordintellij

import com.intellij.openapi.components.{PersistentStateComponent, State, Storage}

@State(name = "discordPresenceEnabled", storages = Array(new Storage("workspace.xml")))
case class PresenceEnabled(var enabled: Boolean) extends PersistentStateComponent[PresenceEnabled] {

  def this() = this(true)

  override def loadState(t: PresenceEnabled): Unit = enabled = t.enabled

  override def getState: PresenceEnabled = this

}
