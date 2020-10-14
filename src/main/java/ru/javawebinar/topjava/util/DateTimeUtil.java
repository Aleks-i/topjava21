package ru.javawebinar.topjava.util;

import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final LocalDateTime DATE_MIN = LocalDateTime.of(1, 1, 1, 0, 0);
    public static final LocalDateTime DATE_MAX = LocalDateTime.of(3000, 1, 1, 0, 0);

    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) < 0;
    }

    public static  @Nullable LocalDate parseLocalDate(String string) {
        return string.isEmpty() ? null : LocalDate.parse(string);
    }

    public static  @Nullable LocalTime parseLocalTime(String string) {
        return string.isEmpty() ? null : LocalTime.parse(string);
    }

    public static LocalDateTime atStartOfDayOrMin(LocalDate localDate) {
        return localDate == null ? DATE_MIN : localDate.atStartOfDay();
    }

    public static LocalDateTime atStartOfNextDayOrMax(LocalDate localDate) {
        return localDate == null ? DATE_MAX : localDate.plus(1, ChronoUnit.DAYS).atStartOfDay();
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}

