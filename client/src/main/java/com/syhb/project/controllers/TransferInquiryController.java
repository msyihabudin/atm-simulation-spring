package com.syhb.project.controllers;

import com.syhb.project.handlers.*;
import com.syhb.project.helpers.TransactionHelper;
import com.syhb.project.helpers.TransactionHelperType;
import com.syhb.project.services.Transaction;
import com.syhb.project.services.impl.TransferInquiryImpl;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Map;

public class TransferInquiryController extends TransactionController {

    public boolean process(String accountNumber, String pin) {

        try {
            System.out.println("\n\nPILIH BANK TUJUAN\n1) Transfer ke Bank Asikin\t\t2) Transfer ke Bank lain\n3) Kembali");
            int idTransfer = Integer.parseInt(scan.nextLine());
            String destinationAccount = "";
            BigDecimal amount;
            TransactionHelperType type;
            if (idTransfer == 1) {
                System.out.println("\nMasukkan Nomor Rekening  : ");
                destinationAccount = scan.nextLine();
                System.out.println("\nMasukkan Jumlah Transfer : ");
                amount = new BigDecimal(Integer.parseInt(scan.nextLine()));
                type = TransactionHelperType.INTERNAL;
            } else if (idTransfer == 2) {
                System.out.println("\nMasukkan Kode Bank + Nomor Rekening  : ");
                destinationAccount = scan.nextLine();
                System.out.println("\nMasukkan Jumlah Transfer : ");
                amount = new BigDecimal(Integer.parseInt(scan.nextLine())+6500);
                type = TransactionHelperType.EXTERNAL;
            } else {
                return true;
            }

            TransactionHelper transactionHelper = new TransactionHelper(accountNumber, pin, type);
            transactionHelper.setAccountNumber(accountNumber);
            transactionHelper.setAmount(amount);
            if (type == TransactionHelperType.INTERNAL) {
                transactionHelper.setDestinationNumber(destinationAccount);
            } else {
                transactionHelper.setDestinationNumber(destinationAccount.trim().substring(1));
            }

            transactionHelper.setType(type);
            Transaction transaction = new TransferInquiryImpl();
            String message = transaction.send(transactionHelper);

            if (message != null) {
                Map isomessage = unpackFromIso(message);

                String processingcode = isomessage.get(3).toString();
                String accountInfo = isomessage.get(62).toString();

                CustomHandler handler1 = new AccountBlocked();
                CustomHandler handler2 = new WrongPINNumber();
                CustomHandler handler3 = new TransactionFailed();

                handler1.setNextHandler(handler2);
                handler2.setNextHandler(handler3);

                String code = processingcode.trim().substring(2,4);

                if (code.equals("00")) {
                    JSONObject jsonObject = new JSONObject(accountInfo);
                    if (type == TransactionHelperType.EXTERNAL) {
                        System.out.println("Nama Bank      : "+ jsonObject.get("bank"));

                    }
                    System.out.println("Nomor Rekening : "+ jsonObject.get("accountnumber"));
                    System.out.println("Atas Nama      : "+ jsonObject.get("customerid"));
                    System.out.println("Jumlah         : RP "+ currencyFormat(transactionHelper.getAmount().toString()));
                    System.out.println("Konfirmasi Transfer? \n1) Ya\n2) Tidak");
                    int confirm = Integer.parseInt(scan.nextLine());

                    if (confirm == 1) {
                        TransferController transferController = new TransferController();
                        transferController.process(transactionHelper);
                    }
                } else {
                    handler1.message(code);
                    return false;
                }
            }
        } catch (JSONException e) {
            logger.debug("In TransferInquiryController process. Message: "+ e.getMessage());
        }

        return true;

    }

}
