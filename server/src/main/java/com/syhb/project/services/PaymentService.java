package com.syhb.project.services;

import com.syhb.project.models.Account;
import com.syhb.project.models.Transaction;
import com.syhb.project.helpers.TransactionHelper;
import com.syhb.project.repositories.AccountRepository;
import com.syhb.project.repositories.TransactionRepository;
import com.syhb.project.responses.PaymentResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Map;

@Service
@Transactional
public class PaymentService implements TransactionService {

    private final Logger logger = LoggerFactory.getLogger(TransferInquiryService.class);

    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    public PaymentService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public String response(Map message) {

        logger.debug("In PaymentService response");
        String accountNumber = message.get(2).toString();
        String responseCode;

        double amount = Double.parseDouble(message.get(4).toString()) + 3000;

        JSONObject jsonAccount = new JSONObject();

        if (accountRepository.existsByAccountnumber(accountNumber)) {
            Account account = accountRepository.findByAccountnumber(accountNumber);
            if ((account.getBalance()-amount) > 50000) {
                account.setBalance(account.getBalance() - amount);

                Transaction transaction = new Transaction();
                transaction.setType("Transfer");
                transaction.setAccountnumber(account.getAccountnumber());
                transaction.setAmount(amount);
                transaction.setCredit(0);
                transaction.setDebit(amount);
                transaction.setBalance(account.getBalance());
                transactionRepository.save(transaction);

                responseCode = "180010";
            } else {
                responseCode = "185110";
            }
        } else {
            responseCode = "181210";
        }

        BigDecimal balance = new BigDecimal(accountRepository.findByAccountnumber(accountNumber).getBalance());

        TransactionHelper transactionHelper = new TransactionHelper(accountNumber, balance, responseCode);
        transactionHelper.setAccountNumber(accountNumber);
        transactionHelper.setJsonObject(jsonAccount);

        PaymentResponse createResponse = new PaymentResponse();

        return createResponse.process(transactionHelper);

    }
}
