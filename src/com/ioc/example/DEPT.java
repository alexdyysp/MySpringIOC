package com.ioc.example;

import java.io.Serializable;

public class DEPT implements Serializable {

    private Long deptno;
    private String dname;

    public Long getDeptno() {
        return deptno;
    }

    public void setDeptno(Long deptno) {
        this.deptno = deptno;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }


}
