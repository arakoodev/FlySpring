package com.flyspring.flyfly.utils;

import java.io.File;

public class ProjectTypeChecker {
    public static boolean isMavenProject() {
        File dir = new File(System.getProperty("user.dir"));
        for(File file: dir.listFiles())
            if(file.getName().equals("pom.xml"))
                return true;
        return false;
    }
    public static boolean isGradleProject() {
        File dir = new File(System.getProperty("user.dir"));
        for(File file: dir.listFiles())
            if(file.getName().equals("build.gradle"))
                return true;
        return false;
    }
}
