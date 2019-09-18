package com.syhb.project.services;

import com.syhb.project.models.Account;
import com.syhb.project.models.Transaction;
import com.syhb.project.helpers.TransactionHelper;
import com.syhb.project.repositories.AccountRepository;
import com.syhb.project.repositories.TransactionRepository;
import com.syhb.project.responses.CashWithdrawalResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Map;

@Service
@Transactional
public class CashWithdrawalService implements TransactionService {

    private final Logger logger = LoggerFactory.getLogger(CashWithdrawalService.class);

    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    public CashWithdrawalService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public String response(Map message) {

        logger.info("In CashWithdrawalService response");

        String accountNumber = message.get(2).toString();
        String pinNumber = message.get(52).toString();
        String responseCode;

        double amount = Double.parseDouble(message.get(4).toString());
        double currentBalance = accountRepository.findByAccountnumber(accountNumber).getBalance();

        BigDecimal balance = new BigDecimal(0);

        if (!accountRepository.findByAccountnumber(accountNumber).getPinnumber().trim().equals(pinNumber)) {
            responseCode = "017610";
        } else if (accountRepository.findByAccountnumber(accountNumber).getStatus().trim().equals("inactive")) {
            responseCode = "017810";
        } else if ((currentBalance - amount) < 50000) {
            responseCode = "015110";
        } else if (amount % 100000 != 0) {
            responseCode = "011210";
        } else {
            Account account = accountRepository.findByAccountnumber(accountNumber);
            account.setBalance(currentBalance - amount);
            accountRepository.save(account);

            Transaction transaction = new Transaction("Cash Withdrawal", account.getAccountnumber(), amount, 0, amount, account.getBalance());
            transactionRepository.save(transaction);

            balance = new BigDecimal(accountRepository.findByAccountnumber(accountNumber).getBalance());
            responseCode = "010010";
        }

        TransactionHelper transactionHelper = new TransactionHelper(accountNumber, balance, responseCode);
        CashWithdrawalResponse createResponse = new CashWithdrawalResponse();

        return createResponse.process(transactionHelper);
    }
}
