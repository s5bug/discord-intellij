package com.tsunderebug.discordintellij.slack;

import com.tsunderebug.discordintellij.Presence;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SlackUtil {
    private static final String DEFAULT_STATUS_TEXT = "Editing [FILE] for [PROJECT] in [TOOL]";
    private static final Pattern pattern = Pattern.compile("\\[(.+?)\\]");

    public static String tokenizeStatus(Presence presence) {
        return tokenizeStatus(presence, DEFAULT_STATUS_TEXT);
    }

    public static String tokenizeStatus(Presence presence, String text) {
        Matcher matcher = pattern.matcher(text);

        HashMap<String, String> replacements = new HashMap<String, String>();
        replacements.put("FILE", presence.getFile());
        replacements.put("PROJECT", presence.getProjectName());
        replacements.put("TOOL", presence.getApplication());

        StringBuilder builder = new StringBuilder();
        int i = 0;
        while (matcher.find()) {
            String replacement = replacements.get(matcher.group(1));
            builder.append(text.substring(i, matcher.start()));
            if (replacement == null)
                builder.append(matcher.group(0));
            else
                builder.append(replacement);
            i = matcher.end();
        }
        builder.append(text.substring(i, text.length()));
        return builder.toString();
    }
}
