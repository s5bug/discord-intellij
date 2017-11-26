package com.tsunderebug.discordintellij

import com.intellij.openapi.components.ApplicationComponent
import com.tsunderebug.drpc.RichPresence

class StartupShutdown extends ApplicationComponent {

  override def initComponent(): Unit = {
    RichPresence.start("384215522050572288")
  }

  override def disposeComponent(): Unit = RichPresence.stop()

}