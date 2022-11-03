package com.application.project.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


import org.springframework.stereotype.Component;

import com.application.project.autoRoute.PropertiesLoader;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SQLiteConfig {


    public Connection getConnection() throws ClassNotFoundException, IOException {
        Connection conn = null;
        try {
            // db parameters
            Class.forName("org.sqlite.JDBC");
            Properties configuration=PropertiesLoader.loadProperties("application.properties");

            String url=configuration.getProperty("sqlite.url");

            System.out.println("URL of the SQLite"+url);
            conn = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            
        }
        return conn;
    }
    
}
