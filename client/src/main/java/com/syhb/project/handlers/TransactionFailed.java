package com.syhb.project.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionFailed implements CustomHandler {

    private CustomHandler nextInHandler;
    private final Logger logger = LoggerFactory.getLogger(TransactionFailed.class);

    @Override
    public void setNextHandler(CustomHandler nextHandler) {
        nextInHandler = nextHandler;
    }

    @Override
    public void message(String code) {
        if (code.trim().toLowerCase().equals("12")) {
            logger.info("Transaksi gagal!");
            System.out.println("\nTransaksi gagal!");
        } else
            nextInHandler.message(code);
    }
}
