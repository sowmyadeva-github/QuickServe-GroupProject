package com.quickserve.quickserve.service;

import com.quickserve.quickserve.entity.User;
import com.quickserve.quickserve.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public User register(User user){
       user.setPassword(passwordEncoder.encode(user.getPassword()));
       return repo.save(user);
    }

    public User login(String email, String password){
        User user = repo.findByEmail(email);

        if(user != null && passwordEncoder.matches(password, user.getPassword())){
            return user;
        }
        return null;
    }
}