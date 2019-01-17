package com.haleywang.monitor.utils;

import java.io.File;

public class PathUtils {

    public static<T> boolean isStartupFromJar(Class<T> clazz) {
        File file = new File(clazz.getProtectionDomain().getCodeSource().getLocation().getPath());
        return file.isFile();

    }

    public static String getRoot() {
        if(isStartupFromJar(PathUtils.class)) {
            return new File("").getAbsolutePath();

        }
        return Thread.currentThread().getContextClassLoader().getResource("").getPath();
    }
}
