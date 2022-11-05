package com.application.project.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.application.project.config.SQLiteConfig;
import com.application.project.model.User;


import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Validated
@Slf4j
public class UserServices {
    private Connection connection;

    

    public UserServices(){
        SQLiteConfig sqLiteConfig = new SQLiteConfig();
        try {
            this.connection = sqLiteConfig.getConnection();
        } catch (ClassNotFoundException | IOException e) {
            try {connection.close();} catch (SQLException f) {f.printStackTrace();}
            e.printStackTrace();
        }
    }

    public Mono<ServerResponse> saveUser( User user) {
        String query =""" 
            Insert into Users (firstName,lastName,age,gender)
            Values(?,?,?,?)
        """;
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1,user.getFirstName());
            ps.setString(2,user.getLastName());
            ps.setString(3,user.getAge());
            ps.setString(4,user.getGender());
            if(ps.executeUpdate()==1){
                ps.close();
                return ServerResponse.ok().body(Mono.just("Saved Successfully"),User.class);
            }
        } catch (SQLException e) {
            log.info("Failed Saving Execution");
            
            e.printStackTrace();
            return ServerResponse.ok().body(Mono.just("Error Saving"),String.class);
        }finally{
            try {connection.close();} catch (SQLException e) {e.printStackTrace();}
            
        }

        return ServerResponse.ok().body(Mono.just("Error Saving"),String.class);


        
    }

    public Mono<ServerResponse> updateUser(User user, String id) {
        String query =""" 
            Update  Users set firstName=?,lastName=?,age=?,gender=? 
            Where id =?
        """;
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1,user.getFirstName());
            ps.setString(2,user.getLastName());
            ps.setString(3,user.getAge());
            ps.setString(4,user.getGender());
            ps.setInt(5,Integer.parseInt(id));
            if(ps.executeUpdate()==1){
                ps.close();
                return ServerResponse.ok().body(Mono.just("Updated Successfully"),User.class);
            }
        } catch (SQLException e) {
            log.info("Failed Updating Execution");
            
            e.printStackTrace();
            return ServerResponse.ok().body(Mono.just("Error Updating"),String.class);
        }finally{
            try {connection.close();} catch (SQLException e) {e.printStackTrace();}
            
        }

        return ServerResponse.ok().body(Mono.just("Error Saving"),String.class);
    }

    public Mono<ServerResponse> getUserById(Integer id) {
        String query =""" 
           Select * from Users Where id =?
        """;
        ResultSet resultSet =null;
        User user = new User();
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);

            resultSet = ps.executeQuery();
            while(resultSet.next()){
                user.setId(resultSet.getInt("id"));
                user.setFirstName(resultSet.getString("firstName"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getString("age"));
                user.setGender(resultSet.getString("gender"));
            }
            resultSet.close();
            return ServerResponse.ok().body(Mono.just(user),User.class);
        } catch (SQLException e) {
            log.info("Failed Query Execution");
            e.printStackTrace();
        }finally{
            try {connection.close();} catch (SQLException e) {e.printStackTrace();}
        }

        return ServerResponse.ok().body(Mono.just("Error Execution"),String.class);
    }

    public Mono<ServerResponse> deleteUser(User user, String id) {
        String query =""" 
           Delete from Users Where id =?
        """;
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, Integer.parseInt(id));

            if(ps.executeUpdate()==1){
                ps.close();
                return ServerResponse.ok().body(Mono.just("User Deleted Successfully"),User.class);
            }
            return ServerResponse.ok().body(Mono.just(user),User.class);
        } catch (SQLException e) {
            log.info("Failed Delete Query Execution");
            e.printStackTrace();
        }finally{
            try {connection.close();} catch (SQLException e) {e.printStackTrace();}
        }

        return ServerResponse.ok().body(Mono.just("Error Execution"),String.class);
    }
    
}
