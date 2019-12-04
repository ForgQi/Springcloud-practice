package com.forgqi.resourcebaseserver.common.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.previous;

public class ParseUtil {
    public static Optional<LocalDate> parse(String text, Integer lastFewDays) {
        LocalDate now = LocalDate.now();
        LocalDate date = null;
        if ("thisWeek".equals(text)) {
            date = now.with(previous(DayOfWeek.MONDAY));
        } else if ("thisMonth".equals(text)) {
            date = now.with(firstDayOfMonth());
        } else if (lastFewDays != null) {
            date = now.minusDays(lastFewDays);
        }
        return Optional.ofNullable(date);
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
