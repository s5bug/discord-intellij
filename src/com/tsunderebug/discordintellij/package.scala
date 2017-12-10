package com.tsunderebug

package object discordintellij {

  def getIDEName(code: String): String = {
    code match {
      case "ic" | "iu" => "IntelliJ IDEA"
      case "py" | "pc" | "pe" => "PyCharm"
      case "rm" => "RubyMine"
      case "go" => "GoLand"
      case "cl" => "CLion"
      case "ps" => "PhpStorm"
      case "ws" => "WebStorm"
      case "ac" => "AppCode"
      case "mp" => "MPS"
      case "db" => "DataGrip"
      case "rd" => "Rider"
      case _ => s"[Unknown IDE ${code.toUpperCase}]"
    }
  }

}
