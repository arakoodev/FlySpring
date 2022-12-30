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
import org.zeroturnaround.exec.ProcessExecutor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class RunnerTemplate {
    Process runningProcess;
    WatchService filesWatcher;
    WatchService buildFileWatcher;
    @Autowired
    TestContainersStarter testContainersStarter;
    boolean allowInfrastructureServices;
    Set<String> supportedDBGroupIds = Set.of( "mysql", "com.mysql","org.postgresql", "org.mariadb.jdbc");
    
    public void start(){
        try{
        log.info("Configuring the project");
        allowInfrastructureServices = isDockerInstalled();
        if(allowInfrastructureServices)
            checkAndConfigureServices();
        log.debug("registering watcher for src files changes");
        registerFilesWatcher();
        log.debug("registering watcher for build file changes");
        registerBuildFileWatcher();
        log.info("Starting the project");
        runTheProject();
        loop();
        }catch(Exception e) {e.printStackTrace();}
    }
    boolean isDockerInstalled() throws IOException, InterruptedException{
        log.info("Checking if docker is installed to allow infrastructure services");
        int exitCode;
        try{
            String[] command;
            if(SystemUtils.IS_OS_WINDOWS)
                command = new String[]{"cmd", "/c", "docker", "--version"};
            else
                command = new String[]{"docker", "--version"};
            
            Process checkDocker = new ProcessExecutor()
                .command(command)
                .start().getProcess();
            exitCode = checkDocker.waitFor();
        }catch(Exception e){
            exitCode = -1;
        }
        if(exitCode != 0){
            log.warn("Couldn't find docker. Disabling infrastructure services.");
            return false;
        }
        return true;
    }
    abstract void runTheProject() throws IOException;
    abstract void checkAndConfigureServices();
    void registerBuildFileWatcher() throws IOException {
        Path path = Paths.get("");
        buildFileWatcher = FileSystems.getDefault().newWatchService();
        path.register(buildFileWatcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
    }

    public void loop() throws IOException, InterruptedException, XmlPullParserException{
        while(true){
            if(didFilesChange() && runningProcess.isAlive()){
                reloadTheProject();
            }
            if(didBuildFileChange()){
                handleBuildFileChange();
            }
        }
    }

    void registerFilesWatcher() throws IOException{
        Path path = Paths.get("src");
        filesWatcher = FileSystems.getDefault().newWatchService();
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                throws IOException{
                dir.register(filesWatcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
                return FileVisitResult.CONTINUE;
            }
        });
    }
    void reloadTheProject() throws IOException, InterruptedException{
        destroyRunningProcess();
        runTheProject();
    }
    boolean didFilesChange() throws InterruptedException {
        WatchKey key = filesWatcher.poll(500, TimeUnit.MILLISECONDS);
        if(key == null)
            return false;
        for(WatchEvent<?> event : key.pollEvents()) {}
        key.reset();
        if(!runningProcess.isAlive())
            return false;
        return true;
    }
    boolean didBuildFileChange() throws InterruptedException {
        WatchKey key = buildFileWatcher.poll(500, TimeUnit.MILLISECONDS);
        if(key == null)
            return false;
        boolean found = false;
        for(WatchEvent<?> event : key.pollEvents()) {
            Path p = (Path) event.context();
            if(p.endsWith("pom.xml") || p.endsWith("build.gradle"))
                found = true;
        }
        key.reset();
        if(found)
            log.info("Detected build file change ...");
        return found;
    }
    void handleBuildFileChange() throws IOException, InterruptedException{
        destroyRunningProcess();
        if(allowInfrastructureServices)
            checkAndConfigureServices();
        runTheProject();
    }
    void destroyRunningProcess() throws InterruptedException, IOException{
        if(SystemUtils.IS_OS_WINDOWS){
            Runtime.getRuntime()
            .exec("cmd.exe /c taskkill /f /t /pid "+runningProcess.pid())
            .waitFor();
        }
        else 
            runningProcess.destroy();
    }
}
