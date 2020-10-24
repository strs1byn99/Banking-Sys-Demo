package com.example.demo.controllers;

import com.example.demo.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import com.example.demo.domain.User;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserService service;

    @PostMapping
    public ResponseEntity<Object> addUser(@RequestBody User user, HttpServletRequest req) throws URISyntaxException {
        long id = service.addUser(user);
        if (id == -1) {
            return ResponseEntity.badRequest().build(); // Email already
        }
        return ResponseEntity.created(new URI(req.getRequestURL() + "/" + id)).build();
    }

    @PostMapping("/delete")
    public ResponseEntity<Object> deleteUser(@RequestBody User user, HttpServletRequest req) throws URISyntaxException {
        if (service.deleteUserByEmail(user.email)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(403).build(); 
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) {
        Optional<User> user = service.getUser(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/_email")
    public ResponseEntity<User> getUser(@RequestBody User u) {
        Optional<User> user = service.getUserByEmail(u.email);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
