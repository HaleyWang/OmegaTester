package com.haleywang.monitor.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.haleywang.monitor.App;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class FileTool {

    public static String readInSamePkg(Class cls, String fileName) throws IOException {
        String path = cls.getResource("").getPath().replace("test-classes", "classes");

        if(PathUtils.isStartupFromJar(cls)) {
            System.out.println(path);
            List<String> line = IOUtils.readLines(cls.getResourceAsStream(fileName), "UTF-8");

            return line.stream().collect(Collectors.joining("\n"));
        }

        return FileUtils.readFileToString(new File(path + "/" + fileName), "UTF-8");
    }

    public static String read(String filePath) throws IOException {

        if(PathUtils.isStartupFromJar(FileTool.class)) {

            String filePathNew = filePath;
            if(!filePathNew.startsWith("/")) {
                filePathNew = "/" + filePathNew;
            }

            List<String> line = IOUtils.readLines(FileTool.class.getResourceAsStream(filePathNew), "UTF-8");

            return line.stream().collect(Collectors.joining("\n"));
        }

        String path = FileTool.class.getResource("/").getPath().toString();
        return FileUtils.readFileToString(new File(path + filePath), "UTF-8");
    }

}