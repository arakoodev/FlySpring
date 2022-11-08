package com.application.project.autoRoute;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunctions.Builder;

import com.application.project.annotation.PathVariableAnnotation;

import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class RouterFunctionConfig {

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
                                        log.info("pathVariables:{}", p);
                                        pathVariable = pathVariable+"/"+p;
                                    }
                                }else{
                                    log.info("pathVariables:{}", annotation.name()[0]);
                                    pathVariable = pathVariable+"/"+annotation.name()[0];
                                }
                               

                            }
                            String methodName = methods[i].getName();
                            String apiType =  methodName.toUpperCase().replace("FLY","");
                            log.info("APItype:{}",apiType);
                            if(apiType.equalsIgnoreCase("GET")){
                                builder.GET(uri(endPoint,pathVariable),req->{
                                    try{
                                        Mono<ServerResponse> res = (Mono<ServerResponse>) classMethod.invoke(clazz.getDeclaredConstructor().newInstance(),new ArkRequest(req));
                                        return res;
                                    }catch(Exception e){
                                        e.printStackTrace();

                                    }
                                    return ServerResponse.ok().body(Mono.just("Exception GET"),String.class);
                                });
                            }
                            else if(apiType.equalsIgnoreCase("POST")){
                                builder.POST("/"+endPoint,req->{
                                    try{
                                        Mono<ServerResponse> res = (Mono<ServerResponse>) classMethod.invoke(clazz.getDeclaredConstructor().newInstance(),new ArkRequest(req));
                                        return res;
                                    }catch(Exception e){
                                        e.printStackTrace();

                                    }
                                    return ServerResponse.ok().body(Mono.just("Exception POST"),String.class);
                                });
                            }
                            else if(apiType.equalsIgnoreCase("PATCH")){
                                builder.PATCH("/"+endPoint,req->{
                                    try{
                                        Mono<ServerResponse> res = (Mono<ServerResponse>) classMethod.invoke(clazz.getDeclaredConstructor().newInstance(),new ArkRequest(req));
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
    
}
