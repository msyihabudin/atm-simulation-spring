package com.syhb.project.handlers;

public interface CustomHandler {

    void setNextHandler(CustomHandler nextHandler);
    void message(String code);

}
