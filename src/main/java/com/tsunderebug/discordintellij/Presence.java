package com.tsunderebug.discordintellij;

import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.project.Project;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Presence {

    private static final Presence instance = new Presence();
    private static Map<Project, Long> projects = new HashMap<>();

    private final String applicationKey;
    private final String versionName;
    private final String fullVersion;
    private final String apiVersion;
    private final String build;
    private Project currentProject = null;
    private long startTimeStamp;
    private String file = "";
    private String fileType = "";
    private String fileTypeKey;

    private Presence() {
        applicationKey = ApplicationInfo.getInstance().getBuild().asString().substring(0, 2).toLowerCase();
        versionName = ApplicationInfo.getInstance().getVersionName();
        fullVersion = ApplicationInfo.getInstance().getFullVersion();
        apiVersion = ApplicationInfo.getInstance().getApiVersion();
        build = ApplicationInfo.getInstance().getBuild().asString();
    }

    public boolean hasFile() {
        return (this.file != null && !this.file.isEmpty());
    }

    public boolean hasCurrentProject() {
        return this.currentProject != null;
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

    public String getProjectName() {
        return currentProject != null ? currentProject.getName() : "";
    }

    public String getApplication() {
        return ApplicationInfo.getInstance().getVersionName();
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file, String fileType) {
        this.file = file;
        setFileType(fileType.split("\\s|\\.|/")[0]);
    }

    public void clearFile() {
        this.file = "";
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
        this.fileTypeKey = fileType.toLowerCase().replaceAll("#", "sharp").replaceAll("\\+", "p");
    }

    public void clearFileType() {
        setFileType("");
    }

    public String getFileTypeString() {
        return String.format("Editing a %s file", getFileType());
    }

    public String getFileTypeKey() {
        return fileTypeKey;
    }

    public void setFileTypeKey(String fileTypeKey) {
        this.fileTypeKey = fileTypeKey;
    }

    public String getApplicationText() {
        return String.format("Using %s", getVersionName());
    }

    public String getApplicationKey() {
        return applicationKey;
    }
}
