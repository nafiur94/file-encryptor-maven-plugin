package org.example.utility;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class FileReader {

    private Set<File> getFiles(File dir) {
        Set<File> files = new HashSet<>();
        File filesList[] = dir.listFiles();
        if (filesList == null) {
            return new HashSet<>();
        }
        for (File file : filesList) {
            if (!file.getName().startsWith(".") && file.isDirectory()) {
                files.addAll(getFiles(file));
            } else if (!file.getName().startsWith(".")) {
                files.add(file);
            }
        }
        return files;
    }

    public Set<File> getFiles(String dir) {
        return getFiles(new File(dir));
    }

    public Path createPath(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new RuntimeException("Could not initialize folder for upload!");
            }
        }
        return Paths.get(file.getAbsolutePath());
    }

    public void copyFolder(File source, File destination) throws IOException {
        if (source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdirs();
            }
            for (String file : Objects.requireNonNull(source.list())) {
                File srcFile = new File(source, file);
                File destFile = new File(destination, file);
                copyFolder(srcFile, destFile);
            }
        } else {
            try (InputStream in = Files.newInputStream(source.toPath());
                 OutputStream out = Files.newOutputStream(destination.toPath());) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            }
        }
    }

    public void deleteFolder(File source) throws IOException {
        if (source.isDirectory()) {
            for (String file : Objects.requireNonNull(source.list())) {
                File srcFile = new File(source, file);
                deleteFolder(srcFile);
            }
        }
        Files.delete(source.toPath());
    }
}
