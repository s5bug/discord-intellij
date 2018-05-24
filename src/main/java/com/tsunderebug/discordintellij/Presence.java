package com.tsunderebug.discordintellij;

import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.project.Project;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Presence {

    private static final Presence instance = new Presence();
    private static Map<Project, Long> projects = new HashMap<>();

    private final String versionName;
    private final String fullVersion;
    private final String apiVersion;
    private final String build;
    private Project currentProject = null;
    private long startTimeStamp;

    private Presence() {
        this.versionName = ApplicationInfo.getInstance().getVersionName();
        this.fullVersion = ApplicationInfo.getInstance().getFullVersion();
        this.apiVersion = ApplicationInfo.getInstance().getApiVersion();
        this.build = ApplicationInfo.getInstance().getBuild().asString();
    }

    public static Presence getInstance(){
        return instance;
    }

    public String getVersionName() {
        return versionName;
    }

    public String getFullVersion() {
        return fullVersion;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public String getBuild() {
        return build;
    }

    public long getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setProjectOpenTime(Project project, long startTime) {
        projects.put(project, startTime);
        startTimeStamp = TimeUnit.MILLISECONDS.toSeconds(startTime);
        currentProject = project;
    }

    private void setStartTimeStamp(long startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public void setStartTimeStamp(Project project) {
        this.setStartTimeStamp(TimeUnit.MILLISECONDS.toSeconds(projects.get(project)));
    }

    public void removeProject(Project project) {
        projects.remove(project);
        if (project.equals(currentProject)) {
            currentProject = null;
        }
    }

    public Project getCurrentProject() {
        return currentProject;
    }

    public String getApplication() {
        return ApplicationInfo.getInstance().getVersionName();
    }
}
