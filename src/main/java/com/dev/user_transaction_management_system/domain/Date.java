package com.dev.user_transaction_management_system.domain;

import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Date {

    private final String dateTime;
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";
    private static final String TIMEZONE = "Asia/Tehran";

    private Date(String dateTime) {
        Assert.hasText(dateTime,"date cannot be null");

        this.dateTime = dateTime;
    }

    public static Date fromLocalDateTime(LocalDateTime dateTime){
        return new Date(dateTime
                .atZone(zone())
                .format(dateFormat()));
    }

    public String asString(){
        return dateTime;
    }

    public LocalDateTime asLocalDateTime() {
        return LocalDateTime.parse(dateTime,dateFormat())
                .atZone(zone())
                .toLocalDateTime();
    }

    private static ZoneId zone() {
        return ZoneId.of(TIMEZONE);
    }

    private static DateTimeFormatter dateFormat() {
        return DateTimeFormatter.ofPattern(DATE_FORMAT);
    }
}
