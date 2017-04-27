package ying.backend_features.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

    public static Long getUTCNowTimestamp () {
        return getNowTimestamp(ZoneOffset.UTC);
    }

    public static Long getNowTimestamp (ZoneId zoneId) {
        return now(zoneId).toInstant().toEpochMilli();
    }

    public static ZonedDateTime nowUTC () {
        return now (ZoneOffset.UTC);
    }

    public static ZonedDateTime now (String timeZone) {
        return now (ZoneId.of(timeZone));
    }

    public static ZonedDateTime now (ZoneId zoneId) {
        return ZonedDateTime.now(zoneId);
    }

    public static ZoneId getZoneId (String timeZone) {
        return ZoneId.of(timeZone);
    }

    public static ZonedDateTime getZonedDateTime (Long date, ZoneId zoneId) {
        return Instant.ofEpochMilli(date).atZone(zoneId);
    }

    /**
     * 2016-06-27T15:15:25.864-04:00
     */
    public static String getZonedDateTimeString (Long date, ZoneId zoneId) {
        return getZonedDateTime(date, zoneId).format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public static ZonedDateTime changeTimeZone (ZonedDateTime zonedDateTime, ZoneId zoneId) {
        return zonedDateTime.withZoneSameInstant(zoneId);
    }

    public static ZonedDateTime string2UTCZonedDateTime (String dateString) {
        return string2ZonedDateTime(dateString, ZoneOffset.UTC);
    }

    public static ZonedDateTime string2ZonedDateTime (String dateString) {
        return ZonedDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME);
    }

    public static ZonedDateTime string2ZonedDateTime (String dateString, ZoneId zoneId) {
        return ZonedDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME).withZoneSameInstant(zoneId);
    }
}
