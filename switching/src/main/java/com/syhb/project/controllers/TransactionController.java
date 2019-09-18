package com.syhb.project.controllers;

import com.syhb.project.models.ISOMessage;
import com.syhb.project.repositories.*;
import com.syhb.project.services.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.json.JSONException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TransactionController {

    private final Logger logger = LogManager.getLogger(TransactionController.class);

    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    private final CustomerRepository customerRepository;

    private final BankRepository bankRepository;

    private final ProductRepository productRepository;

    public TransactionController(AccountRepository accountRepository, TransactionRepository transactionRepository,
                                 CustomerRepository customerRepository, BankRepository bankRepository,
                                 ProductRepository productRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
        this.bankRepository = bankRepository;
        this.productRepository = productRepository;
    }

    @PostMapping("/transaction/create")
    public String create(@RequestBody String body) throws JSONException {

        logger.info("In TransactionController create");

        ISOMessage isoMessage = new ISOMessage(unpackFromIso(body));
        Map message = isoMessage.getMessage();

        TransactionService transaction;

        switch (message.get(3).toString()) {
            case "400010":
                transaction = new TransferService(accountRepository, transactionRepository, customerRepository);
                break;
            case "380010":
                transaction = new PaymentInquiryService(productRepository);
                break;
            case "180010":
                transaction = new PaymentService(productRepository);
                break;
            default:
                transaction = new TransferInquiryService(accountRepository, customerRepository, bankRepository);
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
            logger.error("In TransactionController unpackFromIso. Message: "+ e.getMessage());
        }

        return map;
    }

}
