package com.syhb.project.controllers;

import com.syhb.project.handlers.*;
import com.syhb.project.helpers.TransactionHelper;
import com.syhb.project.helpers.TransactionHelperType;
import com.syhb.project.services.Transaction;
import com.syhb.project.services.impl.CashWithdrawalImpl;

import java.math.BigDecimal;
import java.util.Map;

public class CashWithdrawalController extends TransactionController {

    public boolean process(String accountNumber, String pin) {

        String amountMenu = "\nPILIH JUMLAH PENARIKAN\n1) 100.000\t3) 500.000\n2) 300.000\t4) 1.000.000\n" +
                "5) Masukkan jumlah lain\n6) Kembali";
        System.out.println(amountMenu);
        int idmenu = Integer.parseInt(scan.nextLine());

        BigDecimal amount;

        if (idmenu == 1)
            amount = new BigDecimal(100000);
        else if (idmenu == 2)
            amount = new BigDecimal(300000);
        else if (idmenu == 3)
            amount = new BigDecimal(500000);
        else if (idmenu == 4)
            amount = new BigDecimal(1000000);
        else if (idmenu == 5){
            System.out.println("Masukkan kelipatan 100.000: ");
            long inputAmoun = Long.parseLong(scan.nextLine());
            amount = new BigDecimal(inputAmoun);
        } else {
            return false;
        }

        TransactionHelper transactionHelper = new TransactionHelper(accountNumber, pin, TransactionHelperType.INTERNAL);
        transactionHelper.setAmount(amount);
        Transaction transaction = new CashWithdrawalImpl();
        String message = transaction.send(transactionHelper);

        if (message != null) {
            Map isomessage = unpackFromIso(message);
            String processingcode = isomessage.get(3).toString();
            String code = processingcode.trim().substring(2,4);

            System.out.println(code);

            CustomHandler handler = new TransactionSuccessful();
            CustomHandler handler1 = new NotEnoughBalance();
            CustomHandler handler2 = new AccountBlocked();
            CustomHandler handler3 = new MultipleOf();
            CustomHandler handler4 = new WrongPINNumber();

            handler.setNextHandler(handler1);
            handler1.setNextHandler(handler2);
            handler2.setNextHandler(handler3);

            if (code.trim().equals("76")) {
                handler4.message(code);
                return false;
            } else {
                handler.message(code);
            }
        }

        return true;

    }

}
