package com.syhb.project.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String type;

    @Basic(optional = false)
    @Column(name = "date", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    private String accountnumber;

    private double amount;

    private double credit;

    private double debit;

    private double balance;

    public Transaction() {

    }

    public Transaction(String type, String accountnumber, double amount, double credit, double debit, double balance) {
        this.type = type;
        this.accountnumber = accountnumber;
        this.amount = amount;
        this.credit = credit;
        this.debit = debit;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }

    public String getAccountnumber() {
        return accountnumber;
    }

    public double getAmount() {
        return amount;
    }

    public double getCredit() {
        return credit;
    }

    public double getDebit() {
        return debit;
    }

    public double getBalance() {
        return balance;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAccountnumber(String accountnumber) {
        this.accountnumber = accountnumber;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public void setDebit(double debit) {
        this.debit = debit;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
