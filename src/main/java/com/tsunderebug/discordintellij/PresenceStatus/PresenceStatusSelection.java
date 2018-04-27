package com.tsunderebug.discordintellij.PresenceStatus;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;

import java.lang.reflect.Field;

public class PresenceStatusSelection extends AnAction {
    private final PresenceStatus presenceStatus;

    public PresenceStatusSelection() {
        this.presenceStatus = null;
    }

    public PresenceStatusSelection(String definedStatus, PresenceStatus presenceStatus) {
        super(definedStatus);
        this.presenceStatus = presenceStatus;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        presenceStatus.setStatus(this.getTemplatePresentation().getText());

    }
}
