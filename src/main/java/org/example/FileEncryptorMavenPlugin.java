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

@Mojo(name = "FileEncryptor", defaultPhase = LifecyclePhase.INSTALL)
public class FileEncryptorMavenPlugin extends AbstractMojo {
    private final static String tempFolder = "/temp/encrypt";

    @Parameter(defaultValue = "-Y0URSTR0NGPA$$-")
    private String secret;

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("File Encryptor Plugin Started");
        try {
            String srcDir = project.getBasedir().getAbsolutePath() + "/src";
            String temDir = srcDir + tempFolder;
            getLog().info("Source Directory : " + srcDir);
            getLog().info("Temp Directory : " + temDir);
            FolderEncryptorDecryptor encryptorDecryptor = new FolderEncryptorDecryptor(secret);
            encryptorDecryptor.encrypt(srcDir, temDir);
            FileReader fileReader = new FileReader();
            fileReader.copyFolder(new File(temDir), new File(srcDir));
            fileReader.deleteFolder(new File(temDir));
        } catch (Exception e) {
            throw new MojoExecutionException("Unable to Encrypt the file");
        }
        getLog().info("File Encryptor Plugin Ended");
    }
}