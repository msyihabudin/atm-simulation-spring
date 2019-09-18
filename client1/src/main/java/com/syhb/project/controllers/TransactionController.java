package com.syhb.project.controllers;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TransactionController {

    Scanner scan = new Scanner(System.in);
    public final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    Map unpackFromIso(String isomessage){

        Map<Integer, String> map = new HashMap<Integer, String>();

        try{
            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setPackager(new GenericPackager("src/main/resources/isoPackager.xml"));
            isoMsg.unpack(isomessage.getBytes());

            for(int i = 1; i <= isoMsg.getMaxField(); i++){
                if(isoMsg.hasField(i)){
                    map.put(i, isoMsg.getString(i));
                }
            }
        } catch(ISOException e) {
            logger.debug("In TransactionController unpackFromIso. Message: "+ e.getMessage());
        }

        return map;

    }

    String removeLeadZero(String message) {
        return message.replaceFirst("^0+(?!$)", "");
    }

    String currencyFormat(String currency) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(Double.parseDouble(currency));
    }

}
