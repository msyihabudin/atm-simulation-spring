package com.syhb.project.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultipleOf implements CustomHandler {

    private CustomHandler nextInHandler;
    private final Logger logger = LoggerFactory.getLogger(MultipleOf.class);

    @Override
    public void setNextHandler(CustomHandler nextHandler) {
        nextInHandler = nextHandler;
    }

    @Override
    public void message(String code) {
        if (code.trim().toLowerCase().equals("12")) {
            logger.info("Jumlah harus kelipatan 100.000!");
            System.out.println("\nJumlah harus kelipatan 100.000!");
        } else
            nextInHandler.message(code);
    }

}
