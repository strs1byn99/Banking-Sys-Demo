package com.example.demo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.Data;

@Entity
@Data
@JsonIgnoreProperties("user")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long accountNum;

    public double balance = 0;

    @ManyToOne
    @JoinColumn(name="id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

	public long getId() {
		return accountNum;
    }

    public double getBalance() {
        return balance;
    }
    
    public void setBalance(double value) {
        balance = value;
    }

}
