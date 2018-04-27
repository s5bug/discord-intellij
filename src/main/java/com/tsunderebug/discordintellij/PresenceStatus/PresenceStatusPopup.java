package com.tsunderebug.discordintellij.PresenceStatus;

import com.intellij.dvcs.ui.BranchActionGroupPopup;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.util.Condition;
import com.tsunderebug.discordintellij.Presence;
import org.jetbrains.annotations.NotNull;

public class PresenceStatusPopup {
    @NotNull
    private final Project myProject;
    @NotNull
    private final PresenceStatus myPresenceStatus;
    private final BranchActionGroupPopup myPopup;
    private static final String DIMENSION_SERVICE_KEY = "Presence.Status.Popup";


    static PresenceStatusPopup getInstance(@NotNull final Project project, @NotNull final PresenceStatus presenceStatus) {
        Condition<AnAction> preselectActionCondition = action -> {
            return false;
        };
        return new PresenceStatusPopup(project, preselectActionCondition, presenceStatus);
    }

    protected PresenceStatusPopup(Project project, @NotNull Condition<AnAction> preselectActionCondition, PresenceStatus presenceStatus) {
        myProject = project;
        myPresenceStatus = presenceStatus;
        String title = "Presence Status";
        myPopup = new BranchActionGroupPopup(title, myProject, preselectActionCondition, createActions(), DIMENSION_SERVICE_KEY);
    }

    @NotNull
    public ListPopup asListPopup() {
        return myPopup;
    }

    @NotNull
    private ActionGroup createActions() {
        DefaultActionGroup popupGroup = new DefaultActionGroup(null, false);
        popupGroup.add(new PresenceStatusSelection("Active", myPresenceStatus));
        popupGroup.add(new PresenceStatusSelection("Away", myPresenceStatus));
        return popupGroup;
    }
}