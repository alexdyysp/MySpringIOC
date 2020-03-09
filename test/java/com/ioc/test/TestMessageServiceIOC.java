package com.ioc.test;

import com.ioc.utils.FactoryBean;
import com.ioc.service.IMessageService;

public class TestMessageServiceIOC {
    public static void main(String[] args){
        // 通过FactoryBean获取IMessageService实例化对象
        IMessageService messageService = FactoryBean.getInstance("msgecho");
        System.out.println(messageService.echo("I am echo from IOC"));
    }
}
