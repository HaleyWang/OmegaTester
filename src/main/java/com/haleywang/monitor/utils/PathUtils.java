package com.haleywang.monitor.utils;

public class PathUtils {

    public static String getRoot() {
        return Thread.currentThread().getContextClassLoader().getResource("").getPath();
    }
}
