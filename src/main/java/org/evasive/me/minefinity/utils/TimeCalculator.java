package org.evasive.me.minefinity.utils;

import java.util.concurrent.TimeUnit;

public class TimeCalculator {
    public static String getString(long milliseconds) {
        long hours = TimeUnit.MILLISECONDS.toHours(milliseconds);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % 60;

        String timeString;
        if (hours > 0) {
            // Include hours if they are greater than 0
            timeString = String.format("%dh %02dm %02ds", hours, minutes, seconds);
        } else if (minutes > 0) {
            // Include only minutes and seconds if hours are 0
            timeString = String.format("%dm %02ds", minutes, seconds);
        } else {
            // Include only seconds if both hours and minutes are 0
            timeString = String.format("%ds", seconds);
        }
        return timeString;
    }
}
