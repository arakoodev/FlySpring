package com.application.project.autoRoute;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.reactive.function.server.RouterFunctions.Builder;

import com.application.project.annotation.PathVariableAnnotation;
import com.application.project.entity.PersonEntity;
import com.application.project.repository.PersonRepo;
import com.application.project.repository.PersonService;

import jakarta.annotation.PostConstruct;

import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
@EnableAutoConfiguration
@EntityScan(basePackageClasses=PersonEntity.class)
@EnableJpaRepositories(basePackageClasses = PersonRepo.class)
@ComponentScan(basePackageClasses = PersonService.class)
@EnableTransactionManagement
public class RouterFunctionConfig {


    @Autowired
    private  AutowireCapableBeanFactory autowireCapableBeanFactory;

    @Bean
    public RouterFunction<ServerResponse> routerFunction(){
        ClassLoader classLoader = getClass().getClassLoader();

        URL sring =classLoader.getResource("");
        String path =sring.getPath().substring(1);
        Path currentRelativePath = Paths.get(path);
        String srcPth ="\\com\\application\\project\\myapi";
        File[] files = new File(currentRelativePath+srcPth).listFiles();

        System.out.println("FILE in Sample "+files.toString());

        return RouterFunctions.route()
        .path("/route",builder->{
            registerRouter(files, builder,"");
        })
        .build();
    }

    private String getFileName(String filename){
        return FilenameUtils.removeExtension(filename);
    }

    private void registerRouter(File[] files, Builder builder, String directory){
        for (File file : files) {
            if (file.isDirectory()) {
    
                System.out.println("Directory for myapi: " + file.getName());
                directory=directory+(directory.equals("")?"":"/")+file.getName();
                System.out.println("directory: " + directory);
                if(file.listFiles().length>0){
                    registerRouter(file.listFiles(), builder,directory);
                }
                
            } else {
                System.out.println("Filename without extention: " + getFileName(file.getName()));
                try {
                    Class<?> clazz = Class.forName("com.application.project.myapi."+(directory.equals("")?"":directory.replace("/", ".")+".")+getFileName(file.getName()));
                    
                    //  clazz = applicationContext.getBean(clazz.getClass());
                    Method[] methods = clazz.getDeclaredMethods();
                    for(int i=0;i<methods.length;i++){
                        if(methods[i].getName().toUpperCase().contains("FLY") && !methods[i].getName().toUpperCase().contains("$")){
                            String endPoint =  switch (directory) {
                                case ""->getFileName(file.getName()).replace("Fly","");
                                default ->directory+"/"+getFileName(file.getName()).replace("Fly","");
                            };
                            String pathVariable ="";
                            System.out.println("Methods in the class: "+methods[i].getName());

                            Method classMethod= clazz.getDeclaredMethod(methods[i].getName(), ArkRequest.class);
                            if(classMethod.isAnnotationPresent(PathVariableAnnotation.class)){
                                PathVariableAnnotation annotation = classMethod.getAnnotation(PathVariableAnnotation.class);
                                if(annotation.name().length>1){
                                    for(String p:annotation.name()){
                                        log.info(getFileName(file.getName())+" pathVariables:{}", p);
                                        pathVariable = pathVariable+"/"+p;
                                    }
                                }else{
                                    log.info(getFileName(file.getName())+" pathVariables:{}", annotation.name()[0]);
                                    log.info("pathVariables:{}", annotation.name()[0]);
                                    pathVariable = pathVariable+"/"+annotation.name()[0];
                                }
                               

                            }
                            String methodName = methods[i].getName();
                            String apiType =  methodName.toUpperCase().replace("FLY","");
                            log.info("APItype:{}",apiType);
                            if(apiType.equalsIgnoreCase("GET")){
                                builder.GET(uri(endPoint,pathVariable),req->{
                                    // autowireCapableBeanFactory.createBean(PersonRepo.class, 1, true);
                                    // autowireCapableBeanFactory.autowire(PersonRepo.class, 1, false);
                                    // autowireCapableBeanFactory.initializeBean(clazz, "personRepo");
                                    // autowireCapableBeanFactory.createBean(clazz, 1, true);
                                   
                                    // autowireCapableBeanFactory.initializeBean(clazz, getFileName(file.getName()));
                                    

                                            
                                    try{
                                        Object cl =clazz.getDeclaredConstructor().newInstance();
                                        autowireCapableBeanFactory.autowireBean(cl);
                                        Mono<ServerResponse> res = (Mono<ServerResponse>) classMethod.invoke(cl,new ArkRequest(req));
                                        return res;
                                    }catch(Exception e){
                                        e.printStackTrace();

                                    }
                                    return ServerResponse.ok().body(Mono.just("Exception GET"),String.class);
                                });
                            }
                            else if(apiType.equalsIgnoreCase("POST")){
                                builder.POST(uri(endPoint,pathVariable),req->{
                                    try{
                                        Object cl =clazz.getDeclaredConstructor().newInstance();
                                        autowireCapableBeanFactory.autowireBean(cl);
                                        Mono<ServerResponse> res = (Mono<ServerResponse>) classMethod.invoke(cl,new ArkRequest(req));
                                        return res;
                                    }catch(Exception e){
                                        e.printStackTrace();

                                    }
                                    return ServerResponse.ok().body(Mono.just("Exception POST"),String.class);
                                });
                            }
                            else if(apiType.equalsIgnoreCase("PATCH")){
                                builder.PATCH(uri(endPoint,pathVariable),req->{
                                    
                                    try{
                                        Object cl =clazz.getDeclaredConstructor().newInstance();
                                        autowireCapableBeanFactory.autowireBean(cl);
                                        Mono<ServerResponse> res = (Mono<ServerResponse>) classMethod.invoke(cl,new ArkRequest(req));
                                        return res;
                                    }catch(Exception e){
                                        e.printStackTrace();

                                    }
                                    return ServerResponse.ok().body(Mono.just("Exception PUT"),String.class);
                                });
                            }
                            else if(apiType.equalsIgnoreCase("PUT")){
                                builder.PUT("/"+endPoint,req->{
                                    try{
                                        Mono<ServerResponse> res = (Mono<ServerResponse>) classMethod.invoke(clazz.getDeclaredConstructor().newInstance(),new ArkRequest(req));
                                        return res;
                                    }catch(Exception e){
                                        e.printStackTrace();

                                    }
                                    return ServerResponse.ok().body(Mono.just("Exception PUT"),String.class);
                                });
                            }

                        }

                    }
                    
                }catch (IllegalArgumentException e) { 
                    log.info("==========================>Exception", e.getMessage());
                    e.printStackTrace();
                }catch (ClassNotFoundException e) { 
                    log.info("==========================>Exception", e.getMessage());
                    e.printStackTrace();
                }
                catch (NoSuchMethodException e) {
                    log.info("==========================>Exception", e.getMessage());
                    e.printStackTrace();
                } 
    
            }
    
        }

    }

    private String uri(String endpoint,String pathVariable){
        return "/"+endpoint+(pathVariable.equals("")?"":pathVariable);

    }

    // @PostConstruct
    // public void init(){
    //     // autowireCapableBeanFactory.initializeBean(JpaRepository.class, "personRepo");
    // //    autowireCapableBeanFactory.autowire(PersonRepo.class, 0, false);
    // // autowireCapableBeanFactory.createBean(PersonRepo.class, 1, true);
    //    autowireCapableBeanFactory.initializeBean(PersonService.class, "personService");
    //    autowireCapableBeanFactory.autowire(PersonService.class, 1, false);
    //     // wire stuff here
    // }
    
}
