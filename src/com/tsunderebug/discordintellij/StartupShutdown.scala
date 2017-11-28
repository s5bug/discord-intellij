package com.tsunderebug.discordintellij

import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.components.ApplicationComponent
import com.tsunderebug.drpc.RichPresence

class StartupShutdown extends ApplicationComponent {

  override def initComponent(): Unit = {
    RichPresence.start("384215522050572288")
    RichPresence(
      state = s"In IDE ${getIDEName(ApplicationInfo.getInstance().getBuild.asString())}",
      details = s"${ApplicationInfo.getInstance().getApiVersion}"
    ).submit()
  }

  def getIDEName(bn: String): String = {
    val code = bn.take(2).toLowerCase
    code match {
      case "ic" | "iu" => "IntelliJ"
      case "py" => "PyCharm"
      case "rm" => "RubyMine"
      case "go" => "GoLand"
      case "cl" => "CLion"
      case "ps" => "PhpStorm"
      case "ws" => "WebStorm"
    }
  }

  override def disposeComponent(): Unit = RichPresence.stop()

}