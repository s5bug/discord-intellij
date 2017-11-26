package com.tsunderebug.discordintellij

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
      details = s"Editing [$name] ${event.getNewFile.getName}"
    ).submit()
  }

}
