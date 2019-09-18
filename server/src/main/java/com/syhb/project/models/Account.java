package com.syhb.project.models;

import javax.persistence.*;

@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String accountnumber;
    private String cardnumber;
    private int customerid;
    private int bankid;
    private String pinnumber;
    private double balance;
    private String status;

    public Account() { }

    public Account(String accountnumber, String cardnumber, int customerid, int bankid, String pinnumber, double balance) {
        this.accountnumber = accountnumber;
        this.cardnumber = cardnumber;
        this.customerid = customerid;
        this.bankid = bankid;
        this.pinnumber = pinnumber;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(String accountnumber) {
        this.accountnumber = accountnumber;
    }

    public String getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }

    public int getCustomerid() {
        return customerid;
    }

    public void setCustomerid(int customerid) {
        this.customerid = customerid;
    }

    public int getBankid() {
        return bankid;
    }

    public void setBankid(int bankid) {
        this.bankid = bankid;
    }

    public String getPinnumber() {
        return pinnumber;
    }

    public void setPinnumber(String pinnumber) {
        this.pinnumber = pinnumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
