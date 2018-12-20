/*
base on:
https://stackoverflow.com/questions/12295824/create-heap-dump-from-within-application-without-hotspotdiagnosticmxbean
*/

package com.example.mbeans;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.management.MBeanServer;
import java.util.zip.GZIPOutputStream;
import java.util.Base64;

public class HeapDump {
    // This is the name of the HotSpot Diagnostic MBean
    private static final String HOTSPOT_BEAN_NAME = "com.sun.management:type=HotSpotDiagnostic";

    private static volatile Object hotspotMBean;   // field to store the hotspot diagnostic MBean

    public static String dumpHeap(String heapDumpFile, boolean live) {
        String headDumpBase64 = "";
        initHotspotMBean();    // initialize hotspot diagnostic MBean
        try {
            Class clazz = Class.forName("com.sun.management.HotSpotDiagnosticMXBean");
            Method m = clazz.getMethod("dumpHeap", String.class, boolean.class);
            try { Files.deleteIfExists(Paths.get(heapDumpFile)); } catch (Exception ignore) { }
            m.invoke(hotspotMBean, heapDumpFile, live);
            gzipFile(heapDumpFile);
            headDumpBase64 = encoder(heapDumpFile + ".gz");
            try { Files.deleteIfExists(Paths.get(heapDumpFile)); } catch (Exception ignore) { }
            try { Files.deleteIfExists(Paths.get(heapDumpFile + ".gz")); } catch (Exception ignore) { }
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
        return headDumpBase64;
    }

    // initialize the hotspot diagnostic MBean field
    private static void initHotspotMBean() {
        if (hotspotMBean == null) {
            synchronized (HeapDump.class) {
                if (hotspotMBean == null) {
                    hotspotMBean = getHotspotMBean();
                }
            }
        }
    }

    // get the hotspot diagnostic MBean from the platform MBean server
    private static Object getHotspotMBean() {
        try {
            Class clazz = Class.forName("com.sun.management.HotSpotDiagnosticMXBean");
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            Object bean = ManagementFactory.newPlatformMXBeanProxy(server, HOTSPOT_BEAN_NAME, clazz);
            return bean;
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }

    private static void gzipFile(String filename) {
        try {
            FileInputStream inputFile = new FileInputStream(filename);
            GZIPOutputStream gzipOS = new GZIPOutputStream(new FileOutputStream(filename + ".gz"));
            byte[] buffer = new byte[1024];
            int len;
            while((len=inputFile.read(buffer)) != -1){
                gzipOS.write(buffer, 0, len);
            }
            inputFile.close();
            gzipOS.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String encoder(String filename) {
        String base64String = "";
        try {
            Path path = Paths.get(filename);
            base64String = Base64.getEncoder().encodeToString(Files.readAllBytes(path));
        } catch (Exception ignore) {
            System.out.println("error encoding file");
        }
        return base64String;
    }
}
