package org.example.utility;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

public class FolderEncryptorDecryptor {

    private static final String algorithm = "AES";
    private static final String transformer = "AES/CBC/PKCS5Padding";
    private final String secret;

    public FolderEncryptorDecryptor(String secret) {
        this.secret = secret;
    }

    public void encrypt(String srcFolder, String destFolder) throws NoSuchAlgorithmException, NoSuchPaddingException, IOException, InvalidKeyException {
        FileReader fileReader = new FileReader();
        Set<File> files = fileReader.getFiles(srcFolder);

        SecretKey secretKey = new SecretKeySpec(secret.getBytes(), algorithm);
        FileEncryptorDecryptor encryptorDecryptor = new FileEncryptorDecryptor(secretKey, transformer);
        fileReader.createPath(destFolder);
        for (File file : files) {
            String path = file.getPath().replace(srcFolder, "").replaceFirst("/", "");
            if (path.contains("/")) {
                fileReader.createPath(destFolder + "/" + path.substring(0, path.lastIndexOf('/')));
            }
            encryptorDecryptor.encrypt(file, destFolder + "/" + path);
        }
    }

    public void decrypt(String srcFolder, String destFolder) throws NoSuchAlgorithmException, NoSuchPaddingException, IOException, InvalidKeyException, InvalidAlgorithmParameterException {
        FileReader fileReader = new FileReader();
        Set<File> files = fileReader.getFiles(srcFolder);

        SecretKey secretKey = new SecretKeySpec(secret.getBytes(), algorithm);
        FileEncryptorDecryptor encryptorDecryptor = new FileEncryptorDecryptor(secretKey, transformer);
        fileReader.createPath(destFolder);
        for (File file : files) {
            String path = file.getPath().replace(srcFolder, "").replaceFirst("/", "");
            if (path.contains("/")) {
                fileReader.createPath(destFolder + "/" + path.substring(0, path.lastIndexOf('/')));
            }
            encryptorDecryptor.decrypt(file, destFolder + "/" + path);
        }
    }
}
