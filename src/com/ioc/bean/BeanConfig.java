package com.ioc.bean;

import java.lang.reflect.Field;

public class BeanConfig {

    private BeanConfig(){}
    public static void configFiled(Object obj, String fieldName, Object val){
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            if (field.getType() == Byte.class){
                if(val instanceof String){
                    field.set(obj, Byte.parseByte(val.toString()));
                }
            }else if(field.getType() == Short.class) {
                if(val instanceof String){
                    field.set(obj, Short.parseShort(val.toString()));
                }
            }else if(field.getType() == Integer.class) {
                if (val instanceof String) {
                    field.set(obj, Integer.parseInt(val.toString()));
                }
            }else if(field.getType() == Long.class) {
                if(val instanceof String){
                    field.set(obj, Long.parseLong(val.toString()));
                }
            }else if(field.getType() == Float.class) {
                if(val instanceof String){
                    field.set(obj, Float.parseFloat(val.toString()));
                }
            }else if(field.getType() == Double.class) {
                if(val instanceof String){
                    field.set(obj, Double.parseDouble(val.toString()));
                }
            }else if(field.getType() == char.class) {
                if(val instanceof String){
                    field.set(obj, val.toString());
                }
            }else if(field.getType() == Boolean.class) {
                if (val instanceof String) {
                    field.set(obj, Boolean.parseBoolean(val.toString()));
                }
            }else if(field.getType() == String.class) {
                    if(val instanceof String){
                        field.set(obj, val);
                    }
            }else {
                field.set(obj, val);
            }
        }catch (NoSuchFieldException e ){
            e.printStackTrace();
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }
    }
}
