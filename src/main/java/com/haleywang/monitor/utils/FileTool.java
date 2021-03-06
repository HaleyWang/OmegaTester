package com.haleywang.monitor.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author haley
 * @date 2018/12/16
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileTool {

    public static final Charset UTF_8 = StandardCharsets.UTF_8;
    public static final String PATH_DELIMITER = "/";
    public static final String NAME_DELIMITER = ".";
    public static final char PATH_DELIMITER_CHAR = '/';
    public static final char NAME_DELIMITER_CHAR = '.';

    public static String readInSamePkg(Class cls, String fileName) throws IOException {
        return readInSamePkg(cls, fileName, false);
    }

    public static String readInSamePkg(Class cls, String fileName, boolean forTest) throws IOException {
        String path = cls.getResource("").getPath();
        if (!forTest) {
            path = path.replace("test-classes", "classes");
        }

        if(PathUtils.isStartupFromJar(cls)) {
            List<String> line = IOUtils.readLines(cls.getResourceAsStream(fileName), UTF_8);

            return line.stream().collect(Collectors.joining("\n"));
        }

        return FileUtils.readFileToString(new File(path + PATH_DELIMITER + fileName), UTF_8);
    }

    public static String read(String filePath) throws IOException {

        if(PathUtils.isStartupFromJar(FileTool.class)) {

            String filePathNew = filePath;
            if(!filePathNew.startsWith(PATH_DELIMITER)) {
                filePathNew = PATH_DELIMITER + filePathNew;
            }

            List<String> line = IOUtils.readLines(FileTool.class.getResourceAsStream(filePathNew), UTF_8);

            return line.stream().collect(Collectors.joining("\n"));
        }

        String path = FileTool.class.getResource(PATH_DELIMITER).getPath();
        return FileUtils.readFileToString(new File(path + filePath), UTF_8);
    }

}