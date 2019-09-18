package com.syhb.project.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionSuccessful implements CustomHandler {

    private CustomHandler nextInHandler;
    private final Logger logger = LoggerFactory.getLogger(TransactionSuccessful.class);

    @Override
    public void setNextHandler(CustomHandler nextHandler) {
        nextInHandler = nextHandler;
    }

    @Override
    public void message(String code) {
        if (code.trim().toLowerCase().equals("00")) {
            logger.info("Transaksi sukses!");
            System.out.println("Transaksi sukses!");
        } else
            nextInHandler.message(code);
    }

}
