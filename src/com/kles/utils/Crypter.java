/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kles.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 *
 * @author jchau
 */
public class Crypter {

    public void testCrypt() {
        try {
            String s = "Hello there. How are you? Have a nice day.";

            // Generate key
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128);
            SecretKey aesKey = kgen.generateKey();

            // Encrypt cipher
            Cipher encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            encryptCipher.init(Cipher.ENCRYPT_MODE, aesKey);

            // Encrypt
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, encryptCipher);
            cipherOutputStream.write(s.getBytes());
            cipherOutputStream.flush();
            cipherOutputStream.close();
            byte[] encryptedBytes = outputStream.toByteArray();

            // Decrypt cipher
            Cipher decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(aesKey.getEncoded());
            decryptCipher.init(Cipher.DECRYPT_MODE, aesKey, ivParameterSpec);

            // Decrypt
            outputStream = new ByteArrayOutputStream();
            ByteArrayInputStream inStream = new ByteArrayInputStream(encryptedBytes);
            CipherInputStream cipherInputStream = new CipherInputStream(inStream, decryptCipher);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = cipherInputStream.read(buf)) >= 0) {
                outputStream.write(buf, 0, bytesRead);
            }

            System.out.println("Result: " + new String(outputStream.toByteArray()));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String encrypt() {
        String s = "";
        return s;
    }
}
