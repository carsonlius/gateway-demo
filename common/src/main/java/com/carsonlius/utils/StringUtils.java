package com.carsonlius.utils;

/**
 * @version V1.0
 * @author:
 * @date: 2022/6/23 15:49
 * @company
 * @description
 */


import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils extends org.apache.commons.lang3.StringUtils {
    static final String PATTERN = "[A-Z]";

    public StringUtils() {
    }

    public static String underscoreToCamelCase(String underscore) {
        String[] ss = underscore.split("_");
        if (ss.length == 1) {
            return underscore;
        } else {
            StringBuffer sb = new StringBuffer();
            sb.append(ss[0]);

            for(int i = 1; i < ss.length; ++i) {
                sb.append(upperFirstCase(ss[i]));
            }

            return sb.toString();
        }
    }

    public static String toLine(String camelCase) {
        Pattern humpPattern = Pattern.compile("[A-Z]");
        Matcher matcher = humpPattern.matcher(camelCase);
        StringBuffer sb = new StringBuffer();

        while(matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }

        matcher.appendTail(sb);
        return sb.toString();
    }

    private static String lowerFirstCase(String str) {
        char[] chars = str.toCharArray();
        chars[0] = (char)(chars[0] + 32);
        return String.valueOf(chars);
    }

    public static String upperFirstCase(String str) {
        char[] chars = str.toCharArray();
        chars[0] = (char)(chars[0] - 32);
        return String.valueOf(chars);
    }

    public static String buildThrowableMsg(Throwable t) {
        return isBlank(t.getMessage()) ? Arrays.toString(t.getStackTrace()) : t.getMessage();
    }

    public static String buildExceptionMsg(Exception e) {
        String stackTrace = getStackTrace(e);
        String exceptionType = e.toString();
        String exceptionMessage = e.getMessage();
        return String.format("%s : %s \r\n %s", exceptionType, exceptionMessage, stackTrace);
    }

    public static String getStackTrace(Throwable t) {
        StackTraceElement[] stackTraceElements = t.getStackTrace();
        StringBuilder result = new StringBuilder();
        if (stackTraceElements != null && stackTraceElements.length > 0) {
            StackTraceElement[] var3 = stackTraceElements;
            int var4 = stackTraceElements.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                StackTraceElement element = var3[var5];
                result.append(element.toString());
                result.append("\n");
            }
        }

        return result.toString();
    }
}
