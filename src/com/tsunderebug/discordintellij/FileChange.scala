package com.tsunderebug.discordintellij

import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.fileEditor.{FileEditorManagerEvent, FileEditorManagerListener}
import com.tsunderebug.drpc.RichPresence

class FileChange extends FileEditorManagerListener {

  override def selectionChanged(event: FileEditorManagerEvent): Unit = {
    val name = event.getNewFile.getFileType.getName
    val lowercaseName = name.split("""\s""").head.toLowerCase
    RichPresence(
      state = s"Working on ${event.getManager.getProject.getName}",
      largeImageKey = lowercaseName,
      largeImageText = s"Editing a $name file",
      smallImageKey = "logo",
      smallImageText = s"Using IntelliJ IDEA ${ApplicationInfo.getInstance.getBuild.asString}",
      details = s"Editing [$name] ${event.getNewFile.getName}",
      startTimestamp = System.currentTimeMillis() / 1000
    ).submit()
  }

}
