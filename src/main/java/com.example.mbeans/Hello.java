package com.example.mbeans;

import com.example.mbeans.HeapDump;

public class Hello implements HelloMBean {
    private final String name = "HelloMBean";
    private int property1 = 13;
    private int property2 = 18;

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

    public String dumpHeap(String heapDumpFile, boolean live) {
        return HeapDump.dumpHeap(heapDumpFile, live);
    }
}
