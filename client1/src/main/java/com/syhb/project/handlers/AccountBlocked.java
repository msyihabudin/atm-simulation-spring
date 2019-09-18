package com.syhb.project.handlers;

import com.syhb.project.services.impl.PaymentImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountBlocked implements CustomHandler {

    private CustomHandler nextInHandler;
    private final Logger logger = LoggerFactory.getLogger(AccountBlocked.class);

    @Override
    public void setNextHandler(CustomHandler nextHandler) {
        nextInHandler = nextHandler;
    }

    @Override
    public void message(String code) {
        if (code.trim().toLowerCase().equals("78")) {
            logger.info("Rekening Anda diblokir!");
            System.out.println("\nRekening Anda diblokir!");
        } else
            nextInHandler.message(code);
    }

}
