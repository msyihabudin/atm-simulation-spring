package com.syhb.project;

import com.syhb.project.controllers.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class MainApp {
    private String accountNumber;
    private String pin;
    private boolean isClose;
    private Scanner scan = new Scanner(System.in);

    private final Logger logger = LoggerFactory.getLogger(MainApp.class);

    public MainApp(String accountNumber, String pin, boolean close) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.isClose = close;
    }

    public boolean isClose() {
        return isClose;
    }

    public void main() {
        try {
            String menu = "\n-- PT BANK A --\n1. Informasi Saldo\t2. Tarik Tunai\n3. Transfer\t\t\t4. Pembayaran\n5. Pembelian" +
                    "\t\t6. Exit";
            boolean flag = true;
            do {
                System.out.println(menu);
                System.out.print("Pilih Transaksi: ");
                int idtransaction = Integer.parseInt(scan.nextLine());

                switch (idtransaction) {
                    case 1:
                        BalanceInquiryController balanceInquiryController = new BalanceInquiryController();
                        flag = balanceInquiryController.process(accountNumber, pin);
                        break;
                    case 2:
                        CashWithdrawalController cashWithdrawalController = new CashWithdrawalController();
                        flag = cashWithdrawalController.process(accountNumber, pin);
                        break;
                    case 3:
                        TransferInquiryController transferInquiryController = new TransferInquiryController();
                        flag = transferInquiryController.process(accountNumber, pin);
                        break;
                    case 4: case 5:
                        PaymentInquiryController paymentInquiryController = new PaymentInquiryController();
                        flag = paymentInquiryController.process(accountNumber, pin, idtransaction);
                        break;
                    case 6:
                        setClose();
                        flag = false;
                        break;
                    default:
                        break;
                }
            } while (flag);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void setClose() {
        isClose = true;
    }

}
