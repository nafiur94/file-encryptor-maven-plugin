# Source Code Encryptor and Decryptor

This is a maven plugin that will encrypt and decrypt the source code. It will be used to encrypt the source code before
any project share in any medium. The source code will be decrypted on the server side. This will prevent the source code
from being stolen.

## Tech Stack

* Java
* Maven Plugin

## Example

Add in your pom.xml file:

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
