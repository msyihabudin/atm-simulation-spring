package com.syhb.project.services.impl;

import com.syhb.project.helpers.TransactionHelper;
import com.syhb.project.helpers.TransactionHelperType;
import com.syhb.project.services.ClientService;
import com.syhb.project.services.Transaction;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.packager.GenericPackager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class TransferImpl implements Transaction {

    private final Logger logger = LoggerFactory.getLogger(TransferImpl.class);

    @Override
    public String send(TransactionHelper transactionHelper) {

        logger.info("In TransferImpl send");

        String result = null;
        try {
            GenericPackager genericPkg = new GenericPackager("src/main/resources/fields.xml");
            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setPackager(genericPkg);
            isoMsg.setMTI("0200");
            isoMsg.set(1, "a000000000000000");
            isoMsg.set(2, transactionHelper.getAccountNumber());
            isoMsg.set(3, "400010");
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
            isoMsg.set(48, "001a");
            isoMsg.set(49, "001");
            isoMsg.set(52, transactionHelper.getPinNumber());
            isoMsg.set(62, transactionHelper.getDestinationNumber());
            isoMsg.set(100, "010");
            isoMsg.set(102, "010");
            isoMsg.set(103, "010");
            isoMsg.set(125, transactionHelper.getType().toString());
            isoMsg.set(127, "0010");
            isoMsg.set(128, ISOUtil.hex2byte("FAA57088694EF194"));

            byte[] isoMsgByte = isoMsg.pack();
            result = new String(isoMsgByte);
        } catch (org.jpos.iso.ISOException e) {
            logger.debug("In TransferImpl send. Message: "+ e.getMessage());
        }

        ClientService cs = new ClientService();
        String requestUrl;

        if (transactionHelper.getType() == TransactionHelperType.INTERNAL) {
            requestUrl = "http://localhost:8084/api/transaction/create/";
        } else if (transactionHelper.getType() == TransactionHelperType.EXTERNAL) {
            requestUrl = "http://localhost:8084/api/transaction/create/";
            transactionHelper.setType(TransactionHelperType.EXTERNALToINTERNAL);
        } else {
            requestUrl = "http://localhost:8085/api/transaction/create/";
        }
        return cs.sendPostRequest(requestUrl, result);
    }

    @Override
    public Integer generateStan() {
        return new Random().nextInt(999999);
    }

}
