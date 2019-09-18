package com.syhb.project.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WrongPINNumber implements CustomHandler {

    private CustomHandler nextInHandler;
    private final Logger logger = LoggerFactory.getLogger(WrongPINNumber.class);

    @Override
    public void setNextHandler(CustomHandler nextHandler) {
        nextInHandler = nextHandler;
    }

    @Override
    public void message(String code) {
        if (code.trim().toLowerCase().equals("76")) {
            logger.info("PIN yang Anda masukkan salah!");
            System.out.println("\nPIN yang Anda masukkan salah!");
        } else {
            nextInHandler.message(code);
        }
    }

}
