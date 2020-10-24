package com.example.demo.controllers;

import com.example.demo.services.AccountService;
import com.example.demo.services.UserService;

import com.example.demo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;

import com.example.demo.domain.Account;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/users/{userId}/accounts")
public class AccountController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private AccountService service;

    @PostMapping
    public ResponseEntity<Object> addAccount(@PathVariable(value="userId") long userId, HttpServletRequest req) throws URISyntaxException{
        Account account = new Account();
        long id = service.addAccount(userId, account);
        if (id == -1) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.created(new URI(req.getRequestURI() + "/" + id)).build();
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<Object> getAccount(@PathVariable(value="userId") long userId, @PathVariable(value="accountId") long accountId, HttpServletRequest req) throws URISyntaxException {
        Optional<User> user = userService.getUser(userId);
        if (user.isPresent()) {
            List<Account> list = user.get().getAccounts();
            Optional<Account> account = service.getAccount(accountId);
            if (account.isPresent()) {
                if (list.contains(account.get()))
                    return ResponseEntity.ok(account.get());
            } 
        } 
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{accountId}/delete")
    public ResponseEntity<Object> deleteUser(@PathVariable(value="userId") long userId, @PathVariable(value="accountId") long accountId, HttpServletRequest req) throws URISyntaxException {
        int flag = service.deleteAccount(userId, accountId);
        if (flag == 0) {
            return ResponseEntity.ok().build();
        } else if (flag == 1) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.notFound().build(); 
    }
    
    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<Object> deposit(
        @RequestBody Account data,
        @PathVariable(value="userId") long userId, 
        @PathVariable(value="accountId") long accountId, 
        HttpServletRequest req) throws URISyntaxException {
        Optional<Account> account = service.changeBalance(userId, accountId, data.balance, 0);
        if (account.isPresent())
            return ResponseEntity.ok(account.get());
        else 
            return ResponseEntity.status(404).build();
    } 

    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<Object> withdraw(
        @RequestBody Account data,
        @PathVariable(value="userId") long userId, 
        @PathVariable(value="accountId") long accountId, 
        HttpServletRequest req) throws URISyntaxException {
        Optional<Account> account = service.changeBalance(userId, accountId, data.balance, 1);
        if (account.isPresent())
            return ResponseEntity.ok(account.get());
        else 
            return ResponseEntity.status(404).build();
    } 

    @PostMapping("/{accountId}/transfer")
    public ResponseEntity<Object> transfer(
        @RequestBody Account data,
        @PathVariable(value="userId") long userId,
        @PathVariable(value="accountId") long accountId,
        HttpServletRequest req) throws URISyntaxException {
            Optional<Account> toAccount = service.getAccount(data.accountNum);
            if (toAccount.isPresent()) {
                Optional<Account> res = service.transfer(userId, accountId, toAccount.get(), data.balance);
                if (res.isEmpty()) {
                    return ResponseEntity.notFound().build();
                }
                return ResponseEntity.ok(res); 
            }
            return ResponseEntity.notFound().build();
        }
}
