package com.example.demo.services;

import java.util.Optional;

import com.example.demo.domain.User;
import com.example.demo.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired 
    UserRepository repo;

    public long addUser(User user) {
        Optional<User> u = repo.findByEmail(user.email);
        if (u.isPresent()) {
            return -1;
        }
        long id = repo.save(user).getId();
        return id;
    }

    public Optional<User> getUser(long id) {
        Optional<User> user = repo.findById(id);
        return user;
    }

    public Optional<User> getUserByEmail(String email){
        Optional<User> user = repo.findByEmail(email);
        return user;
    }

    public boolean deleteUserByEmail(String email) {
        Optional<User> user = repo.findByEmail(email);
        if (user.isPresent()) {
            if (user.get().getAccounts().size() != 0) {
                return false;
            }
            repo.deleteById(user.get().getId());
            return true;
        }
        return false;
    }
}
