package com.xinzuo.qiaobashangjia.util;

public class BaseInfo  {
    private static String balance ="";

    public static String getA() {
        return balance;
    }

    public static void setA(String balance) {
        BaseInfo.balance = balance;
    }
}
