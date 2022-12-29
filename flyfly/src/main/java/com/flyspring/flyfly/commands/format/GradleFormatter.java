package com.flyspring.flyfly.commands.format;

import java.io.*;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.SystemUtils;
import org.springframework.stereotype.Component;
import org.testcontainers.shaded.org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.*;

@Component
public class GradleFormatter extends FormatterTemplate {

    @Override
    boolean isSpotlessInstalled() throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader("build.gradle"));
        String line;
        while((line = reader.readLine()) != null){
            if(line.contains("spotless")){
                reader.close();
                return true;
            }
        }
        reader.close();
        return false;
    }

    @Override
    void installSpotless() throws IOException {
        BufferedReader reader = 
            new BufferedReader(
                new FileReader("build.gradle"));
        String line;
        StringBuilder sb = new StringBuilder();
        while((line = reader.readLine()) != null){
            sb.append(line + "\n");
            if(line.contains("plugins")){
                sb.append("\tid \"com.diffplug.spotless\" version \"6.12.0\"\n");
            }
        }
        reader.close();
        sb.append(plugin);
        BufferedWriter writer = new BufferedWriter(new FileWriter("build.gradle"));
        writer.write(sb.toString());
        writer.flush();
        writer.close();
    }

    @Override
    void runSpotless() throws InvalidExitValueException, IOException, InterruptedException, TimeoutException {
        String[] command;
        if(SystemUtils.IS_OS_WINDOWS)
            command = new String[]{"cmd", "/c", "gradle", "spotlessApply"};
        else
            command = new String[]{"gradle", "spotlessApply"};
        
        new ProcessExecutor()
            .command(command)
            .redirectOutput(System.out)
            .start().getProcess().waitFor();
    }
    
    private String plugin = 
"""
spotless {
    java {
        target fileTree('.') {
            include '**/*.java'
            exclude '**/build/**', '**/build-*/**'
        }
        toggleOffOn()
        googleJavaFormat()
        removeUnusedImports()
        trimTrailingWhitespace()
        endWithNewline()
    }
}
""";
    
}
