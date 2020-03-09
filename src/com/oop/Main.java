package com.oop;

import com.oop.impl.MessageServiceImpl;

public class Main {
    public static void main(String[] args){
        // 通过new关键字实例化IMessageService对象
        IMessageService messageService = new MessageServiceImpl();
        System.out.println(messageService.echo("I am echo from OOP"));
    }
}
