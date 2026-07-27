package com.nectavox.nxcore.utils;

import java.util.ArrayList;
import java.util.List;

public final class Placeholders {

    private Placeholders() {}

    public static String apply(String text, Object... replacements) {
        for (int i = 0; i + 1 < replacements.length; i += 2) {
            text = text.replace(String.valueOf(replacements[i]), String.valueOf(replacements[i + 1]));
        }
        return text;
    }

    public static List<String> apply(List<String> lines, Object... replacements) {
        List<String> result = new ArrayList<>(lines.size());
        for (String line : lines) {
            result.add(apply(line, replacements));
        }
        return result;
    }
}