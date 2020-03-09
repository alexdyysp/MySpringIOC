package com.ioc.example;

import java.io.Serializable;

public class EMP implements Serializable {

    private Long empno;
    private String ename;
    private DEPT dept;

    public Long getEmpno() {
        return empno;
    }

    public String getEname() {
        return ename;
    }

    public DEPT getDept() {
        return dept;
    }

    public void setEmpno(Long empno) {
        this.empno = empno;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public void setDept(DEPT dept) {
        this.dept = dept;
    }


}
