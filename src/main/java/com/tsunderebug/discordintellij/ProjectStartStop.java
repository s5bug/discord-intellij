package com.tsunderebug.discordintellij;

import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.tsunderebug.discordintellij.PresenceStatus.PresenceStatusWidget;
import org.jetbrains.annotations.NotNull;

public class ProjectStartStop implements ProjectComponent {
    private final static Logger LOG = Logger.getInstance(ProjectStartStop.class);

    private final Project project;
    private StatusBar statusBar;


    @NotNull
    private final StartupShutdown applicationComponent;

    /**
     * @param project The current project, i.e. the project which was just opened.
     */
    public ProjectStartStop(Project project, @NotNull StartupShutdown applicationComponent) {
        this.project = project;
        this.applicationComponent = applicationComponent;
    }

    public void initComponent() {
        //called before projectOpened()
    }

    public void projectOpened() {
        LOG.info(String.format("Project '%s' has been opened, base dir '%s'", project.getName(), project.getBaseDir().getCanonicalPath()));
        Presence presence = Presence.getInstance();
        presence.setProjectOpenTime(project, System.currentTimeMillis());
        String code = ApplicationInfo.getInstance().getBuild().asString().substring(0, 2).toLowerCase();
        FileChange hook = new FileChange();
        project.getMessageBus().connect().subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, hook);
        presence.setState(String.format("Opened %s", project.getName()));
        presence.setDetails(String.format("%s", ApplicationInfo.getInstance().getApiVersion()));
        presence.setLargeImageKey(code);
        presence.setLargeImageText(ApplicationInfo.getInstance().getVersionName());

        PresenceEnabled presenceEnabled = project.getComponent(PresenceEnabled.class, new PresenceEnabled());
        if(presenceEnabled.isEnabled()) {
            AgentManager.getAgents().forEach(x -> x.enable(Presence.getInstance()));
        }

        if (this.statusBar == null) {
            this.statusBar = WindowManager.getInstance().getStatusBar(project);
        }
        statusBar.addWidget(new PresenceStatusWidget(project, presence));
    }

    public void projectClosed() {
        LOG.info(String.format("Project '%s' has been closed.", project.getName()));
        Presence presence = Presence.getInstance();
        presence.removeProject(project);

        PresenceEnabled presenceEnabled = project.getComponent(PresenceEnabled.class, new PresenceEnabled());
        if(presenceEnabled.isEnabled() && project.equals(presence.getCurrentProject())) {
            AgentManager.getAgents().forEach(PresenceAgent::hide);
        }
    }

    public void disposeComponent() {
        //called after projectClosed()
    }

    @NotNull
    public String getComponentName() {
        return "projectStartStop";
    }
}
