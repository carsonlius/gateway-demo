package com.carsonlius.utils;

/**
 * @version V1.0
 * @author:
 * @date: 2022/6/23 15:50
 * @company
 * @description
 */

import java.security.MessageDigest;

public class DigestUtils {
    public DigestUtils() {
    }

    public static String getSHAHash(String str) {
        String specHash = null;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(str.getBytes("UTF-8"));
            specHash = toHash(digest.digest());
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return specHash;
    }

    private static String toHash(byte[] bytes) {
        StringBuffer buffer = new StringBuffer();
        String temp = null;

        for(int i = 0; i < bytes.length; ++i) {
            temp = Integer.toHexString(bytes[i] & 255);
            if (temp.length() == 1) {
                buffer.append("0");
            }

            buffer.append(temp);
        }

        return buffer.toString();
    }

    public static String getMD5Hash(String str) {
        String specHash = null;

        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(str.getBytes("UTF-8"));
            specHash = toHash(digest.digest());
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return specHash;
    }
}
