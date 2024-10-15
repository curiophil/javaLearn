package com.curiophil.javalearn.pojo;

import org.slf4j.MDC;

import java.util.Map;

public abstract class JavaLearnThread extends Thread {

    private Map<String, String> copyOfContextMap;

    public JavaLearnThread() {
        this.copyOfContextMap = MDC.getCopyOfContextMap();
    }

    @Override
    public void run() {
        MDC.setContextMap(copyOfContextMap);
        run0();
    }

    public abstract void run0();
}
