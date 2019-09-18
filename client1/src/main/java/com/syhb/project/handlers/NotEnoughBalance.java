package com.syhb.project.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotEnoughBalance implements CustomHandler {

    private CustomHandler nextInHandler;
    private final Logger logger = LoggerFactory.getLogger(NotEnoughBalance.class);

    @Override
    public void setNextHandler(CustomHandler nextHandler) {
        nextInHandler = nextHandler;
    }

    @Override
    public void message(String code) {
        if (code.trim().toLowerCase().equals("51")) {
            logger.info("Saldo Anda tidak mencukupi!");
            System.out.println("\nSaldo Anda tidak mencukupi!");
        } else
            nextInHandler.message(code);
    }
}
