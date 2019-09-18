package com.syhb.project.controllers;

import com.syhb.project.models.ISOMessage;
import com.syhb.project.repositories.AccountRepository;
import com.syhb.project.repositories.CustomerRepository;
import com.syhb.project.repositories.TransactionRepository;
import com.syhb.project.services.*;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api")
public class TransactionController {

    private final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    private final CustomerRepository customerRepository;

    public TransactionController(AccountRepository accountRepository, TransactionRepository transactionRepository, CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
    }

    @PostMapping("/transaction/create")
    public String create(@RequestBody String body) throws JSONException, IOException, TimeoutException {

        logger.debug("In TransactionService create");

        ISOMessage isoMessage = new ISOMessage(unpackFromIso(body));
        Map message = isoMessage.getMessage();

        TransactionService transaction;

        switch (message.get(3).toString()) {
            case "010010":
                transaction = new CashWithdrawalService(accountRepository, transactionRepository);
                break;
            case "390010":
                transaction = new TransferInquiryService(accountRepository, customerRepository);
                break;
            case "400010":
                transaction = new TransferService(accountRepository, transactionRepository, customerRepository);
                break;
            case "380010":
                transaction = new PaymentInquiryService(accountRepository);
                break;
            case "180010":
                transaction = new PaymentService(accountRepository, transactionRepository);
                break;
            default:
                transaction = new BalanceInquiryService(accountRepository);
                break;
        }

        return transaction.response(message);
    }

    private Map unpackFromIso(String isomessage){
        Map<Integer, String> map = new HashMap<Integer, String>();

        try{
            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setPackager(new GenericPackager("src/main/resources/fields.xml"));
            isoMsg.unpack(isomessage.getBytes());

            for(int i = 1; i <= isoMsg.getMaxField(); i++){
                if(isoMsg.hasField(i)){
                    map.put(i, isoMsg.getString(i));
                }
            }
        }catch(ISOException e){
            e.printStackTrace();
        }

        return map;
    }

}
