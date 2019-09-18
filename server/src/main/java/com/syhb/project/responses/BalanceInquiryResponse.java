package com.syhb.project.responses;

import com.syhb.project.helpers.TransactionHelper;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.packager.GenericPackager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BalanceInquiryResponse extends TransactionResponse {

    public String process(TransactionHelper transactionHelper) {
        String result = null;
        GenericPackager genericPkg;
        ISOMsg isoMsg;
        String rc = transactionHelper.getResponseCode().trim().substring(2,4);

        try {
            genericPkg = new GenericPackager("src/main/resources/fields.xml");
            isoMsg = new ISOMsg();
            isoMsg.setPackager(genericPkg);

            isoMsg.setMTI("0200");
            isoMsg.set(1, "a000000000000000");
            isoMsg.set(2, transactionHelper.getAccountNumber());
            isoMsg.set(3, transactionHelper.getResponseCode());
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
            isoMsg.set(38, "000000");
            isoMsg.set(39, rc);
            isoMsg.set(41, "tm000000");
            isoMsg.set(42, "an0000000000000");
            isoMsg.set(43, "ans0000000000000000000000000000000000000");
            isoMsg.set(49, "001");
            isoMsg.set(54, "0010");
            isoMsg.set(62, "001a");
            isoMsg.set(102, "01a");
            isoMsg.set(128, ISOUtil.hex2byte("FAA57088694EF194"));

            byte[] isoMsgByte = isoMsg.pack();
            result = new String(isoMsgByte);
        } catch (org.jpos.iso.ISOException e) {
            e.printStackTrace();
        }

        return result;
    }

}
