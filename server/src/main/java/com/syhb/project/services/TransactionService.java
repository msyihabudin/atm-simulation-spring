package com.syhb.project.services;

import org.json.JSONException;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public interface TransactionService {

    String response(Map message) throws JSONException, IOException, TimeoutException;

}
