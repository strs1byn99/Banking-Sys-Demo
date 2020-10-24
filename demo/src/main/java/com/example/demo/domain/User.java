package com.example.demo.domain;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;
import java.util.*;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String name;
    @Column(unique = true)
    public String email;
    @Column(unique = true)
    public String phoneNum;
    @ElementCollection
    List<String> address;
    @OneToMany(mappedBy = "user")
    List<Account> accounts;
	public List<Account> getAccounts() {
        return accounts;
    }
    
    // private UserDto user;
}

class UserDTO {
    long id;
    String name;
    String email;
}
