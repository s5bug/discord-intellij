package com.tsunderebug.discordintellij

import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

class ProjectChange extends StartupActivity {

  override def runActivity(project: Project): Unit = {
    project.getMessageBus.connect().subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, new FileChange)
  }

}
