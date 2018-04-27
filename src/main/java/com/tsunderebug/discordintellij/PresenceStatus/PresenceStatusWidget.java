package com.tsunderebug.discordintellij.PresenceStatus;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.StatusBarWidget.MultipleTextValuesPresentation;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.openapi.wm.impl.status.EditorBasedWidget;
import com.intellij.util.Consumer;
import com.tsunderebug.discordintellij.Presence;
import org.jetbrains.annotations.CalledInAwt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.MouseEvent;

public class PresenceStatusWidget extends EditorBasedWidget
        implements MultipleTextValuesPresentation, StatusBarWidget.Multiframe
{
    protected static final Logger LOG = Logger.getInstance(PresenceStatusWidget.class);
    private static final String MAX_STRING = "Available - Editing ReallyReallyLongFilename.java";

    @Nullable private String myText;
    @Nullable private String myTooltip = "";
    private PresenceStatus myPresenceStatus;

    public PresenceStatusWidget(@NotNull Project project, Presence presence) {
        super(project);
        myPresenceStatus = presence.getPresenceStatus();
        myText = myPresenceStatus.getStatus();
        subscribeToPresenceChangeEvents(project);
    }


    @NotNull
    protected ListPopup getPopup(@NotNull Project project) {
        return PresenceStatusPopup.getInstance(project, myPresenceStatus).asListPopup();
    }

    protected void subscribeToPresenceChangeEvents(@NotNull Project project) {
        ApplicationManager.getApplication().getMessageBus().connect().subscribe(PresenceStatus.PRESENCE_STATUS_CHANGE, new PresenceStatusChangeListener() {
            @Override
            public void statusChanged(@NotNull String status) {
                LOG.debug("repository mappings changed");
                updateLater();
            }
        });
    }

    public void activate() {
        Project project = getProject();
        if (project != null) {
            installWidgetToStatusBar(project, this);
        }
    }

    public void deactivate() {
        Project project = getProject();
        if (project != null) {
            removeWidgetFromStatusBar(project, this);
        }
    }

    public void dispose() {
        deactivate();
        super.dispose();
    }

    @NotNull
    @Override
    public String ID() {
        return getClass().getName();
    }

    @Override
    public WidgetPresentation getPresentation(@NotNull PlatformType type) {
        return this;
    }

    @Override
    public void selectionChanged(@NotNull FileEditorManagerEvent event) {
        LOG.debug("selection changed");
        update();
    }

    @Override
    public void fileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
        LOG.debug("file opened");
        update();
    }

    @Override
    public void fileClosed(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
        LOG.debug("file closed");
        update();
    }

    @CalledInAwt
    @Nullable
    @Override
    public String getSelectedValue() {
        return StringUtil.isEmpty(myText) ? "" : myText;
    }

    @NotNull
    @Override
    @Deprecated
    public String getMaxValue() {
        return MAX_STRING;
    }

    @Nullable
    @Override
    public String getTooltipText() {
        return myTooltip;
    }

    @Nullable
    @Override
    public ListPopup getPopupStep() {
        Project project = getProject();
        if (project == null || project.isDisposed()) return null;

        return getPopup(project);
    }

    @Nullable
    @Override
    public Consumer<MouseEvent> getClickConsumer() {
        // has no effect since the click opens a list popup, and the consumer is not called for the MultipleTextValuesPresentation
        return null;
    }

    protected void updateLater() {
        Project project = getProject();
        if (project != null && !project.isDisposed()) {
            ApplicationManager.getApplication().invokeLater(() -> {
                LOG.debug("update after change");
                update();
            }, project.getDisposed());
        }
    }

    @CalledInAwt
    private void update() {
        myText = null;
        myTooltip = null;

        Project project = getProject();
        if (project == null || project.isDisposed()) return;

        int maxLength = MAX_STRING.length() - 1; // -1, because there are arrows indicating that it is a popup
        // TODO
        myText = StringUtil.shortenTextWithEllipsis(myPresenceStatus.getStatus(), maxLength, 5);
        myTooltip = getToolTip(project);
        if (myStatusBar != null) {
            myStatusBar.updateWidget(ID());
        }
    }

    @Nullable
    @CalledInAwt
    private String getToolTip(@NotNull Project project) {
        return myTooltip;
    }

    private void installWidgetToStatusBar(@NotNull final Project project, @NotNull final StatusBarWidget widget) {
        ApplicationManager.getApplication().invokeLater(() -> {
            StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
            if (statusBar != null && !isDisposed()) {
                statusBar.addWidget(widget, "after " + (SystemInfo.isMac ? "Encoding" : "InsertOverwrite"), project);
//                subscribeToMappingChanged();
//                subscribeToRepoChangeEvents(project);
                update();
            }
        }, project.getDisposed());
    }

    private void removeWidgetFromStatusBar(@NotNull final Project project, @NotNull final StatusBarWidget widget) {
        ApplicationManager.getApplication().invokeLater(() -> {
            StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
            if (statusBar != null && !isDisposed()) {
                statusBar.removeWidget(widget.ID());
            }
        }, project.getDisposed());
    }

    @Override
    public StatusBarWidget copy() {
        return null;
    }

    protected void subscribeToStatusChangeEvents() {
        ApplicationManager.getApplication().getMessageBus().connect().subscribe(PresenceStatus.PRESENCE_STATUS_CHANGE, new PresenceStatusChangeListener() {
            @Override
            public void statusChanged(@NotNull String status) {
                LOG.debug("status changed: " + status);
                updateLater();
            }
        });
    }
//    @Override
//    protected void subscribeToRepoChangeEvents(@NotNull Project project) {
//        project.getMessageBus().connect().subscribe(GitRepository.GIT_REPO_CHANGE, new GitRepositoryChangeListener() {
//            @Override
//            public void repositoryChanged(@NotNull GitRepository repository) {
//                LOG.debug("repository changed");
//                updateLater();
//            }
//        });
//    }

//    private void subscribeToMappingChanged() {
//        myProject.getMessageBus().connect().subscribe(VcsRepositoryManager.VCS_REPOSITORY_MAPPING_UPDATED, new VcsRepositoryMappingListener() {
//            @Override
//            public void mappingChanged() {
//                LOG.debug("repository mappings changed");
//                updateLater();
//            }
//        });
//    }
}