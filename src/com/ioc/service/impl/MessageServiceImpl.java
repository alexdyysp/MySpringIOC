package com.ioc.service.impl;

import com.ioc.service.IMessageService;

public class MessageServiceImpl implements IMessageService {

    @Override
    public String echo(String message){
        return message;
    }

}
