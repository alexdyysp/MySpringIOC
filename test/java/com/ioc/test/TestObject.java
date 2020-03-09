package com.ioc.test;

import com.ioc.example.DEPT;
import com.ioc.example.EMP;
import com.ioc.utils.FactoryBean;

public class TestObject {
    public static void main(String[] args){
        //DEPT dept = FactoryBean.getInstance("dept");
        EMP emp = FactoryBean.getInstance("emp");
        //System.out.println(dept);
        System.out.println(emp);
        //System.out.println(dept.getDeptno());
        //System.out.println(dept.getDname());
        System.out.println(emp.getEname());
        System.out.println(emp.getEmpno());
        System.out.println(emp.getDept());
    }
}
