package com.tsunderebug.discordintellij

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import com.intellij.openapi.util.IconLoader

class ToggleAction extends AnAction {

  override def actionPerformed(a: AnActionEvent): Unit = {
    a.getPresentation.setIcon(ToggleAction.enabled)
    a.getPresentation.setDisabledIcon(ToggleAction.disabled)

  }

}

object ToggleAction {

  val enabled = IconLoader.findIcon("/icons/enabled.png")
  val disabled = IconLoader.findIcon("/icons/disabled.png")

}
