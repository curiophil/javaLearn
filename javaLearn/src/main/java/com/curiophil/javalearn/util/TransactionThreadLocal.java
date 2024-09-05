package com.curiophil.javalearn.util;

public class TransactionThreadLocal {

    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();


    public static String getTxId() {
        return threadLocal.get();
    }

    public static void setTxId(String txId) {
        threadLocal.set(txId);
    }
}
