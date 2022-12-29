package com.flyspring.flyfly.commands.run;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.flyspring.flyfly.utils.ProjectTypeChecker;

import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
@Component
@Command(
    name="run",
    description = "Run a Maven or Gradle Spring Boot Application")
@Slf4j
public class RunCommand implements Runnable {

    @Autowired
    MavenRunner mavenRunner;
    @Autowired
    GradleRunner gradleRunner;
    @Autowired
    TestContainersStarter testContainersStarter;

    @Override
    public void run() {
        if(ProjectTypeChecker.isMavenProject())
            mavenRunner.start();
        else if (ProjectTypeChecker.isGradleProject())
            gradleRunner.start();
        else{
            log.error("Couldn't find pom.xml or build.gradle");
            log.error("Please try again inside the project directory");
        }
    }
    
}
