package com.flyspring.flyfly.commands.run;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.lang3.SystemUtils;
import org.springframework.stereotype.Component;
import org.zeroturnaround.exec.ProcessExecutor;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GradleRunner extends RunnerTemplate {

    @Override
    void runTheProject() throws IOException {
        String[] command;
        if(SystemUtils.IS_OS_WINDOWS)
            command = new String[]{"cmd", "/c", "gradle", "bootRun"};
        else
            command = new String[]{"gradle", "bootRun"};

        runningProcess = new ProcessExecutor()
                .command(command)
                .redirectOutput(System.out)
                .start().getProcess();
    }

    @Override
    void checkAndConfigureServices() {
        log.info("Checking if services are needed");
        try {
        BufferedReader reader = 
            new BufferedReader(
                new FileReader("build.gradle"));
        String line;
        while((line = reader.readLine()) != null){
            if(line.contains("dependencies")){
                while((line = reader.readLine()) != null){
                    int start = line.indexOf("\'");
                    int end = line.indexOf(":");
                    if(start < 0 || end < 0)
                        continue;
                    String groupID = line.substring(start+1, end);
                    if(supportedDBGroupIds.contains(groupID)){
                        if( ! testContainersStarter.isServiesNeeded())
                            break;
                        log.info("Found : " + groupID);
                        switch(groupID){
                            case "mysql","com.mysql" -> testContainersStarter.startMySQL();
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
        } catch (Exception e) {e.printStackTrace();}
    }

}
