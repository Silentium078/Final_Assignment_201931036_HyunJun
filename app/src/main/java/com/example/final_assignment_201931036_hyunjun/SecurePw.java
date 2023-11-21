package com.example.final_assignment_201931036_hyunjun;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class SecurePw {
    String securePassword;
    String saltString;
    SecurePw(String userPw) {
        this(userPw,"");
    }
        SecurePw(String userPw,String userSalt){
            if (userSalt.isEmpty()) {
                SecureRandom secureRandom = new SecureRandom();
                byte[] salt = new byte[16];
                secureRandom.nextBytes(salt);
                saltString = Base64.getEncoder().encodeToString(salt);
                securePassword = getSecurePassword(userPw, saltString);
            }else {
                securePassword = getSecurePassword(userPw, userSalt);
                saltString=userSalt;
            }
        }
        public String getSecurePw(){
            return securePassword;
        }
        public String getSalt(){
            return saltString;
        }

    public static String getSecurePassword(String passwordToHash, String salt) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] saltBytes = salt.getBytes();
            byte[] passwordBytes = passwordToHash.getBytes();
            md.update(saltBytes);
            md.update(passwordBytes);

            byte[] bytes = md.digest();

            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
}
