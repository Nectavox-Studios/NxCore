package com.nectavox.nxcore.utils;

public class TimeFormatUtil {
    public static String getTimeFormatted(int seconds) {
        if (seconds < 0) {
            seconds = 0;
        }

        int days = seconds / (24 * 60 * 60);
        seconds %= (24 * 60 * 60);

        int hours = seconds / (60 * 60);
        seconds %= (60 * 60);

        int minutes = seconds / 60;
        int sec = seconds % 60;

        StringBuilder formattedTime = new StringBuilder();

        if (days > 0) {
            formattedTime.append(days).append("d ");
        }
        if (hours > 0) {
            formattedTime.append(hours).append("h ");
        }
        if (minutes > 0) {
            formattedTime.append(minutes).append("m ");
        }
        if (sec > 0) {
            formattedTime.append(sec).append("s");
        }

        if (formattedTime.isEmpty()) {
            return "0s";
        }

        return formattedTime.toString().trim();
    }
}
