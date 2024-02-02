package org.example.utility;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class FileEncryptorDecryptor {
    private final SecretKey secretKey;
    private final Cipher cipher;

    private final String[] ignoredFiles = {"png"};

    public FileEncryptorDecryptor(SecretKey secretKey, String transformation) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.secretKey = secretKey;
        this.cipher = Cipher.getInstance(transformation);
    }

    private boolean isEncryptAble(File file) {
        String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        for (int i = 0; i < ignoredFiles.length; i++) {
            if (extension.equals(ignoredFiles[0])) {
                return false;
            }
        }
        return true;
    }

    private void encryptFile(File file, String dest) throws InvalidKeyException, IOException{
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] iv = cipher.getIV();

        try (FileOutputStream fileOut = new FileOutputStream(dest);
             CipherOutputStream cipherOut = new CipherOutputStream(fileOut, cipher)) {
            fileOut.write(iv);
            cipherOut.write(encodeFileToBase64Binary(file));
        }
    }

    public void encrypt(File file, String dest) throws InvalidKeyException, IOException {
        if (file.isDirectory()) {
            return;
        }
        if (isEncryptAble(file)) {
            encryptFile(file, dest);
        } else {
            copyFile(file, dest);
        }
    }

    public void decrypt(File file, String dest) throws IOException, InvalidAlgorithmParameterException, InvalidKeyException {
        if (file.isDirectory()) {
            return;
        }
        if (isEncryptAble(file)) {
            decryptFile(file, dest);
        } else {
            copyFile(file, dest);
        }
    }

    private void copyFile(File file, String dest) throws IOException {
        try (InputStream in = new BufferedInputStream(Files.newInputStream(file.toPath()));
             OutputStream out = new BufferedOutputStream(Files.newOutputStream(Paths.get(dest)))) {
            byte[] buffer = new byte[1024];
            int lengthRead;
            while ((lengthRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, lengthRead);
                out.flush();
            }
        }
    }

    private byte[] encodeFileToBase64Binary(File file) throws IOException {
        return Base64.getEncoder().encode(Files.readAllBytes(file.toPath()));
    }

    public void decryptFile(File file,  String dest) throws IOException, InvalidAlgorithmParameterException, InvalidKeyException {
        String content;

        try (FileInputStream fileIn = new FileInputStream(file)) {
            byte[] fileIv = new byte[16];
            fileIn.read(fileIv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(fileIv));

            try (CipherInputStream cipherIn = new CipherInputStream(fileIn, cipher);
                 InputStreamReader inputReader = new InputStreamReader(cipherIn);
                 BufferedReader reader = new BufferedReader(inputReader)) {

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                content = sb.toString();
            }
        }
        Files.write(new File(dest).toPath(), Base64.getDecoder().decode(content));
    }
}
