package com.htmx.thymeleaf.users;

import io.github.wimdeblauwe.hsbt.mvc.HxRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {
    @Autowired private UserService userService;

    @GetMapping("/")
    public String homepage(Model model){
        model.addAttribute("users", userService.getAll());
        return "users";
    }

    @GetMapping("/chart")
    public String chartPage(Model model){
        List<Info> infoData = userService.getInfoData();
        List<String> dates = infoData.stream().map(Info::getDate).collect(Collectors.toList());
        List<Integer> userCreated = infoData.stream().map(Info::getUserCreated).collect(Collectors.toList());
        List<Integer> userDeleted = infoData.stream().map(Info::getUserDeleted).collect(Collectors.toList());
        model.addAttribute("dates", dates);
        model.addAttribute("created", userCreated);
        model.addAttribute("deleted", userDeleted);
        return "chart";
    }

    @PostMapping("/create")
    @HxRequest
    public String saveUser(User newUser, Model model){
        User user = userService.saveUser(newUser);
        model.addAttribute("user", user);
        return "temp :: row";
    }

    @GetMapping("/user/{id}")
    @HxRequest
    public String getUserById(@PathVariable Integer id, Model model){
        User user = userService.getBankById(id);
        model.addAttribute("user", user);
        return "temp :: row";
    }

    @GetMapping("/user/{id}/edit")
    @HxRequest
    public String getUserForm(@PathVariable Integer id, Model model){
        User user = userService.getBankById(id);
        model.addAttribute("user", user);
        return "temp :: editForm";
    }

    @PutMapping("/user/{id}/edit")
    @HxRequest
    public String updateUser(@PathVariable Integer id, User user,  Model model){
        User newUser = userService.update(user);
        model.addAttribute("user", newUser);
        return "temp :: row";
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    @HxRequest
    public void deleteUser(@PathVariable Integer id){
        userService.deleteUser(id);
    }


}
