package com.tsunderebug.discordintellij

import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.tsunderebug.drpc.RichPresence

class ProjectChange extends StartupActivity {

  override def runActivity(project: Project): Unit = {
    val code = ApplicationInfo.getInstance().getBuild.asString().take(2).toLowerCase
    val hook = new FileChange
    project.getMessageBus.connect().subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, hook)
    RichPresence(
      state = s"Opened ${project.getName}",
      details = s"${ApplicationInfo.getInstance().getApiVersion}",
      largeImageKey = code,
      largeImageText = getIDEName(code)
    ).submit()
  }

}
