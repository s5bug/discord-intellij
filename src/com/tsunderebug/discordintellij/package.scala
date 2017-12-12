package com.tsunderebug

import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.project.Project
import com.tsunderebug.drpc.RichPresence

package object discordintellij {

  def enableRPC(): Unit = {
    RichPresence.start("384215522050572288")
    val code = ApplicationInfo.getInstance().getBuild.asString().take(2).toLowerCase
    RichPresence(
      state = s"In ${ApplicationInfo.getInstance().getVersionName} ${ApplicationInfo.getInstance().getFullVersion}",
      details = s"${ApplicationInfo.getInstance().getApiVersion}",
      largeImageKey = code,
      largeImageText = ApplicationInfo.getInstance().getVersionName
    ).submit()
  }

  def enableRPC(p: Project): Unit = {
    enableRPC()
    new ProjectChange().runActivity(p)
  }

  def disableRPC(): Unit = {
    RichPresence.stop()
  }

}
