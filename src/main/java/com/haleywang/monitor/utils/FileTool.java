package com.haleywang.monitor.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class FileTool {

    public static String readInSamePkg(Class cls, String fileName) throws IOException {
        String path = cls.getResource("").getPath().toString();
        return FileUtils.readFileToString(new File(path + "/" + fileName), "UTF-8");
    }

    public static String read(String filePath) throws IOException {
        String path = FileTool.class.getResource("/").getPath().toString();
        return FileUtils.readFileToString(new File(path + filePath), "UTF-8");
    }

}