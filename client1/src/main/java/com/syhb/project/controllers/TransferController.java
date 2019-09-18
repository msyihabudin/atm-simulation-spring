package com.syhb.project.controllers;

import com.syhb.project.handlers.CustomHandler;
import com.syhb.project.handlers.NotEnoughBalance;
import com.syhb.project.handlers.TransactionFailed;
import com.syhb.project.handlers.TransactionSuccessful;
import com.syhb.project.helpers.TransactionHelper;
import com.syhb.project.helpers.TransactionHelperType;
import com.syhb.project.services.Transaction;
import com.syhb.project.services.impl.TransferImpl;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

class TransferController extends TransactionController {

    void process(TransactionHelper transactionHelper) {

        try {
            Transaction transaction = new TransferImpl();
            String message = transaction.send(transactionHelper);

            if (message != null) {
                Map isoMessage;
                String accountInfo;

                isoMessage = unpackFromIso(message);
                accountInfo = isoMessage.get(62).toString();
                String processingcode = isoMessage.get(3).toString();

                CustomHandler handler1 = new NotEnoughBalance();
                CustomHandler handler2 = new TransactionFailed();
                CustomHandler handler3 = new TransactionSuccessful();

                handler1.setNextHandler(handler2);

                String code = processingcode.trim().substring(2,4);
                System.out.println(transactionHelper.getType()+" "+ code);

                if (code.trim().equals("00")) {
                    handler3.message(code);

                    JSONObject jsonObject;

                    if (transactionHelper.getType() == TransactionHelperType.EXTERNALToINTERNAL) {
                        Transaction transaction1 = new TransferImpl();
                        message = transaction1.send(transactionHelper);
                        isoMessage = unpackFromIso(message);
                        accountInfo = isoMessage.get(62).toString();
                        jsonObject = new JSONObject(accountInfo);
                    } else
                        jsonObject = new JSONObject(accountInfo);

                    System.out.println("\nPT Bank Asikin");
                    System.out.println("Tanggal : "+ new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()));
                    System.out.println("TRANSFER ATM\n");
                    System.out.println("Dari Rekening       : "+ transactionHelper.getAccountNumber());
                    System.out.println("Ke Rekening         : "+ jsonObject.get("accountnumber"));
                    System.out.println("Nama                : "+ jsonObject.get("customerid"));

                    String result;
                    if (transactionHelper.getType() == TransactionHelperType.EXTERNALToINTERNAL) {
                        System.out.println("Biaya Administrasi  : RP 6,500");
                        result = String.valueOf(Long.parseLong(transactionHelper.getAmount().toString()));
                    } else {
                        result = transactionHelper.getAmount().toString();
                    }

                    System.out.println("Jumlah              : RP "+ currencyFormat(result));
                    System.out.println();
                    System.out.println("Terima Kasih");

                } else {
                    handler1.message(code);
                }
            }
        } catch (JSONException e) {
            logger.debug("In TransferController process. Message: "+ e.getMessage());
        }

    }

}
