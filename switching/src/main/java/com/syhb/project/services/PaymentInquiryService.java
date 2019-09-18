package com.syhb.project.services;

import com.syhb.project.models.Product;
import com.syhb.project.helpers.TransactionHelper;
import com.syhb.project.repositories.ProductRepository;
import com.syhb.project.responses.PaymentInquiryResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Map;

public class PaymentInquiryService implements TransactionService {

    private final Logger logger = LogManager.getLogger(PaymentInquiryService.class);

    private final ProductRepository productRepository;

    public PaymentInquiryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public String response(Map message) throws JSONException {

        logger.info("In PaymentInquiryService response");

        String accountNumber = message.get(2).toString();
        String referenceNumber = message.get(62).toString().substring(4);
        String responseCode;

        JSONObject jsonAccount = new JSONObject();
        if (productRepository.existsByReferencenumber(referenceNumber)) {
            Product product = productRepository.findByReferencenumber(referenceNumber);

            jsonAccount.put("idPelanggan", product.getReferenceNumber());
            jsonAccount.put("jumlahTagihan", product.getCredit());

            responseCode = "380010";
        } else {
            responseCode = "381210";
        }

        TransactionHelper transactionHelper = new TransactionHelper(accountNumber, new BigDecimal(0), responseCode);
        transactionHelper.setDestinationNumber(String.valueOf(referenceNumber));
        transactionHelper.setJsonObject(jsonAccount);

        PaymentInquiryResponse createResponse = new PaymentInquiryResponse();

        return createResponse.process(transactionHelper);
    }
}
