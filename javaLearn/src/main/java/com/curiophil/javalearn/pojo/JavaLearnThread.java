package com.curiophil.javalearn.pojo;

import org.slf4j.MDC;

import java.util.Map;

public abstract class JavaLearnThread extends Thread {

    private static final Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();;

    @Override
    public void run() {
        MDC.setContextMap(copyOfContextMap);
        run0();
    }

    public void run0() {

    }
}
