package com.example.demo.services;

import com.example.demo.domain.Account;
import com.example.demo.domain.User;
import com.example.demo.repositories.AccountRepository;
import com.example.demo.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    
    @Autowired 
    UserRepository UserRepo;

    @Autowired
    AccountRepository AccountRepo;

    public long addAccount(long userId, Account account) {
        Optional<User> user = UserRepo.findById(userId);
        if (!user.isPresent()) {
            return -1;
        }
        // user.getAccounts().add(account);
        account.setUser(user.get());
        return AccountRepo.save(account).getId();
    }

    public int deleteAccount(long userId, long AccountNum) {
        Optional<User> user = UserRepo.findById(userId);
        if (user.isPresent()) {
            List<Account> list = user.get().getAccounts();
            Optional<Account> account = AccountRepo.findById(AccountNum);
            if (account.isPresent() && list.contains(account.get())) {
                if (account.get().getBalance() == 0) {
                    AccountRepo.deleteById(AccountNum);
                    return 0;
                } else {
                    return 1; 
                }
            }
        }
        return -1;
    }

    public Optional<Account> getAccount(long accountId) {
        Optional<Account> account = AccountRepo.findById(accountId);
        return account;
    }

    public Optional<Account> changeBalance(long userId, long accountId, double value, int flag) {
        Optional<User> user = UserRepo.findById(userId);
        if (user.isPresent()) {
            List<Account> list = user.get().getAccounts();
            Optional<Account> account = AccountRepo.findById(accountId);
            if (account.isPresent() && list.contains(account.get())) {
                Account a = account.get();
                if (flag == 0)
                    a.setBalance(a.getBalance() + value);
                else
                    a.setBalance(a.getBalance() - value);
                AccountRepo.save(a);
            }
        }
        return AccountRepo.findById(accountId);
    }

    public Optional<Account> transfer(long userId, long accountId, Account toAccount, double value) {
        Optional<User> user = UserRepo.findById(userId);
        if (user.isPresent()) {
            List<Account> list = user.get().getAccounts();
            Optional<Account> account = AccountRepo.findById(accountId);
            if (account.isPresent() && list.contains(account.get())) {
                Account from = account.get();
                from.setBalance(from.getBalance() - value);
                toAccount.setBalance(toAccount.getBalance() + value);
                AccountRepo.save(from);
                AccountRepo.save(toAccount);
            }
        }
        return AccountRepo.findById(accountId);
    }

}
