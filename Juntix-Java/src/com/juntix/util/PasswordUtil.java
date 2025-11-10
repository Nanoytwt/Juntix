package com.juntix.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


/**
 * PasswordUtil
 * Utilidad que implementa hashing de contraseñas mediante PBKDF2WithHmacSHA256.
 * Se evita el uso de librerías externas para facilitar la portabilidad del prototipo.
 * Formato del hash almacenado: iterations:saltBase64:hashBase64
 */
public class PasswordUtil {
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int SALT_LEN = 16;
    private static final int HASH_LEN = 32; // bytes (256 bits)
    private static final int ITERATIONS = 100_000;

    public static String generarHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt = new byte[SALT_LEN];
        SecureRandom sr = SecureRandom.getInstanceStrong();
        sr.nextBytes(salt);
        byte[] hash = pbkdf2(password.toCharArray(), salt, ITERATIONS, HASH_LEN);
        String saltB64 = Base64.getEncoder().encodeToString(salt);
        String hashB64 = Base64.getEncoder().encodeToString(hash);
        return ITERATIONS + ":" + saltB64 + ":" + hashB64;
    }

    public static boolean verificarPassword(String password, String almacenado)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] partes = almacenado.split(":");
        if (partes.length != 3) return false;
        int iter = Integer.parseInt(partes[0]);
        byte[] salt = Base64.getDecoder().decode(partes[1]);
        byte[] hash = Base64.getDecoder().decode(partes[2]);
        byte[] prueba = pbkdf2(password.toCharArray(), salt, iter, hash.length);
        if (prueba.length != hash.length) return false;
        int diff = 0;
        for (int i = 0; i < hash.length; i++) diff |= hash[i] ^ prueba[i];
        return diff == 0;
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iteraciones, int bytes)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iteraciones, bytes * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
        return skf.generateSecret(spec).getEncoded();
    }
}
