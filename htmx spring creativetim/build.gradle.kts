plugins {
    java
    id("org.springframework.boot") version ("3.0.4")
    id("io.spring.dependency-management") version ("1.1.0")
    id("com.github.node-gradle.node") version "3.4.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("io.github.wimdeblauwe:htmx-spring-boot-thymeleaf:2.0.0")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.mysql:mysql-connector-j:8.0.31")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.register<com.github.gradle.node.npm.task.NpxTask>("tailwind"){
    command.set("tailwindcss")
    args.set(listOf("-i", "./src/main/resources/static/styles.css",
        "-o" ,"./src/main/resources/static/output.css", "--watch"))
}
tasks.register("sync"){
    inputs.files("./src/main/resources/static","./src/main/resources/templates",)
    doLast {
        sync {
            from("./src/main/resources/static")
            into("build/resources/main/static")
        }
        sync {
            from("./src/main/resources/templates")
            into("build/resources/main/templates")
        }
    }
}


group = "com.htmx.thymeleaf"
version = "0.0.1-SNAPSHOT"
description = "htmx"
java.sourceCompatibility = JavaVersion.VERSION_17
