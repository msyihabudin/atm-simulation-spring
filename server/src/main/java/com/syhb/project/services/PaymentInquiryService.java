package com.syhb.project.services;

import com.syhb.project.models.Account;
import com.syhb.project.helpers.TransactionHelper;
import com.syhb.project.repositories.AccountRepository;
import com.syhb.project.responses.TransferInquiryResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Map;

public class PaymentInquiryService implements TransactionService {

    private final Logger logger = LoggerFactory.getLogger(PaymentInquiryService.class);

    private final AccountRepository accountRepository;

    public PaymentInquiryService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public String response(Map message) throws JSONException {

        logger.info("In PaymentInquiryService response");

        String accountNumber = message.get(2).toString();
        String destinationAccount = message.get(62).toString();
        double amount = Double.parseDouble(message.get(4).toString()) + 3000;
        String responseCode;

        BigDecimal balance = new BigDecimal(0);
        Account account;

        JSONObject jsonAccount = new JSONObject();

        if (accountRepository.existsByAccountnumber(destinationAccount)) {
            account = accountRepository.findByAccountnumber(destinationAccount);
            if ((account.getBalance()-amount) > 50000) {
                jsonAccount.put("accountnumber", account.getAccountnumber());
                jsonAccount.put("cardnumber", account.getCardnumber());

                responseCode = "380010";
            } else {
                responseCode = "385110";
            }
        } else {
            responseCode = "381210";
        }

        TransactionHelper transactionHelper = new TransactionHelper(accountNumber, balance, responseCode);
        transactionHelper.setDestinationNumber(destinationAccount);
        transactionHelper.setJsonObject(jsonAccount);

        TransferInquiryResponse createResponse = new TransferInquiryResponse();

        return createResponse.process(transactionHelper);
    }
}
