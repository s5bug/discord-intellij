package com.tsunderebug.discordintellij

import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.components.ApplicationComponent
import com.tsunderebug.drpc.RichPresence

class StartupShutdown extends ApplicationComponent {

  override def initComponent(): Unit = {
    RichPresence.start("384215522050572288")
    val code = ApplicationInfo.getInstance().getBuild.asString().take(2).toLowerCase
    RichPresence(
      state = s"In IDE ${getIDEName(code)}",
      details = s"${ApplicationInfo.getInstance().getApiVersion}",
      largeImageKey = code,
      largeImageText = ApplicationInfo.getInstance().getBuild.asString()
    ).submit()
  }

  override def disposeComponent(): Unit = RichPresence.stop()

}