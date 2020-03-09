package com.ioc.utils;

import com.ioc.bean.iniFileReader;

public class FactoryBean {

    private static iniFileReader inireader = new iniFileReader();

    public FactoryBean() {}

    public static <T> T getInstance(String beanName){
        try {
            // 通过传入类对象返回实例: return (T) clazz.getDeclaredConstructor().newInstance();
            // 通过beanName直接获取实例化对象
            // return (T) inireader.getObjectMap().get(beanName).newInstance()
            return (T) inireader.getObjectMap().get(beanName);
        } catch (Exception e) {
            return null;
        }
    }
}
