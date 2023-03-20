# flyfly CLI
## Installation & Usage
Package the code into a jar with
```console
mvn package 
```
Go to the directory of the project you want to use and use the following command
```bash
java -jar <path-of-cli-jar> <command>
```

## Commands 

### run  
Runs the Spring Boot application. if the project has jpa and a database driver(connector) in the build file and there is not 'spring.datasource.url' in the application.properties. Then the CLI will start a TestContainers database and add temporary values to application.properties to allow the application to run successfully. That's if the driver is supported by the CLI and Docker is installed.  
Currently supported DBs are: MySQL, Postgres, and MariaDB.  

### format
Format the code using Spotless.
