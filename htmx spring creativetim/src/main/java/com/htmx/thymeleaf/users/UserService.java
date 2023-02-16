package com.htmx.thymeleaf.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    @Autowired private UserRepo userRepo;
    @Autowired private InfoRepo infoRepo;

    public User saveUser(User user){
        Info info = getTodayInfo();
        info.setUserCreated(info.getUserCreated() + 1);
        infoRepo.save(info);
        return userRepo.save(user);
    }

    public List<User> getAll(){
        return userRepo.findAll();
    }

    public User update(User user){
        if(user.getId() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User id not found");
        }
        getBankById(user.getId());
       return userRepo.save(user);
    }

    public void deleteUser(Integer id){
        getBankById(id);
        Info info = getTodayInfo();
        info.setUserDeleted(info.getUserDeleted() + 1);
        infoRepo.save(info);
        userRepo.deleteById(id);
    }

    public List<Info> getInfoData(){
        return infoRepo.findAll();
    }

    public User getBankById(int id) {
        Optional<User> bankOptional =  userRepo.findById(id);
        if (bankOptional.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Bank not found");
        return bankOptional.get();
    }
    public Info getTodayInfo(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String today = dateFormat.format(new Date());
        return infoRepo.findByDate(today).orElse(new Info(today, 0, 0));
    }
}
