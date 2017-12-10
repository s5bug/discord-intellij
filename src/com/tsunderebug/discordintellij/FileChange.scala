package com.tsunderebug.discordintellij

import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.fileEditor.{FileEditorManagerEvent, FileEditorManagerListener}
import com.tsunderebug.drpc.RichPresence

class FileChange extends FileEditorManagerListener {

  override def selectionChanged(event: FileEditorManagerEvent): Unit = {
    Option(event.getNewFile) match {
      case Some(file) =>
        val name = file.getFileType.getName
        val lowercaseName = name.split("""\s""").head.toLowerCase
        val ide = ApplicationInfo.getInstance.getBuild.asString.take(2).toLowerCase
        RichPresence(
          state = s"Working on ${event.getManager.getProject.getName}",
          largeImageKey = lowercaseName,
          largeImageText = s"Editing a $name file",
          smallImageKey = ide,
          smallImageText = s"Using ${getIDEName(ide)} version ${ApplicationInfo.getInstance.getBuild.asString}",
          details = s"Editing [$name] ${file.getName}",
          startTimestamp = System.currentTimeMillis() / 1000
        ).submit()
      case None =>
        val code = ApplicationInfo.getInstance().getBuild.asString().take(2).toLowerCase
        RichPresence(
          state = s"Idling",
          details = s"In ${event.getManager.getProject.getName}",
          startTimestamp = System.currentTimeMillis() / 1000,
          largeImageKey = code,
          largeImageText = getIDEName(code)
        ).submit()
    }

  }

}
