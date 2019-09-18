package com.syhb.project.services.impl;

import com.syhb.project.client.RPCClient;
import com.syhb.project.helpers.TransactionHelper;
import com.syhb.project.services.Transaction;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.packager.GenericPackager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.*;

public class BalanceInquiryImpl implements Transaction {

    private final Logger logger = LoggerFactory.getLogger(BalanceInquiryImpl.class);

    @Override
    public String send(TransactionHelper transactionHelper) {

        logger.info("In BalanceInquiryImpl send");

        String result = null;
        try {

            GenericPackager genericPkg = new GenericPackager("src/main/resources/fields.xml");
            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setPackager(genericPkg);
            isoMsg.setMTI("0200");
            isoMsg.set(1, "a000000000000000");
            isoMsg.set(2, transactionHelper.getAccountNumber());
            isoMsg.set(3, "300000");
            isoMsg.set(4, String.valueOf(transactionHelper.getAmount()));
            isoMsg.set(7, new SimpleDateFormat("MMddHHmmss").format(new Date()));
            isoMsg.set(11, String.valueOf(generateStan()));
            isoMsg.set(12, new SimpleDateFormat("HHmmss").format(new Date()));
            isoMsg.set(13, new SimpleDateFormat("MMdd").format(new Date()));
            isoMsg.set(15, new SimpleDateFormat("MMdd").format(new Date()));
            isoMsg.set(18, "0000");
            isoMsg.set(32, "00000000000");
            isoMsg.set(33, "00000000000");
            isoMsg.set(37, "an0000000000");
            isoMsg.set(41, "tm000000");
            isoMsg.set(42, "an0000000000000");
            isoMsg.set(43, "ans0000000000000000000000000000000000000");
            isoMsg.set(49, "001");
            isoMsg.set(52, transactionHelper.getPinNumber());
            isoMsg.set(54, "0010");
            isoMsg.set(62, "001a");
            isoMsg.set(102, "01a");
            isoMsg.set(128, ISOUtil.hex2byte("FAA57088694EF194"));

            byte[] isoMsgByte = isoMsg.pack();
            result = new String(isoMsgByte);
        } catch (org.jpos.iso.ISOException e) {
            logger.debug("In BalanceInquiryImpl send. Message: "+ e.getMessage());
        }

        String requestQueueName = "server-queue";

        String response = null;
        try (RPCClient RPCResponse = new RPCClient()) {

             response = RPCResponse.call(result, requestQueueName);

        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        return response;
    }

    @Override
    public Integer generateStan(){
        return new Random().nextInt(999999);
    }

}
