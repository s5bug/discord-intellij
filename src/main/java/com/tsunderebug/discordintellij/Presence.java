package com.tsunderebug.discordintellij;

import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.project.Project;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Presence {

    private static final Presence instance = new Presence();
    private static Map<Project, Long> projects = new HashMap<>();

    private final String application;
    private String state;
    private String details;
    private String largeImageKey;
    private String largeImageText;
    private String smallImageKey = "tsun";
    private String smallImageText = "TsundereBug's plugin: https://goo.gl/81tZHT";
    private Project currentProject = null;
    private long startTimeStamp;

    private Presence() {
        application = ApplicationInfo.getInstance().getVersionName();
        state = String.format("In %s %s", ApplicationInfo.getInstance().getVersionName(), ApplicationInfo.getInstance().getFullVersion());
        details = ApplicationInfo.getInstance().getApiVersion();
        largeImageKey = ApplicationInfo.getInstance().getBuild().asString().substring(0, 2).toLowerCase();
        largeImageText = ApplicationInfo.getInstance().getVersionName();
    }

    public static Presence getInstance(){
        return instance;
    }

    public String getApplication() {
        return application;
    }

    public String getState() {
        return state;
    }

    public String getDetails() {
        return details;
    }

    public String getLargeImageKey() {
        return largeImageKey;
    }

    public String getLargeImageText() {
        return largeImageText;
    }

    public String getSmallImageKey() {
        return smallImageKey;
    }

    public String getSmallImageText() {
        return smallImageText;
    }

    public long getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setProjectOpenTime(Project project, long startTime) {
        projects.put(project, startTime);
        startTimeStamp = TimeUnit.MILLISECONDS.toSeconds(startTime);
        currentProject = project;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setLargeImageKey(String largeImageKey) {
        this.largeImageKey = largeImageKey;
    }

    public void setLargeImageText(String largeImageText) {
        this.largeImageText = largeImageText;
    }

    public void setSmallImageKey(String smallImageKey) {
        this.smallImageKey = smallImageKey;
    }

    public void setSmallImageText(String smallImageText) {
        this.smallImageText = smallImageText;
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
}
