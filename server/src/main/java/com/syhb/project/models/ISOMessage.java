package com.syhb.project.models;

import java.util.Map;

public class ISOMessage {

    private Map<Integer, String> message;

    public ISOMessage(Map<Integer, String> message) {
        this.message = message;
    }

    public Map<Integer, String> getMessage() {
        return message;
    }
}
