package com.ioc.bean;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class iniFileReader {

    public static String File_Name = "ioc.ini";
    public static String Base_Dir = System.getProperty("user.dir");
    public static String Sub_Path = "src\\com\\resources\\" + File_Name;
    public Map<String, Object> Objectmap = new LinkedHashMap<>();
    private String flag = null;

    public iniFileReader() {
        this.readIni();
    }

    public Map<String, Object> getObjectMap(){
        return Objectmap;
    }

    public void readIni(){
        File file = new File(Base_Dir, Sub_Path);
        //System.getProperties().list(System.out);
        //System.out.println(file.isFile());
        try {
            Scanner scan = new Scanner(file);
            while (scan.hasNext()){
                String context = scan.next();
                //System.out.println(context);
                // read "[]"
                if(context.matches("\\[.+\\]")){
                    if("[ioc]".equalsIgnoreCase(context)){
                        flag = "[ioc]";
                    }else if("[field]".equalsIgnoreCase(context)){
                        flag = "[field]";
                    }else if("[di]".equalsIgnoreCase(context)){
                        flag = "[di]";
                    }
                }else{
                    this.readMapObject(context);
                }
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    private void readMapObject(String context){
        switch (this.flag){
            case "[ioc]":{
                String res[] = context.split("=");
                try{
                    this.Objectmap.put(res[0], Class.forName(res[1]).getDeclaredConstructor().newInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case "[field]":{
                String res[] = context.split("=");
                String objectInfo[]  = res[0].split("\\.");
                Object obj = this.Objectmap.get(objectInfo[0]);
                if(obj != null){
                    try{
                        BeanConfig.configFiled(obj, objectInfo[1], res[1]);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            }
            case "[di]":{
                String res[] = context.split("=");
                String objectInfo[]  = res[0].split("\\.");
                Object obj = this.Objectmap.get(objectInfo[0]);
                if(obj != null){
                    try{
                        BeanConfig.configFiled(obj, objectInfo[1], this.Objectmap.get(res[1]));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
    }
}
