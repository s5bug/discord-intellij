package com.tsunderebug.discordintellij

import com.intellij.openapi.components.ApplicationComponent

class StartupShutdown extends ApplicationComponent {

  override def initComponent(): Unit = enableRPC()

  override def disposeComponent(): Unit = disableRPC()

}