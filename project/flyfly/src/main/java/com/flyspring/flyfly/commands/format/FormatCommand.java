package com.flyspring.flyfly.commands.format;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.flyspring.flyfly.utils.ProjectTypeChecker;

import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;

/**
 * This class represents a command for formatting code using the Spotless tool.
 * <p>
 * It is a Spring component and a Picocli command with the name "format".
 * <p>
 * This class relies on two dependencies: "formatter" and "projectTypeChecker", which are autowired using Spring's annotation-based dependency injection.
 * <p>
 * The "run" method is the entry point of the command, and it first checks whether the project is a Gradle project using the "projectTypeChecker" component.
 * If the project is a Gradle project, the "formatter" component is used to format the code.
 * If the project is not a Gradle project, the method logs an error message and exits.
 * <p>
 * This class imports the following external packages:
 * - org.springframework.beans.factory.annotation.Autowired
 * - org.springframework.stereotype.Component
 * - com.flyspring.flyfly.utils.ProjectTypeChecker
 * - lombok.extern.slf4j.Slf4j
 * - picocli.CommandLine.Command
 */
@Component
@Command(
        name = "format",
        description = "Format code with Spotless")
@Slf4j
public class FormatCommand implements Runnable {
    @Autowired
    Formatter formatter;
    @Autowired
    ProjectTypeChecker projectTypeChecker;

    @Override
    public void run() {
        if (projectTypeChecker.isGradleProject())
            formatter.format();
        else {
            log.error("Couldn't find build.gradle");
            log.error("Please try again inside the project directory");
        }
    }

}
