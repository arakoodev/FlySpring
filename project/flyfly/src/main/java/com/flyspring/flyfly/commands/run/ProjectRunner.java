/*
This module provides a `ProjectRunner` class to run a Java Spring Boot project. It uses the Gradle build tool to build and run the project. The class also provides functionality to watch for changes in the project source code and automatically rebuild and restart the project as necessary. Additionally, it provides functionality to check if Docker is installed on the system, and if so, start a database service using TestContainers library.

The class uses the `ProjectSetup` and `TestContainersStarter` classes to perform project setup and database service startup tasks.

Attributes:
    testContainersStarter (TestContainersStarter): An instance of `TestContainersStarter` class used to start a database service.
    projectSetup (ProjectSetup): An instance of `ProjectSetup` class used to setup the project.
    runningProcess (Process): The running process of the project.
    filesWatcher (WatchService): A watch service to watch for changes in the project source code.
    buildFileWatcher (WatchService): A watch service to watch for changes in the project build file.
    allowInfrastructureServices (bool): A boolean flag indicating whether Docker is installed on the system, and if so, whether infrastructure services can be started.

Methods:
    run(): Runs the project, sets up the project, starts a database service (if necessary), and starts the watch services for changes in project source code and build file.
    isDockerInstalled(): Checks if Docker is installed on the system.
    runTheProject(): Runs the project using Gradle.
    checkAndConfigureServices(): Checks if a database service is needed for the project and starts it if necessary.
    registerBuildFileWatcher(): Registers a watch service for changes in the project build file.
    loop(): A loop to watch for changes in the project source code and build file and rebuild and restart the project as necessary.
    registerFilesWatcher(): Registers a watch service for changes in the project source code.
    reloadTheProject(): Stops the running project process and starts a new one.
    didFilesChange(): Checks if changes have been made to the project source code.
    didBuildFileChange(): Checks if changes have been made to the project build file.
*/

package com.flyspring.flyfly.commands.run;

import java.io.*;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;

import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.SystemUtils;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zeroturnaround.exec.ProcessExecutor;

import com.flyspring.flyfly.utils.ProjectSetup;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ProjectRunner {
    @Autowired
    TestContainersStarter testContainersStarter;
    @Autowired
    ProjectSetup projectSetup;

    Process runningProcess;
    WatchService filesWatcher;
    WatchService buildFileWatcher;
    boolean allowInfrastructureServices;

    public void run() {
        try {
            log.info("Configuring the project");
            log.info("Checking if initscript exists");
            if (!projectSetup.initscriptExists()) {
                log.info("Initscript doesn't exist");
                log.info("Adding flyfly.gradle to initscripts");
                projectSetup.addInitscript();
            }
            projectSetup.addAutorouteJar();
            allowInfrastructureServices = isDockerInstalled();
            if (allowInfrastructureServices)
                checkAndConfigureServices();
            log.debug("registering watcher for src files changes");
            registerFilesWatcher();
            log.debug("registering watcher for build file changes");
            registerBuildFileWatcher();
            log.info("Starting the project");
            runTheProject();
            loop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean isDockerInstalled() throws IOException, InterruptedException {
        log.info("Checking if docker is installed to allow infrastructure services");
        int exitCode;
        try {
            String[] command;
            if (SystemUtils.IS_OS_WINDOWS)
                command = new String[]{"cmd", "/c", "docker", "info"};
            else
                command = new String[]{"docker", "info"};

            exitCode = new ProcessExecutor()
                    .command(command).start()
                    .getProcess().waitFor();
        } catch (Exception e) {
            exitCode = -1;
        }
        if (exitCode != 0) {
            log.warn("Couldn't find docker. Disabling infrastructure services.");
            return false;
        }
        return true;
    }

    void runTheProject() throws IOException {
        String[] command;
        if (SystemUtils.IS_OS_WINDOWS)
            command = new String[]{"cmd", "/c", "gradlew.bat", "bootRun"};
        else
            command = new String[]{"./gradlew", "bootRun"};

        runningProcess = new ProcessExecutor()
                .command(command)
                .redirectOutput(System.out)
                .start().getProcess();
    }

    void checkAndConfigureServices() throws IOException {
        log.info("Checking if services are needed");
        Set<String> supportedDBGroupIds =
                Set.of("mysql", "com.mysql", "org.postgresql", "org.mariadb.jdbc");
        BufferedReader reader =
                new BufferedReader(
                        new FileReader("build.gradle"));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("dependencies")) {
                while ((line = reader.readLine()) != null) {
                    int start = line.indexOf("\'");
                    int end = line.indexOf(":");
                    if (start < 0 || end < 0)
                        continue;
                    String groupID = line.substring(start + 1, end);
                    if (supportedDBGroupIds.contains(groupID)) {
                        if (!testContainersStarter.isServiesNeeded())
                            break;
                        log.info("Found : " + groupID);
                        switch (groupID) {
                            case "mysql", "com.mysql" -> testContainersStarter.startMySQL();
                            case "org.postgresql" -> testContainersStarter.startPostgreSQL();
                            case "org.mariadb.jdbc" -> testContainersStarter.startMariaDB();
                        }
                        break;
                    }
                }
                break;
            }
        }
        reader.close();
    }

    void registerBuildFileWatcher() throws IOException {
        Path path = Paths.get("");
        buildFileWatcher = FileSystems.getDefault().newWatchService();
        path.register(buildFileWatcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
    }

    public void loop() throws IOException, InterruptedException, XmlPullParserException {
        while (true) {
            if (didFilesChange() && runningProcess.isAlive()) {
                reloadTheProject();
            }
            if (didBuildFileChange()) {
                handleBuildFileChange();
            }
        }
    }

    void registerFilesWatcher() throws IOException {
        Path path = Paths.get("src");
        filesWatcher = FileSystems.getDefault().newWatchService();
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException {
                dir.register(filesWatcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    void reloadTheProject() throws IOException, InterruptedException {
        destroyRunningProcess();
        runTheProject();
    }

    boolean didFilesChange() throws InterruptedException {
        WatchKey key = filesWatcher.poll(500, TimeUnit.MILLISECONDS);
        if (key == null)
            return false;
        for (WatchEvent<?> event : key.pollEvents()) {
        }
        key.reset();
        if (!runningProcess.isAlive())
            return false;
        return true;
    }

    boolean didBuildFileChange() throws InterruptedException {
        WatchKey key = buildFileWatcher.poll(500, TimeUnit.MILLISECONDS);
        if (key == null)
            return false;
        boolean found = false;
        for (WatchEvent<?> event : key.pollEvents()) {
            Path p = (Path) event.context();
            if (p.endsWith("build.gradle"))
                found = true;
        }
        key.reset();
        if (found)
            log.info("Detected build file change ...");
        return found;
    }

    void handleBuildFileChange() throws IOException, InterruptedException {
        destroyRunningProcess();
        if (allowInfrastructureServices)
            checkAndConfigureServices();
        runTheProject();
    }

    @PreDestroy
    void destroyRunningProcess() throws InterruptedException, IOException {
        if (runningProcess == null)
            return;
        if (SystemUtils.IS_OS_WINDOWS) {
            Runtime.getRuntime()
                    .exec("cmd.exe /c taskkill /f /t /pid " + runningProcess.pid())
                    .waitFor();
        } else
            runningProcess.destroy();
    }
}
