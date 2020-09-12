package com.haleywang.monitor.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
/**
 * @author haley
 * @date 2018/12/16
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PathUtils {



    public static<T> boolean isStartupFromJar(Class<T> clazz) {
        File file = new File(clazz.getProtectionDomain().getCodeSource().getLocation().getPath());
        return file.isFile();

    }

    public static String getRoot() {
        String res = "";
        if(isStartupFromJar(PathUtils.class)) {
            res =  new File("").getAbsolutePath();

        }else {
            res = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        }
        res = res.replace("target/test-classes", "target/classes");
        return res;
    }
}
