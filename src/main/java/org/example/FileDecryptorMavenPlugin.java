package org.example;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.example.utility.FileReader;
import org.example.utility.FolderEncryptorDecryptor;

import java.io.File;
import java.io.IOException;

@Mojo(name = "FileDecryptor", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class FileDecryptorMavenPlugin extends AbstractMojo {
    private final static String tempFolder = "/temp/decrypt";

    @Parameter(defaultValue = "-Y0URSTR0NGPA$$-")
    private String secret;

    @Parameter(defaultValue = "")
    private String directory;

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("File Decryptor Plugin Started");
        FileReader fileReader = new FileReader();
        String srcDir = project.getBasedir().getAbsolutePath() + "/" + directory;
        String temDir = srcDir + tempFolder;

        try {
            getLog().info("Source Directory : " + srcDir);
            getLog().info("Temp Directory : " + temDir);
            FolderEncryptorDecryptor encryptorDecryptor = new FolderEncryptorDecryptor(secret);
            encryptorDecryptor.decrypt(srcDir, temDir);
            fileReader.copyFolder(new File(temDir), new File(srcDir));
        } catch (Exception e) {
            e.printStackTrace();
            throw new MojoExecutionException("Unable to Decrypt the file");
        } finally {
            File tempFile = new File(temDir);
            if (tempFile.exists()) {
                try {
                    fileReader.deleteFolder(tempFile.getParentFile());
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new MojoExecutionException("Unable to Remove temporary files");
                }
            }
        }
        getLog().info("File Decryptor Plugin Ended");
    }
}