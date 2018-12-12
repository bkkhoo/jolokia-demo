package com.example.mbeans;

public class Hello implements HelloMBean {
    private static final int DEFAULT_VALUE = 13;
    private final String name = "HelloMBean";
    private int property1 = DEFAULT_VALUE;
    private int property2 = DEFAULT_VALUE;

    public String sayHello() {
        return this.name + ": hello, world";
    }

    public int add(int x, int y) {
        return x + y;
    }

    public String getName() {
        return this.name;
    }

    public int getProperty1() {
        return this.property1;
    }

    public synchronized void setProperty1(int integer) {
        this.property1 = integer;
        System.out.println("Property1 is now " + this.property1);
    }
    public int getProperty2() {
        return this.property2;
    }

    public synchronized void setProperty2(int integer) {
        this.property2 = integer;
        System.out.println("Property2 is now " + this.property2);
    }

}

