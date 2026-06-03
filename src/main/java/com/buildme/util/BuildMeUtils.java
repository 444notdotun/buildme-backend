package com.buildme.util;

import java.time.*;

/**
 * Utility class for shared helper methods
 * Follows Single Responsibility Principle by consolidating utility logic
 */
public class BuildMeUtils {

    public static String getWeekKey(LocalDate date) {
        return date.getYear() + "-W" + (date.getDayOfYear() / 7);
    }

    public static String getWeekKey() {
        return getWeekKey(LocalDate.now());
    }

    public static String simpleHash(String input) {
        int hash = 5381;
        String str = input + "buildme_dotman_salt_2026";
        for (char c : str.toCharArray()) {
            hash = ((hash << 5) + hash) + c;
        }
        return Integer.toHexString(Math.abs(hash));
    }

    public static String camelToSnake(String camel) {
        return camel.replaceAll("([A-Z])", "_$1").toLowerCase();
    }

    public static boolean isMonday() {
        return LocalDate.now().getDayOfWeek() == DayOfWeek.MONDAY;
    }

    public static boolean isSunday() {
        return LocalDate.now().getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    public static boolean isFirstOfMonth() {
        return LocalDate.now().getDayOfMonth() == 1;
    }

    public static boolean isQuarterStart() {
        LocalDate today = LocalDate.now();
        return isFirstOfMonth() &&
            (today.getMonthValue() == 1 ||
             today.getMonthValue() == 4 ||
             today.getMonthValue() == 7 ||
             today.getMonthValue() == 10);
    }
}

