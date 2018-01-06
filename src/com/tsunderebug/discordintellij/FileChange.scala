package com.tsunderebug.discordintellij

import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.fileEditor.{FileEditorManagerEvent, FileEditorManagerListener}
import com.tsunderebug.drpc.RichPresence

class FileChange extends FileEditorManagerListener {

  override def selectionChanged(event: FileEditorManagerEvent): Unit = {
    val pe = event.getManager.getProject.getComponent[PresenceEnabled](classOf[PresenceEnabled], new PresenceEnabled())
    pe match {
      case PresenceEnabled(true) =>
        Option(event.getNewFile) match {
          case Some(file) =>
            val name = file.getFileType.getName
            val lowercaseName = name.split("""\s""").head.toLowerCase.replaceAll("#", "sharp")
            val ide = ApplicationInfo.getInstance.getBuild.asString.take(2).toLowerCase
            RichPresence(
              state = s"Working on ${event.getManager.getProject.getName}",
              largeImageKey = lowercaseName,
              largeImageText = s"Editing a $name file",
              smallImageKey = ide,
              smallImageText = s"Using ${ApplicationInfo.getInstance().getVersionName}",
              details = s"Editing [$name] ${file.getName}",
              startTimestamp = openTimes(event.getManager.getProject) / 1000
            ).submit()
          case None =>
            val code = ApplicationInfo.getInstance().getBuild.asString().take(2).toLowerCase
            RichPresence(
              state = s"Idling",
              details = s"In ${event.getManager.getProject.getName}",
              startTimestamp = openTimes(event.getManager.getProject) / 1000,
              largeImageKey = code,
              largeImageText = ApplicationInfo.getInstance().getVersionName
            ).submit()
        }
      case _ =>
    }
  }

}
