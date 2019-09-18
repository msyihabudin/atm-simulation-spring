package com.syhb.project.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.syhb.project.services.impl.BalanceInquiryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientService {

    private final Logger logger = LoggerFactory.getLogger(BalanceInquiryImpl.class);

    public String sendPostRequest(String requestUrl, String payload) {
        try {
            logger.info("In ClientService sendPostRequest");
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            writer.write(payload);
            writer.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }
            br.close();
            connection.disconnect();
            return jsonString.toString();
        } catch (Exception e) {
            logger.error("In ClientService sendPostRequest. Message: "+ e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

}
