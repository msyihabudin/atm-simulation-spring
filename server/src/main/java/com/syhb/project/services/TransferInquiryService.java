package com.syhb.project.services;

import com.syhb.project.models.Account;
import com.syhb.project.helpers.TransactionHelper;
import com.syhb.project.repositories.AccountRepository;
import com.syhb.project.repositories.CustomerRepository;
import com.syhb.project.responses.TransferInquiryResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Map;

@Service
@Transactional
public class TransferInquiryService implements TransactionService {

    private final Logger logger = LoggerFactory.getLogger(TransferInquiryService.class);

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public TransferInquiryService(AccountRepository accountRepository, CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public String response(Map message) throws JSONException {

        logger.info("In TransferInquiryService response");

        String accountNumber = message.get(2).toString();
        String destinationAccount = message.get(62).toString();
        String responseCode;

        BigDecimal balance = new BigDecimal(0);
        Account account;

        JSONObject jsonAccount = new JSONObject();

        if (accountRepository.existsByAccountnumber(destinationAccount)) {
            account = accountRepository.findByAccountnumber(destinationAccount);

            jsonAccount.put("accountnumber", account.getAccountnumber());
            jsonAccount.put("cardnumber", account.getCardnumber());
            jsonAccount.put("customerid", customerRepository.findCustomerById(account.getCustomerid()).getName());

            responseCode = "390010";
        } else {
            responseCode = "391210";
        }

        TransactionHelper transactionHelper = new TransactionHelper(accountNumber, balance, responseCode);
        transactionHelper.setDestinationNumber(destinationAccount);
        transactionHelper.setJsonObject(jsonAccount);

        TransferInquiryResponse createResponse = new TransferInquiryResponse();

        return createResponse.process(transactionHelper);
    }

}
