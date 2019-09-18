package com.syhb.project.services;

import com.syhb.project.helpers.TransactionHelper;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public interface Transaction {

    String send(TransactionHelper transactionHelper);
    Integer generateStan();

    Duration timeout = Duration.ofSeconds(10);
    ExecutorService executor = Executors.newSingleThreadExecutor();

}
