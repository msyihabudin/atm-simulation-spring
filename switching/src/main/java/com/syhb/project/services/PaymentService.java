package com.syhb.project.services;

import com.syhb.project.models.Product;
import com.syhb.project.helpers.TransactionHelper;
import com.syhb.project.repositories.ProductRepository;
import com.syhb.project.responses.PaymentResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Map;

public class PaymentService implements TransactionService {

    private final Logger logger = LogManager.getLogger(PaymentService.class);

    private final ProductRepository productRepository;

    public PaymentService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public String response(Map message) throws JSONException {

        logger.info("In PaymentService response");

        String accountNumber = message.get(2).toString();
        String referenceNumber = message.get(62).toString().substring(4);
        String productCode = message.get(62).toString().substring(0,4);
        double amount = Double.parseDouble(message.get(4).toString());
        String responseCode;

        JSONObject jsonAccount = new JSONObject();
        if (productCode.equals("2101")) {
            if (productRepository.existsByReferencenumber(referenceNumber)) {
                Product product = productRepository.findByReferencenumber(referenceNumber);
                product.setStatus(true);
                productRepository.save(product);

                jsonAccount.put("idPelanggan", product.getReferenceNumber());
                jsonAccount.put("jumlahTagihan", product.getCredit());

                responseCode = "180010";
            } else {
                responseCode = "181210";
            }
        } else {
            Product product = new Product(productCode, referenceNumber, amount  + 2000);
            product.setStatus(true);
            productRepository.save(product);

            jsonAccount.put("noHandphone", referenceNumber);
            jsonAccount.put("nominalPulsa", amount);
            jsonAccount.put("biayaAdministrasi", 2000);

            responseCode = "180010";
        }

        TransactionHelper transactionHelper = new TransactionHelper(accountNumber, new BigDecimal(0), responseCode);
        transactionHelper.setDestinationNumber(referenceNumber);
        transactionHelper.setJsonObject(jsonAccount);

        PaymentResponse createResponse = new PaymentResponse();

        return createResponse.process(transactionHelper);

    }
}
