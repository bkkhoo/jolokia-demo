package com.example.mbeans;

public interface HelloMBean {
    // operations
    public String sayHello();
    public int add(int x, int y);

    // a read-only Attribute
    public String getName();

    // a read-write Attributes
    public int getProperty1();
    public void setProperty1(int interger);
    public int getProperty2();
    public void setProperty2(int interger);
}

