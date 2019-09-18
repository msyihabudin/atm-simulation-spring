package com.syhb.project.controllers;

import com.syhb.project.handlers.CustomHandler;
import com.syhb.project.handlers.NotEnoughBalance;
import com.syhb.project.handlers.TransactionFailed;
import com.syhb.project.handlers.TransactionSuccessful;
import com.syhb.project.helpers.TransactionHelper;
import com.syhb.project.helpers.TransactionHelperType;
import com.syhb.project.services.Transaction;
import com.syhb.project.services.impl.PaymentImpl;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

class PaymentController extends TransactionController {

    void forPayment(TransactionHelper transactionHelper) {

        try {
            Transaction transaction = new PaymentImpl();
            String message = transaction.send(transactionHelper);

            if (message != null) {
                Map isoMessage;
                String accountInfo;
                isoMessage = unpackFromIso(message);
                accountInfo = isoMessage.get(62).toString();
                String processingCode = isoMessage.get(3).toString();

                CustomHandler handler1 = new NotEnoughBalance();
                CustomHandler handler2 = new TransactionFailed();
                CustomHandler handler3 = new TransactionSuccessful();

                handler1.setNextHandler(handler2);

                String code = processingCode.trim().substring(2,4);

                if (code.trim().equals("00")) {
                    handler3.message(code);

                    JSONObject jsonObject;
                    if (transactionHelper.getType() == TransactionHelperType.EXTERNALToINTERNAL) {
                        message = transaction.send(transactionHelper);
                        isoMessage = unpackFromIso(message);
                        accountInfo = isoMessage.get(62).toString();
                        jsonObject = new JSONObject(accountInfo);
                    } else {
                        jsonObject = new JSONObject(accountInfo);
                    }

                    System.out.println("Tanggal : "+ new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()));
                    System.out.println("PEMBAYARAN PLN\n");
                    System.out.println("ID Pelanggan       : "+ jsonObject.get("idPelanggan"));
                    System.out.println("Jumlah Tagihan     : RP "+ currencyFormat(jsonObject.get("jumlahTagihan").toString()));
                    System.out.println("Biaya Administrasi : RP 3.000");
                    System.out.println("Total Pembayaran   : RP "+ currencyFormat(String.valueOf(Long.parseLong(jsonObject.get("jumlahTagihan").toString()) + 3000)));
                    System.out.println();
                    System.out.println("Terima Kasih");

                } else {
                    handler1.message(code);
                }
            }
        } catch (JSONException e) {
            logger.debug("In PaymentController forPayment. Message: "+ e.getMessage());
        }

    }

    void forPurchase(TransactionHelper transactionHelper) {

        try {
            Transaction transaction = new PaymentImpl();
            String message = transaction.send(transactionHelper);

            if (message != null) {
                Map isoMessage = unpackFromIso(message);
                String accountInfo = isoMessage.get(62).toString();
                String processingCode = isoMessage.get(3).toString();

                CustomHandler handler1 = new NotEnoughBalance();
                CustomHandler handler2 = new TransactionFailed();
                CustomHandler handler3 = new TransactionSuccessful();

                handler1.setNextHandler(handler2);

                String code = processingCode.trim().substring(2,4);

                if (code.trim().equals("00")) {
                    handler3.message(code);

                    JSONObject jsonObject;
                    if (transactionHelper.getType() == TransactionHelperType.EXTERNALToINTERNAL) {
                        message = transaction.send(transactionHelper);
                        isoMessage = unpackFromIso(message);
                        accountInfo = isoMessage.get(62).toString();
                        jsonObject = new JSONObject(accountInfo);
                    } else {
                        jsonObject = new JSONObject(accountInfo);
                    }

                    System.out.println("Tanggal : "+ new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()));
                    System.out.println("PEMBELIAN PULSA\n");
                    System.out.println("Nomor Handphone    : "+ jsonObject.get("noHandphone").toString());
                    System.out.println("Nominal Pulsa      : RP "+ currencyFormat(jsonObject.get("nominalPulsa").toString()));
                    System.out.println("Biaya Administrasi : RP "+ currencyFormat(jsonObject.get("biayaAdministrasi").toString()));
                    System.out.println("Total Pembayaran   : RP "+ currencyFormat(String.valueOf(Long.parseLong(jsonObject.get("nominalPulsa").toString()) + 2000)));
                    System.out.println();
                    System.out.println("Terima Kasih");

                } else {
                    handler1.message(code);
                }
            }
        } catch (JSONException e) {
            logger.debug("In PaymentController forPurchase. Message: "+ e.getMessage());
        }

    }

}
