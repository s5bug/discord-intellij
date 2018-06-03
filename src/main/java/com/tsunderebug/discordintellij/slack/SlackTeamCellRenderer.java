package com.tsunderebug.discordintellij.slack;

import javax.swing.*;
import java.awt.*;

public class SlackTeamCellRenderer extends JLabel
        implements ListCellRenderer<SlackTeamMember> {

    SlackTeamCellRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends SlackTeamMember> list,
                                                  SlackTeamMember value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {
        setText(value.getTeamName());
        setToolTipText(String.format("Team ID: %s\nUser ID: %s", value.getTeamId(), value.getUserId()));

        if (isSelected)

        {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        setFont(list.getFont());

        setEnabled(list.isEnabled());

        return this;
    }
}