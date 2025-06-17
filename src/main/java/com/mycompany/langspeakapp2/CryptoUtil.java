/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.langspeakapp2;

/**
 *
 * @author ACER
 */

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.mindrot.jbcrypt.BCrypt;
import com.lambdaworks.crypto.SCryptUtil;

public class CryptoUtil {

    // PBKDF2 hashing
    public static String hashPBKDF2(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 65536;
        int keyLength = 256;
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = factory.generateSecret(spec).getEncoded();

        return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean validatePBKDF2(String password, String stored) throws Exception {
        String[] parts = stored.split(":");
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] hashStored = Base64.getDecoder().decode(parts[1]);

        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, hashStored.length * 8);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hashCheck = factory.generateSecret(spec).getEncoded();

        return Base64.getEncoder().encodeToString(hashCheck).equals(parts[1]);
    }

    // bcrypt hashing
    public static String hashBCrypt(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean validateBCrypt(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }

    // scrypt hashing
    public static String hashScrypt(String password) {
        return SCryptUtil.scrypt(password, 16384, 8, 1);
    }

    public static boolean validateScrypt(String password, String hashed) {
        return SCryptUtil.check(password, hashed);
    }

    // Utility method to generate salt
    private static byte[] getSalt() {
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
}
