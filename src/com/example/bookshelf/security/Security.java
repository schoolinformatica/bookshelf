package com.example.bookshelf.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jls on 5/14/15.
 */

// All one way hashing methods go here
public class Security {

    public static String sha256(String hashMe) {
        // Get bytes and create bytes
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hash = new byte[0];
        try {
            hash = md.digest(hashMe.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // Transform bytes to hex format
        StringBuffer sb = new StringBuffer();
        for (byte b : hash) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString(); // return hexformat
    }
}
