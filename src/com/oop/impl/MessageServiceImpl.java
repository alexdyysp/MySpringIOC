package com.oop.impl;

import com.oop.IMessageService;

public class MessageServiceImpl implements IMessageService {

    @Override
    public String echo(String message){
        return message;
    }

}
