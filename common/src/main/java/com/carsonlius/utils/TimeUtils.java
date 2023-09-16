package com.carsonlius.utils;

/**
 * @version V1.0
 * @author:
 * @date: 2022/6/23 15:47
 * @company
 * @description
 */

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class TimeUtils {
    private static final ZoneId ZONE = ZoneId.systemDefault();
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_ZONE = "yyyy-MM-dd HH:mm:ssZ";
    public static final String TIME_FORMAT_ZONE = "yyyyMMdd'T'HHmmss'Z'";
    public static final String DATE_FORMAT_STANDARD = "yyyyMMddHHmmss";
    public static final String HOUR_MINUTE_SECOND_FORMAT = "HH:mm:ss";
    private static final String REGEX = "\\:|\\-|\\s";
    public static final long WDT_UNIX_TIMESTAMP_MS_DIFF = 1325347200000L;
    public static final Long DEFAULT_DATE_LONG = 57600000L;
    public static final Long SECOND_UNIT = 1000L;
    public static final String DEFAULT_DATE_STRING = "1970-01-02 00:00:00";
    private static final DateTimeFormatter YEAR = DateTimeFormatter.ofPattern("yyyy");
    private static final DateTimeFormatter MONTH = DateTimeFormatter.ofPattern("MM");
    private static final DateTimeFormatter DAY = DateTimeFormatter.ofPattern("dd");
    private static final DateTimeFormatter HOUR = DateTimeFormatter.ofPattern("HH");
    private static final DateTimeFormatter MINUTE = DateTimeFormatter.ofPattern("mm");
    private static final DateTimeFormatter SECOND = DateTimeFormatter.ofPattern("ss");
    private static final DateTimeFormatter YEAR_MONTH_DAY = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_TIME_STANDARD = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final DateTimeFormatter HOUR_MINUTE_SECOND = DateTimeFormatter.ofPattern("HH:mm:ss");

    public TimeUtils() {
    }

    public static Long locateTimeToLong(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZONE).toInstant().toEpochMilli();
    }

    public static LocalDateTime stringTolocateTime(String stringTime) {
        if (StringUtils.isBlank(stringTime)) {
            return null;
        } else {
            return Objects.equals("0000-00-00 00:00:00", stringTime) ? null : LocalDateTime.parse(stringTime, DATE_TIME_FORMATTER);
        }
    }

    public static LocalDateTime dateToLocalDateTime(Date d) {
        Instant instant = d.toInstant();
        return LocalDateTime.ofInstant(instant, ZONE);
    }

    public static LocalDate dateToLocalDate(Date d) {
        Instant instant = d.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZONE);
        return localDateTime.toLocalDate();
    }

    public static LocalTime dateToLocalTime(Date d) {
        Instant instant = d.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZONE);
        return localDateTime.toLocalTime();
    }

    public static LocalDateTime longToToLocalTime(Long lTime) {
        return dateToLocalDateTime(new Date(lTime * 1000L));
    }

    public static LocalDate dateToLocalDate(long lTime) {
        Date date = new Date(lTime);
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZONE);
        return localDateTime.toLocalDate();
    }

    public static Date longToDayMax(long lTime) {
        Date date = new Date(lTime);
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZONE);
        LocalDateTime start = LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MAX);
        return localDateTimeToDate(start);
    }

    public static Date getDayStart(LocalDateTime localDateTime) {
        LocalDateTime start = LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MIN);
        return localDateTimeToDate(start);
    }

    public static Date longToDayMin(long lTime) {
        Date date = new Date(lTime);
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZONE);
        LocalDateTime start = LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.MIN);
        return localDateTimeToDate(start);
    }

    public static Date getOneDayMin(int days, boolean dayFlag, int years, boolean yearFlag) {
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime oneDayStart = null;
        if (dayFlag) {
            oneDayStart = todayStart.minusDays((long)days);
        } else if (yearFlag) {
            oneDayStart = todayStart.minusYears((long)years);
        }

        Instant instant = oneDayStart.atZone(ZONE).toInstant();
        return Date.from(instant);
    }

    public static Date localDateToDate(LocalDate localDate) {
        Instant instant = localDate.atStartOfDay().atZone(ZONE).toInstant();
        return Date.from(instant);
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZONE).toInstant();
        return Date.from(instant);
    }

    public static Date stringToDate(String time, String format) {
        DateTimeFormatter f = DateTimeFormatter.ofPattern(format);
        if ("yyyy-MM-dd HH:mm:ss".equals(format)) {
            return localDateTimeToDate(LocalDateTime.parse(time, f));
        } else {
            return "yyyy-MM-dd".equals(format) ? localDateToDate(LocalDate.parse(time, f)) : null;
        }
    }

    public static Long chronoUnitBetweenByDate(ChronoUnit cu, Date d1, Date d2) {
        return cu.between(dateToLocalDateTime(d1), dateToLocalDateTime(d2));
    }

    public static Long chronoUnitBetweenByTime(ChronoUnit cu, LocalDateTime from, LocalDateTime to) {
        return cu.between(from, to);
    }

    public static Long chronoUnitBetweenByString(ChronoUnit cu, String s1, String s2, String dateFormat) {
        DateTimeFormatter f = DateTimeFormatter.ofPattern(dateFormat);

        if ("yyyy-MM-dd HH:mm:ss".equals(dateFormat)) {
            LocalDateTime l1;
            LocalDateTime l2;
            l1 = dateToLocalDateTime(stringToDate(s1, dateFormat));
            l2 = dateToLocalDateTime(stringToDate(s2, dateFormat));
            return cu.between(l1, l2);
        } else if ("yyyy-MM-dd".equals(dateFormat)) {
            LocalDate l1 = dateToLocalDate(stringToDate(s1, dateFormat));
            LocalDate l2 = dateToLocalDate(stringToDate(s2, dateFormat));
            return cu.between(l1, l2);
        } else if ("yyyyMMddHHmmss".equals(dateFormat)) {
            LocalDateTime l1;
            LocalDateTime l2;
            l1 = LocalDateTime.parse(s1.replaceAll("\\:|\\-|\\s", ""), f);
            l2 = LocalDateTime.parse(s2.replaceAll("\\:|\\-|\\s", ""), f);
            return cu.between(l1, l2);
        } else {
            return null;
        }
    }

    public static Date chronoUnitPlusByDate(ChronoUnit cu, Date d1, long d2) {
        return localDateTimeToDate((LocalDateTime)cu.addTo(dateToLocalDateTime(d1), d2));
    }

    public static Long stringDateToMilli(String time) {
        return LocalDateTime.parse(time, DATE_TIME_FORMATTER).atZone(ZONE).toInstant().toEpochMilli();
    }

    public static Date timeMilliToDate(String time) {
        return Date.from(Instant.ofEpochMilli(Long.parseLong(time)));
    }

    public static List<Pair<Long, Long>> getTimeSlice(long startTime, long endTime, long interval) {
        int threshold = 1024;
        int count = 0;
        long pTime = endTime;

        ArrayList arrayList;
        for(arrayList = new ArrayList(1024); pTime > startTime && count++ < threshold; endTime = pTime + 1000L) {
            pTime = pTime - startTime > interval ? pTime - interval + 1000L : startTime;
            arrayList.add(new ImmutablePair(pTime, endTime));
        }

        return (List)arrayList.stream().sorted().collect(Collectors.toList());
    }

    public static String getYear() {
        return LocalDate.now().format(YEAR);
    }

    public static String getMonth() {
        return LocalDate.now().format(MONTH);
    }

    public static String getDay() {
        return LocalDate.now().format(DAY);
    }

    public static String getHour() {
        return LocalDateTime.now().format(HOUR);
    }

    public static String getMinute() {
        return LocalDateTime.now().format(MINUTE);
    }

    public static String getSecond() {
        return LocalDateTime.now().format(SECOND);
    }

    public static String getYearMonthDay() {
        return LocalDate.now().format(YEAR_MONTH_DAY);
    }

    public static String getCurrentDate() {
        return LocalDateTime.now().format(DATE_TIME_FORMATTER);
    }

    public static Long locateTimeToSecond(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZONE).toEpochSecond();
    }

    public static Long getTimeStampMilli() {
        return ZonedDateTime.now(ZONE).toInstant().toEpochMilli();
    }

    public static Long getTimeStampSecond() {
        return ZonedDateTime.now(ZONE).toEpochSecond();
    }

    public static String longToString(Long timesTamp) {
        return Objects.isNull(timesTamp) ? null : LocalDateTime.ofInstant(Instant.ofEpochMilli(timesTamp), ZONE).format(DATE_TIME_FORMATTER);
    }

    public static String longToStringByStandard(Long timesTamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timesTamp), ZONE).format(DATE_TIME_STANDARD);
    }

    public static String longToZoneString(Long timeStamp) {
        return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ", Locale.UK)).format(timeStamp);
    }

    public static String timeZoneString() {
        Calendar calendar = Calendar.getInstance();
        Date nowTime = calendar.getTime();
        return (new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'", Locale.CHINA)).format(nowTime.getTime());
    }

    public static long dateToTimestamp(String date) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long epoch = df.parse(date).getTime();
        return epoch / 1000L;
    }

    public static Long milliToSecond(Long millTamp) {
        return longToLocalDateTime(millTamp).atZone(ZONE).toEpochSecond();
    }

    public static LocalDateTime longToLocalDateTime(Long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, ZONE);
    }

    public static LocalDateTime longToLocalDateTimeSeconds(Long timestamp) {
        Instant instant = Instant.ofEpochSecond(timestamp);
        return LocalDateTime.ofInstant(instant, ZONE);
    }

    public static String getLastOrFutureDay(int amount) throws ParseException {
        SimpleDateFormat sj = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sj.parse(LocalDateTime.now().toString());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(5, amount);
        return sj.format(calendar.getTime());
    }

    public static String timeFormat(LocalDateTime time) {
        return timeFormat("yyyy-MM-dd HH:mm:ss", time);
    }

    public static String timeFormat(String format, LocalDateTime time) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        return time.format(dateTimeFormatter);
    }

    public static Timestamp getTimeStampPlus(Timestamp timestamp, int num, ChronoUnit unit) {
        return Timestamp.valueOf(timestamp.toLocalDateTime().plus((long)num, unit));
    }

    public static Timestamp getTimeStampPlus(int num, ChronoUnit unit) {
        return Timestamp.valueOf(LocalDateTime.now().plus((long)num, unit));
    }

    public static String getTimeStampPlusStr(int num, ChronoUnit unit) {
        return timeFormat(LocalDateTime.now().plus((long)num, unit));
    }

    public static int wdtTimestamp() {
        return (int)((System.currentTimeMillis() - 1325347200000L) / 1000L);
    }

    public static LocalDateTime strToLocalDateTime(String dateStr) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateStr, df);
    }

    public static LocalTime timeStampToLocateTime(Long timeStamp) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp), ZONE);
        return localDateTime.toLocalTime();
    }

    public static LocalTime stringToLocalTime(String stringTime) {
        return StringUtils.isBlank(stringTime) ? null : LocalTime.parse(stringTime, HOUR_MINUTE_SECOND);
    }

    public static boolean isEffectiveDate(long nowTime, String beginTime, String endTime) {
        LocalTime now = timeStampToLocateTime(nowTime);
        LocalTime begin = stringToLocalTime(beginTime);
        LocalTime end = stringToLocalTime(endTime);
        return now.isAfter(begin) && now.isBefore(end);
    }

    public static Date longToDateMills(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;

        try {
            date = sdf.parse(longToZoneString(time));
            return date;
        } catch (ParseException var5) {
            var5.printStackTrace();
            return null;
        }
    }
}
