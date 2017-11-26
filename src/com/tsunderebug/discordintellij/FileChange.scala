package com.tsunderebug.discordintellij

import com.intellij.openapi.fileEditor.{FileEditorManagerEvent, FileEditorManagerListener}
import com.tsunderebug.drpc.RichPresence

class FileChange extends FileEditorManagerListener {

  override def selectionChanged(event: FileEditorManagerEvent): Unit = {
    val lowercaseName = event.getNewFile.getFileType.getName.take(event.getNewFile.getFileType.getName.indexOf(' ')).toLowerCase
    RichPresence(
      state = s"Working on ${event.getManager.getProject.getName}",
      largeImageKey = lowercaseName,
      largeImageText = s"Editing a ${event.getNewFile.getFileType.getName} file",
      details = s"Editing [${event.getNewFile.getFileType.getName}] ${event.getNewFile.getName}"
    ).submit()
  }

}
