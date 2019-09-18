package com.syhb.project.services;

import com.syhb.project.models.Account;
import com.syhb.project.models.Transaction;
import com.syhb.project.helpers.TransactionHelper;
import com.syhb.project.helpers.TransactionHelperType;
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

    private final Logger logger = LogManager.getLogger(TransferInquiryService.class);

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
        String transferIndicator = message.get(125).toString();
        String responseCode;
        TransactionHelperType type = TransactionHelperType.INTERNAL;

        double currentBalanceOrigin = accountRepository.findByAccountnumber(accountNumber).getBalance();
        double amount = Double.parseDouble(message.get(4).toString());

        JSONObject jsonAccount = new JSONObject();
        Account originAccount = new Account();

        if ((currentBalanceOrigin - amount) < 50000) {
            responseCode = "405110";
        } else if (transferIndicator.trim().equals("INTERNAL")) {
            type = TransactionHelperType.INTERNAL;

            Account destinationAccount = accountRepository.findByAccountnumber(destinationAccountNumber);
            double currentBalanceDestination = accountRepository.findByAccountnumber(destinationAccountNumber).getBalance();
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

            originAccount = accountRepository.findByAccountnumber(accountNumber);
            originAccount.setBalance(currentBalanceOrigin - amount);

            Transaction transactionForOrigin = new Transaction();
            transactionForOrigin.setType("Transfer");
            transactionForOrigin.setAccountnumber(originAccount.getAccountnumber());
            transactionForOrigin.setAmount(amount);
            transactionForOrigin.setCredit(0);
            transactionForOrigin.setDebit(amount);
            transactionForOrigin.setBalance(originAccount.getBalance());
            transactionRepository.save(transactionForOrigin);

            responseCode = "400010";
        } else if (transferIndicator.trim().equals("EXTERNAL")){
            type = TransactionHelperType.EXTERNAL;

            originAccount = accountRepository.findByAccountnumber(accountNumber);
            originAccount.setBalance((currentBalanceOrigin) - amount);

            Transaction transactionForOrigin = new Transaction();
            transactionForOrigin.setType("Transfer");
            transactionForOrigin.setAccountnumber(originAccount.getAccountnumber());
            transactionForOrigin.setAmount(amount);
            transactionForOrigin.setCredit(0);
            transactionForOrigin.setDebit(amount);
            transactionForOrigin.setBalance(originAccount.getBalance());
            transactionRepository.save(transactionForOrigin);

            responseCode = "400010";
        } else {
            responseCode = "401210";
        }

        BigDecimal balance = new BigDecimal(accountRepository.findByAccountnumber(accountNumber).getBalance());

        TransactionHelper transactionHelper = new TransactionHelper(accountNumber, balance, responseCode);
        transactionHelper.setAccountNumber(originAccount.getAccountnumber());
        transactionHelper.setDestinationNumber(destinationAccountNumber);
        transactionHelper.setJsonObject(jsonAccount);
        transactionHelper.setType(type);

        TransferResponse createResponse = new TransferResponse();

        return createResponse.process(transactionHelper);
    }
}
