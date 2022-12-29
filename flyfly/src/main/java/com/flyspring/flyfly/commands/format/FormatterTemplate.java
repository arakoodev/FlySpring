package com.flyspring.flyfly.commands.format;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.testcontainers.shaded.org.zeroturnaround.exec.InvalidExitValueException;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public abstract class FormatterTemplate {

    void format(){
        try{
        log.info("Checking Spotless configuration");
        if(! isSpotlessInstalled()){
            log.info("Configuring Spotless");
            installSpotless();
        }
        log.info("Running Spotless");
        runSpotless();
        log.info("Done.");
        }catch(Exception e) {e.printStackTrace();}
        
    }

    abstract boolean isSpotlessInstalled() throws IOException;
    abstract void installSpotless() throws IOException;
    abstract void runSpotless() throws InvalidExitValueException, IOException, InterruptedException, TimeoutException;
}
