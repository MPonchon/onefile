## Onefile

###  What does it do ?
Merge all files of a local java project into one single file.

### How to set up ?
Copy the script ***create_onefile.sh*** to your directory **src/main/resources**  
  
Then in your project build with maven, add this code to your pom.xml:
```xml
<build>
<plugins>
    <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>exec-maven-plugin</artifactId>
        <version>3.1.1</version>
        <executions>
            <execution>
                <phase>package</phase> <!-- Adjust the phase as needed -->
                <goals>
                    <goal>exec</goal>
                </goals>
            </execution>
        </executions>
        <configuration>
            <executable>src/main/resources/create_onefile.sh</executable>
        </configuration>
    </plugin>
</plugins>
</build>
```

## How to run  it ?

It will launch the script by default at ***package*** phase and then create one single java file in the **dist** directory.  

You can change the phase at you convenience in the code above.
