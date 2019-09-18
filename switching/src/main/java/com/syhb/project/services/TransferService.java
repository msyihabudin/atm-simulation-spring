package com.syhb.project.services;

import com.syhb.project.models.Account;
import com.syhb.project.models.Transaction;
import com.syhb.project.helpers.TransactionHelper;
import com.syhb.project.repositories.AccountRepository;
import com.syhb.project.repositories.CustomerRepository;
import com.syhb.project.repositories.TransactionRepository;
import com.syhb.project.responses.TransferResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Map;

@Service
@Transactional
public class TransferService implements TransactionService {

    private final Logger logger = LogManager.getLogger(TransferService.class);

    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    private final CustomerRepository customerRepository;

    public TransferService(AccountRepository accountRepository, TransactionRepository transactionRepository, CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public String response(Map message) throws JSONException {

        logger.info("In TransferService response");

        String accountNumber = message.get(2).toString();
        String destinationAccountNumber = message.get(62).toString();

        String responseCode;

        double currentBalanceDestination = accountRepository.findByAccountnumber(destinationAccountNumber).getBalance();
        double amount = Double.parseDouble(message.get(4).toString());

        JSONObject jsonAccount = new JSONObject();

        if (accountRepository.existsByAccountnumber(destinationAccountNumber)) {
            Account destinationAccount = accountRepository.findByAccountnumber(destinationAccountNumber);
            destinationAccount.setBalance(currentBalanceDestination + amount);
            accountRepository.save(destinationAccount);

            Transaction transactionForDestination = new Transaction();
            transactionForDestination.setType("Transferred");
            transactionForDestination.setAccountnumber(destinationAccount.getAccountnumber());
            transactionForDestination.setAmount(amount);
            transactionForDestination.setCredit(amount);
            transactionForDestination.setDebit(0);
            transactionForDestination.setBalance(destinationAccount.getBalance());
            transactionRepository.save(transactionForDestination);

            jsonAccount.put("accountnumber", destinationAccount.getAccountnumber());
            jsonAccount.put("cardnumber", destinationAccount.getCardnumber());
            jsonAccount.put("customerid", customerRepository.findCustomerById(destinationAccount.getCustomerid()).getName());

            responseCode = "400010";
        } else {
            responseCode = "401210";
        }

        TransactionHelper transactionHelper = new TransactionHelper(accountNumber, new BigDecimal(0), responseCode);
        transactionHelper.setAccountNumber(accountNumber);
        transactionHelper.setDestinationNumber(destinationAccountNumber);
        transactionHelper.setJsonObject(jsonAccount);

        TransferResponse createResponse = new TransferResponse();

        return createResponse.process(transactionHelper);
    }
}
