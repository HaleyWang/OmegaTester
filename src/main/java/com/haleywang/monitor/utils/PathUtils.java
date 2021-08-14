package com.haleywang.monitor.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author haley
 * @date 2018/12/16
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PathUtils {


    public static <T> boolean isStartupFromJar(Class<T> clazz) {
        File file = new File(clazz.getProtectionDomain().getCodeSource().getLocation().getPath());
        return file.isFile();

    }

    public static String getRresourcePath(String fileName) {
        // fileName eg: "/static/js/underscore-min.js";
        String filePath = PathUtils.getRoot() + fileName;
        return filePath.replaceAll("//", "/").replaceAll("test-classes", "classes");
    }

    public static String getRresource(String fileName) throws IOException {
        String filePath = getRresourcePath(fileName);
        return FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8);
    }

    public static String getRoot() {
        //String parentPath = new File("").getAbsolutePath()
        String res = "";
        if (isStartupFromJar(PathUtils.class)) {
            res = new File("").getAbsolutePath();

        } else {
            res = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        }
        res = res.replace("target/test-classes", "target/classes");
        return res;
    }

    public static String urlToPath(String libUrl) {
        int index = libUrl.indexOf(";//") + 3;
        return libUrl.substring(index).replaceAll("//", "/");
    }
}
