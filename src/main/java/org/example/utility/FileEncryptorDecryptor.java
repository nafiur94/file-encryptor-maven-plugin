package org.example.utility;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class FileEncryptorDecryptor {
    private final SecretKey secretKey;
    private final Cipher cipher;

    public FileEncryptorDecryptor(SecretKey secretKey, String transformation) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.secretKey = secretKey;
        this.cipher = Cipher.getInstance(transformation);
    }

    private void encryptFile(File file, String dest) throws InvalidKeyException, IOException {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] iv = cipher.getIV();

        try (FileOutputStream fileOut = new FileOutputStream(dest); CipherOutputStream cipherOut = new CipherOutputStream(fileOut, cipher)) {
            fileOut.write(iv);
            cipherOut.write(getFileContent(file));
        }
    }

    public void encrypt(File file, String dest) throws InvalidKeyException, IOException {
        if (file.isDirectory()) {
            return;
        }
        encryptFile(file, dest);
    }

    public void decrypt(File file, String dest) throws IOException, InvalidAlgorithmParameterException, InvalidKeyException {
        if (file.isDirectory()) {
            return;
        }
        decryptFile(file, dest);
    }

    private byte[] getFileContent(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    public void decryptFile(File file, String dest) throws IOException, InvalidAlgorithmParameterException, InvalidKeyException {
        try (FileInputStream fileIn = new FileInputStream(file)) {
            byte[] fileIv = new byte[16];
            fileIn.read(fileIv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(fileIv));

            try (CipherInputStream cipherIn = new CipherInputStream(fileIn, cipher);) {
                Files.write(new File(dest).toPath(), cipherIn.readAllBytes());
            }
        }
    }
}
