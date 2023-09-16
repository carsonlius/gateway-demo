package com.carsonlius.utils;

import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;

import java.util.HashMap;
import java.util.Map;



public class AntiSqlFilter {

    private static final String[] KEY_WORDS = {";", "\"", "\'", "/*", "*/", "--", "exec",
            "select", "update", "delete", "insert",
            "alter", "drop", "create", "shutdown"};

    public static Map<String, String[]> getSafeParameterMap(Map<String, String[]> parameterMap) {
        Map<String, String[]> map = new HashMap<>(parameterMap.size());
        for (String key : parameterMap.keySet()) {
            String[] oldValues = parameterMap.get(key);
            map.put(key, getSafeValues(oldValues));
        }
        return map;
    }

    public static String[] getSafeValues(String[] oldValues) {
        if (ArrayUtils.isNotEmpty(oldValues)) {
            String[] newValues = new String[oldValues.length];
            for (int i = 0; i < oldValues.length; i++) {
                newValues[i] = getSafeValue(oldValues[i]);
            }
            return newValues;
        }
        return null;
    }

    public static String getSafeValue(String oldValue) {
        if (oldValue == null || "".equals(oldValue)) {
            return oldValue;
        }
        StringBuilder sb = new StringBuilder(oldValue);
        String lowerCase = oldValue.toLowerCase();
        for (String keyWord : KEY_WORDS) {
            int x;
            while ((x = lowerCase.indexOf(keyWord)) >= 0) {
                if (keyWord.length() == 1) {
                    sb.replace(x, x + 1, " ");
                    lowerCase = sb.toString().toLowerCase();
                    continue;
                }
                sb.deleteCharAt(x + 1);
                lowerCase = sb.toString().toLowerCase();
            }
        }
        return sb.toString();
    }

}
