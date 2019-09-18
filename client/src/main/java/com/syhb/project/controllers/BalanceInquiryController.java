package com.syhb.project.controllers;

import com.syhb.project.handlers.AccountBlocked;
import com.syhb.project.handlers.CustomHandler;
import com.syhb.project.handlers.WrongPINNumber;
import com.syhb.project.helpers.TransactionHelper;
import com.syhb.project.helpers.TransactionHelperType;
import com.syhb.project.services.Transaction;
import com.syhb.project.services.impl.BalanceInquiryImpl;

import java.math.BigDecimal;
import java.util.Map;

public class BalanceInquiryController extends TransactionController {

    public boolean process(String accountNumber, String pin) {

        TransactionHelper transactionHelper = new TransactionHelper(accountNumber, pin, TransactionHelperType.INTERNAL);
        transactionHelper.setAmount(new BigDecimal(0));
        Transaction transaction = new BalanceInquiryImpl();
        String message = transaction.send(transactionHelper);

        if (message != null) {
            Map isomessage = unpackFromIso(message);
            String processingcode = isomessage.get(3).toString();
            String code = processingcode.trim().substring(2,4);

            CustomHandler handler1 = new AccountBlocked();
            CustomHandler handler2 = new WrongPINNumber();

            handler1.setNextHandler(handler2);
            if (code.equals("00")) {
                System.out.println("\nSaldo Anda: RP " + currencyFormat(removeLeadZero(isomessage.get(4).toString())));
            } else {
                handler1.message(code);
                return false;
            }
        } else {
            System.out.println(message);
        }

        return true;

    }

}
