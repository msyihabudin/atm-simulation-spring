package com.syhb.project.helpers;

import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

public class TransactionHelper {

    private String cardNumber;
    private String pinNumber;
    private String accountNumber;
    private String destinationNumber;
    private BigDecimal amount;
    private TransactionHelperType type;

    @Value("${server.port}")
    private String port;

    public TransactionHelper(String accountNumber, String pinNumber, TransactionHelperType type) {
        this.accountNumber = accountNumber;
        this.pinNumber = pinNumber;
        this.type = type;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getPinNumber() {
        return pinNumber;
    }

    public void setPinNumber(String pinNumber) {
        this.pinNumber = pinNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getDestinationNumber() {
        return destinationNumber;
    }

    public void setDestinationNumber(String destinationNumber) {
        this.destinationNumber = destinationNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionHelperType getType() {
        return type;
    }

    public TransactionHelper setType(TransactionHelperType type) {
        this.type = type;
        return this;
    }

    public String getPort() {
        return port;
    }
}
