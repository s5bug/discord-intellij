package com.tsunderebug.discordintellij;

import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.project.Project;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Presence {

    private static final Presence instance = new Presence();
    private static Map<Project, Long> projectStartTimes = new HashMap<>();
    private static Set<PresenceChangeListener> listeners = new HashSet<>();

    private final String applicationKey;
    private final String versionName;
    private final String fullVersion;
    private final String apiVersion;
    private final String build;
    private Project currentProject = null;
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

    public static void registerAgent(PresenceAgent presenceAgent) {
        listeners.add(presenceAgent);
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
        return TimeUnit.MILLISECONDS.toSeconds(projectStartTimes.get(currentProject));
    }

    public void setProjectOpenTime(Project project, long startTime) {
        projectStartTimes.put(project, startTime);
        currentProject = project;
        listeners.forEach(listener -> listener.projectChanged(this));
    }

    public void removeProject(Project project) {
        projectStartTimes.remove(project);
        if (project.equals(currentProject)) {
            currentProject = null;
        }
        listeners.forEach(listener -> listener.projectChanged(this));
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
        setFileType(fileType);
        listeners.forEach(listener -> listener.fileChanged(this));
    }

    public void clearFile() {
        setFile("", "");
    }

    public String getFileType() {
        return fileType;
    }

    private void setFileType(String fileType) {
        if (!fileType.isEmpty()) {
            String ft = fileType.split("\\s|\\.|/")[0];
            this.fileType = ft;
            this.fileTypeKey = ft.toLowerCase().replaceAll("#", "sharp").replaceAll("\\+", "p");
        } else {
            clearFileType();
        }
    }

    private void clearFileType() {
        this.fileType = "";
        this.fileTypeKey = "";
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
