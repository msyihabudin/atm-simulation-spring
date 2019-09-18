package com.syhb.project.controllers;

import com.syhb.project.handlers.*;
import com.syhb.project.helpers.TransactionHelper;
import com.syhb.project.helpers.TransactionHelperType;
import com.syhb.project.services.Transaction;
import com.syhb.project.services.impl.PaymentInquiryImpl;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Map;

public class PaymentInquiryController extends TransactionController {

    public boolean process(String accountNumber, String pin, int idtransaction) {

        if (idtransaction == 4) {
            return forPayment(accountNumber, pin);
        } else {
            return forPurchase(accountNumber, pin);
        }

    }

    private boolean forPayment(String accountNumber, String pin) {
        try {

            System.out.println("\nPILIH TRANSAKSI PEMBAYARAN\n1) Listrik/PLN\n2) Kembali");
            int idPayment = Integer.parseInt(scan.nextLine());

            String idPelanggan = "";

            if (idPayment == 1) {
                System.out.println("\nMasukkan Nomor ID Pelanggan/IDPEL : ");
                idPelanggan = scan.nextLine();
            }

            TransactionHelper transactionHelper = new TransactionHelper(accountNumber, pin, TransactionHelperType.EXTERNAL);
            transactionHelper.setAmount(new BigDecimal(0));
            transactionHelper.setAccountNumber(accountNumber);
            transactionHelper.setDestinationNumber("2101"+idPelanggan);
            Transaction transaction = new PaymentInquiryImpl();
            String message = transaction.send(transactionHelper);

            if (message != null) {
                Map isoMessage = unpackFromIso(message);

                String processingCode = isoMessage.get(3).toString();
                String accountInfo = isoMessage.get(62).toString();

                CustomHandler handler1 = new AccountBlocked();
                CustomHandler handler2 = new WrongPINNumber();
                CustomHandler handler3 = new TransactionFailed();

                handler1.setNextHandler(handler2);
                handler2.setNextHandler(handler3);

                String code = processingCode.trim().substring(2,4);

                if (code.equals("00")) {
                    JSONObject jsonObject = new JSONObject(accountInfo);
                    System.out.println("ID Pelanggan       : "+ jsonObject.get("idPelanggan"));
                    System.out.println("Jumlah Tagihan     : RP "+ currencyFormat(jsonObject.get("jumlahTagihan").toString()));
                    System.out.println("Biaya Administrasi : RP 3.000");
                    System.out.println("Total Pembayaran   : RP "+ currencyFormat(String.valueOf(Long.parseLong(jsonObject.get("jumlahTagihan").toString()) + 3000)));
                    System.out.println("Konfirmasi Pembayaran? \n1) Ya\n2) Tidak");
                    int confirm = Integer.parseInt(scan.nextLine());

                    if (confirm == 1) {
                        transactionHelper.setAmount(new BigDecimal(Integer.parseInt(jsonObject.get("jumlahTagihan").toString())));
                        PaymentController paymentController = new PaymentController();
                        paymentController.forPayment(transactionHelper);
                    }
                } else {
                    handler1.message(code);
                    return false;
                }
            }

        } catch (JSONException e) {
            logger.debug("In TransactionController forPayment. Message : "+ e.getMessage());
        }

        return true;

    }

    private boolean forPurchase(String accountNumber, String pin) {

        try {
            System.out.println("\nPILIH TRANSAKSI PEMBELIAN\n1) Pulsa\n2) Kembali");
            int idPurchase = Integer.parseInt(scan.nextLine());

            String handphoneNumber = "";
            String productCode = "";
            BigDecimal amount = new BigDecimal(0);

            if (idPurchase == 1) {
                System.out.println("\nPILIH OPERATOR SELULER\n" +
                        "1) Telkomsel\t2) Indosat\n" +
                        "3) XL/Axis\t\t4) Kembali");
                int idSeluler = Integer.parseInt(scan.nextLine());
                if (idSeluler == 1)
                    productCode = "1131";
                else if (idSeluler == 2)
                    productCode = "1151";
                else if (idSeluler == 3)
                    productCode = "1121";
                else
                    return true;

                System.out.println("\nPILIH NOMINAL\n1) 25.000\t\t2) 50.000\n3) 75.000\t\t4) 100.000\n" +
                        "5) 200.000\t\t6) 500.000\n7) 1.000.000\t8) Keluar");
                int idnominal = Integer.parseInt(scan.nextLine());

                if (idnominal == 1)
                    amount = new BigDecimal(25000);
                else if (idnominal == 2)
                    amount = new BigDecimal(50000);
                else if (idnominal == 3)
                    amount = new BigDecimal(75000);
                else if (idnominal == 4)
                    amount = new BigDecimal(100000);
                else if (idnominal == 5)
                    amount = new BigDecimal(200000);
                else if (idnominal == 6)
                    amount = new BigDecimal(500000);
                else if (idnominal == 7)
                    amount = new BigDecimal(1000000);
                else
                    return false;

                System.out.println("\nMasukkan Nomor Handphone Anda : ");
                handphoneNumber = scan.nextLine();
            }

            TransactionHelper transactionHelper = new TransactionHelper(accountNumber, pin, TransactionHelperType.EXTERNAL);
            transactionHelper.setAmount(amount);
            transactionHelper.setAccountNumber(accountNumber);
            transactionHelper.setDestinationNumber(productCode+handphoneNumber);

            System.out.println("Nomor Handphone    : "+ handphoneNumber);
            System.out.println("Nominal Pulsa      : RP "+ currencyFormat(String.valueOf(amount)));
            System.out.println("Biaya Administrasi : RP 2.000");
            System.out.println("Total Pembelian    : RP "+ currencyFormat(String.valueOf(Long.parseLong(amount.toString())+2000)));
            System.out.println("Konfirmasi Pembelian? \n1) Ya\n2) Tidak");
            int confirm = Integer.parseInt(scan.nextLine());

            if (confirm == 1) {
                PaymentController paymentController = new PaymentController();
                paymentController.forPurchase(transactionHelper);
            }
        } catch (Exception e) {
            logger.debug("In TransactionController forPurchase. Message: "+ e.getMessage());
        }

        return true;

    }

}
