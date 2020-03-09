package com.ioc.test;

import com.ioc.bean.iniFileReader;

public class TestiniReader {
    public static void main(String[] args){
        iniFileReader iniread = new iniFileReader();
        //iniread.readIni();

        System.out.println(iniread.getObjectMap());
    }
}
