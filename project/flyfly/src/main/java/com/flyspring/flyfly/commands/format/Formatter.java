/**
 * This class represents a code formatter using Spotless.
 * <p>
 * To use the formatter, create a new instance of the Formatter class and call the format() method.
 * The format() method will check if the project has a Spotless configuration script (.flyfly/format.gradle)
 * and if not, it will add one using the ProjectSetup class. It will then run the Spotless command to format
 * the code according to the configuration script.
 * <p>
 * This class has external dependencies on the Apache Commons Lang 3 library and the Lombok logging library.
 * <p>
 * This class imports the following packages:
 * - org.apache.commons.lang3.SystemUtils
 * - org.springframework.beans.factory.annotation.Autowired
 * - org.springframework.stereotype.Component
 * - com.flyspring.flyfly.utils.ProjectSetup
 * - lombok.extern.slf4j.Slf4j
 */
package com.flyspring.flyfly.commands.format;


import org.apache.commons.lang3.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.flyspring.flyfly.utils.ProjectSetup;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Formatter {
    @Autowired
    ProjectSetup projectSetup;

    void format() {
        try {
            log.info("Checking formatting configuration");
            if (!projectSetup.formatScriptExists()) {
                log.info("Configuring Spotless");
                projectSetup.addFormatScript();
            }
            log.info("Running Spotless");
            String[] command;
            if (SystemUtils.IS_OS_WINDOWS)
                command = new String[]{"cmd", "/c", "gradlew.bat -I .flyfly/format.gradle spotlessApply"};
            else
                command = new String[]{"bash", "-c", "./gradlew -I .flyfly/format.gradle spotlessApply"};

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.inheritIO();
            pb.start().waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
