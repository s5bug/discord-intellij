package com.tsunderebug

package object discordintellij {

  def getIDEName(code: String): String = {
    code match {
      case "ic" | "iu" => "IntelliJ"
      case "py" | "pc" | "pe" => "PyCharm"
      case "rm" => "RubyMine"
      case "go" => "GoLand"
      case "cl" => "CLion"
      case "ps" => "PhpStorm"
      case "ws" => "WebStorm"
      case _ => s"[Unknown IDE ${code.toUpperCase}]"
    }
  }

}
