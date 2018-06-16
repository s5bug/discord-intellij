package com.tsunderebug.discordintellij;

import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class ProjectStartStop implements ProjectComponent {
    private final static Logger LOG = Logger.getInstance(ProjectStartStop.class);

    private final Project project;
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

        AgentManager.getAgents().forEach(agent -> {
            agent.setCurrentProject(project);
            agent.show();
        });
    }

    public void projectClosed() {
        LOG.info(String.format("Project '%s' has been closed.", project.getName()));
        Presence presence = Presence.getInstance();
        presence.removeProject(project);

        if (project.equals(presence.getCurrentProject())) {
            AgentManager.getAgents().forEach(agent -> {
                agent.hide();
                agent.setCurrentProject(null);
            });
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
