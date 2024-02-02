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

@Mojo(name = "FileDecryptor", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class FileDecryptorMavenPlugin extends AbstractMojo {
    private final static String tempFolder = "/temp/decrypt";

    @Parameter(defaultValue = "-Y0URSTR0NGPA$$-")
    private String secret;

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("File Decryptor Plugin Started");
        try {
            String srcDir = project.getBasedir().getAbsolutePath() + "/src";
            String temDir = srcDir + tempFolder;
            getLog().info("Source Directory : " + srcDir);
            getLog().info("Temp Directory : " + temDir);
            FolderEncryptorDecryptor encryptorDecryptor = new FolderEncryptorDecryptor(secret);
            encryptorDecryptor.decrypt(srcDir, temDir);
            FileReader fileReader = new FileReader();
            fileReader.copyFolder(new File(temDir), new File(srcDir));
            fileReader.deleteFolder(new File(temDir).getParentFile());
        } catch (Exception e) {
            e.printStackTrace();
            throw new MojoExecutionException("Unable to Decrypt the file");
        }
        getLog().info("File Decryptor Plugin Ended");
    }
}