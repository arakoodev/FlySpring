package com.flyspring.flyfly.commands.format;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.SystemUtils;
import org.springframework.stereotype.Component;
import org.testcontainers.shaded.org.zeroturnaround.exec.InvalidExitValueException;
import org.testcontainers.shaded.org.zeroturnaround.exec.ProcessExecutor;


@Component
public class MavenFormatter extends FormatterTemplate {

    @Override
    boolean isSpotlessInstalled() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("pom.xml"));
        String line;
        while((line = reader.readLine()) != null){
            if(line.contains("com.diffplug.spotless")){
                reader.close();
                return true;
            }
        }
        reader.close();
        return false;
    }

    @Override
    void installSpotless() throws IOException {
        String buildFile = Files.readString(Path.of("pom.xml"));
        String fileWithSpotless = buildFile.replace("</plugins>", plugin);
        BufferedWriter writer = new BufferedWriter(new FileWriter("pom.xml"));
        writer.write(fileWithSpotless);
        writer.flush();
        writer.close();
    }

    @Override
    void runSpotless() throws InvalidExitValueException, IOException, InterruptedException, TimeoutException {
        String[] command;
        if(SystemUtils.IS_OS_WINDOWS)
            command = new String[]{"cmd", "/c", "mvn", "spotless:apply"};
        else
            command = new String[]{"mvn", "spotless:apply"};
            
        new ProcessExecutor()
            .command(command)
            .redirectOutput(System.out)
            .start().getProcess().waitFor();
    }
    
    private String plugin = 
        """
            <plugin>
				<groupId>com.diffplug.spotless</groupId>
				<artifactId>spotless-maven-plugin</artifactId>
				<version>2.28.0</version>
				<configuration>
					<java>
						<includes>
							<include>src/main/java/**/*.java</include> <!-- Check application code -->
							<include>src/test/java/**/*.java</include> <!-- Check application tests code -->
						</includes>
						<googleJavaFormat>
							<version>1.15.0</version>
							<style>GOOGLE</style>
						</googleJavaFormat>
					</java>
				</configuration>
			</plugin>
        </plugins>
                """;
}
