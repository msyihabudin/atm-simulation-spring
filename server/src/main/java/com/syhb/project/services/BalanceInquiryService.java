package com.syhb.project.services;

import com.syhb.project.helpers.TransactionHelper;
import com.syhb.project.repositories.AccountRepository;
import com.syhb.project.responses.BalanceInquiryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Map;

@Service
@Transactional
public class BalanceInquiryService implements TransactionService {

    private final Logger logger = LoggerFactory.getLogger(BalanceInquiryService.class);

    @Autowired
    private final AccountRepository accountRepository;

    public BalanceInquiryService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public String response(Map message) {

        logger.info("In BalanceInquiryService response");

        String accountNumber = message.get(2).toString();
        String pinNumber = message.get(52).toString();
        String responseCode;

        BigDecimal balance = new BigDecimal(0);

        if (!accountRepository.findByAccountnumber(accountNumber).getPinnumber().trim().equals(pinNumber)) {
            responseCode = "307600";
        } else if(accountRepository.findByAccountnumber(accountNumber).getStatus().trim().equals("inactive")) {
            responseCode = "307800";
        } else {
            balance = new BigDecimal(accountRepository.findByAccountnumber(accountNumber).getBalance());
            responseCode = "300000";
        }

        TransactionHelper transactionHelper = new TransactionHelper(accountNumber, balance, responseCode);
        BalanceInquiryResponse createResponse = new BalanceInquiryResponse();

        return createResponse.process(transactionHelper);
    }

}
