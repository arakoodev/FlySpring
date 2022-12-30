package com.flyspring.flyfly.commands.run;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.lang3.SystemUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.stereotype.Component;
import org.zeroturnaround.exec.ProcessExecutor;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MavenRunner extends RunnerTemplate {
    @Override
    void runTheProject() throws IOException {
        String[] command;
        if(SystemUtils.IS_OS_WINDOWS)
            command = new String[]{"cmd", "/c", "mvn", "spring-boot:run", "-e"};
        else
            command = new String[]{"mvn", "spring-boot:run", "-e"};

        runningProcess = new ProcessExecutor()
            .command(command)
            .redirectOutput(System.out)
            .start().getProcess();

    }

    boolean mavenreader() throws FileNotFoundException, IOException, XmlPullParserException {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new FileReader("pom.xml"));
        for(Dependency dependency: model.getDependencies()){
            if(dependency.getGroupId().equals("org.springframework.boot")
                && dependency.getArtifactId().equals("spring-boot-devtools"))
                return true;
        }
        return false;
    }

    @Override
    void checkAndConfigureServices() {
        log.info("Checking if services are needed");
        try {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new FileReader("pom.xml"));
        for(Dependency dependency: model.getDependencies()){
            if(supportedDBGroupIds.contains(dependency.getGroupId())){
                if( ! testContainersStarter.isServiesNeeded())
                    return;
                log.info("Found : " + dependency.getGroupId());
                switch(dependency.getGroupId()){
                    case "mysql","com.mysql" -> testContainersStarter.startMySQL();
                    case "org.postgresql" -> testContainersStarter.startPostgreSQL();
                    case "org.mariadb.jdbc" -> testContainersStarter.startMariaDB();
                }
            }
        }
        } catch (Exception e) {e.printStackTrace();}
    }
}
