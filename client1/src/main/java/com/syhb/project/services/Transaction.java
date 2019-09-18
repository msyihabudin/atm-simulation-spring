package com.syhb.project.services;

import com.syhb.project.helpers.TransactionHelper;

public interface Transaction {

    String send(TransactionHelper transactionHelper);
    Integer generateStan();

}
