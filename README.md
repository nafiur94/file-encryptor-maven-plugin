# Source Code Encryptor and Decryptor

This is a maven plugin that will encrypt and decrypt the source code. It will be used to encrypt the source code before sharing any project in any medium. The source code will be decrypted on the server side. This will prevent the source code from being stolen.

## Tech Stack

* Java
* Maven Plugin

## Example

Clone the repository and run the following command:

```bash
mvn clean install
```

After that, add the following code in your project's pom.xml file:

## Pre-requisite
1. Secret key should be strong enough to encrypt the source code. The secret key should be 16 characters long.
2. Specify the directory where the source code is located.

## Encryption
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.example</groupId>
            <artifactId>file-encryptor-maven-plugin</artifactId>
            <version>1.0-SNAPSHOT</version>
            <configuration>
                <directory>src</directory>
                <secret>-Y0URSTR0NGPA$$-</secret>
            </configuration>
            <executions>
                <execution>
                    <goals>
                        <goal>FileEncryptor</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```
## Decryption
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.example</groupId>
            <artifactId>file-encryptor-maven-plugin</artifactId>
            <version>1.0-SNAPSHOT</version>
            <configuration>
                <directory>src</directory>
                <secret>-Y0URSTR0NGPA$$-</secret>
            </configuration>
            <executions>
                <execution>
                    <goals>
                        <goal>FileDecryptor</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```
To encrypt or decrypt the source code, run the following command in your targeted project directory:

```bash

```bash
mvn clean install
```