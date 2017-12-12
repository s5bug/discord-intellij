package com.tsunderebug.discordintellij

import javax.swing.Icon

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import com.intellij.openapi.util.IconLoader

class ToggleAction extends AnAction {

  override def actionPerformed(a: AnActionEvent): Unit = {
    val pe = a.getProject.getComponent[PresenceEnabled](classOf[PresenceEnabled], new PresenceEnabled())
    pe match {
      case PresenceEnabled(true) =>
        hideRPC()
        pe.loadState(PresenceEnabled(false))
        a.getPresentation.setIcon(ToggleAction.disabled)
      case PresenceEnabled(false) =>
        pe.loadState(new PresenceEnabled())
        a.getPresentation.setIcon(ToggleAction.enabled)
        enableRPC(a.getProject)
    }
  }

}

object ToggleAction {

  val enabled: Icon = IconLoader.findIcon("/icons/enabled.png")
  val disabled: Icon = IconLoader.findIcon("/icons/disabled.png")

}
