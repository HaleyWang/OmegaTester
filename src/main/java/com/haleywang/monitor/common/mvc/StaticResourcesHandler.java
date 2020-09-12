package com.haleywang.monitor.common.mvc;

import com.google.common.base.Splitter;
import com.haleywang.monitor.utils.PathUtils;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Stream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Created by haley on 2018/8/17.
 */
@Slf4j
public class StaticResourcesHandler implements HttpHandler {

    private static final String EXTENSION_MIME = "js=application/javascript,css=text/css," +
            "woff=application/octet-stream,ttf=application/octet-stream,woff2=application/font-woff2";
    private static final Map<String, String> EXTENSION_MIME_MAP = Splitter.on(",")
            .withKeyValueSeparator("=").split(EXTENSION_MIME);
    private static final String DEFAULT_MIME = "text/html";
    public static final String INDEX_HTML = "/index.html";
    public static final String DIAGONAL = "/";


    public  void copyFolder(Path src, Path dest) throws IOException {
        try (Stream<Path> stream = Files.walk(src)) {
            stream.forEach(source -> copy(source, dest.resolve(src.relativize(source))));
        }
    }

    private void copy(Path source, Path dest) {
        try {
            Files.copy(source, dest, REPLACE_EXISTING);
        } catch (Exception e) {
            //throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void handle(HttpExchange t) throws IOException {
        String root = PathUtils.getRoot();
        URI uri = t.getRequestURI();
        String path = uri.getPath();
        if(DIAGONAL.equals(path)) {
            path = INDEX_HTML;
        }
        String rootPath = root.endsWith(DIAGONAL) ? root : root + DIAGONAL;
        String filePath = rootPath + "static" + path;
        File file = new File(filePath).getCanonicalFile();
        boolean fileExists = file.isFile();
        if (!fileExists && filePath.contains("target")) {
            copyResources(root);
            fileExists = file.isFile();
        }

        filePath = filePath.replaceAll("//", DIAGONAL);
        log.info("looking for path: " + filePath);

        if (!fileExists) {
            // Object does not exist or is not a file: reject with 404 error.
            String response = "404 (Not Found)\n";
            t.sendResponseHeaders(404, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            IOUtils.closeQuietly(os);
        } else {
            // Object exists and is a file: accept with response code 200.

            String extension = FilenameUtils.getExtension(path);
            String mime = EXTENSION_MIME_MAP.getOrDefault(extension, DEFAULT_MIME);

            Headers h = t.getResponseHeaders();
            h.set("Content-Type", mime);
            t.sendResponseHeaders(200, 0);

            OutputStream os = null;
            FileInputStream fs = null;
            try {
                os = t.getResponseBody();
                fs = new FileInputStream(file);
                final byte[] buffer = new byte[0x10000];
                int count = 0;
                while ((count = fs.read(buffer)) >= 0) {
                    os.write(buffer, 0, count);
                }
            }finally {
                IOUtils.closeQuietly(fs);
                IOUtils.closeQuietly(os);
            }
        }
    }

    private void copyResources(String root) throws IOException {
        synchronized (this) {
            String projectPath = Paths.get(root, "").getParent().getParent().toString();
            String projectResources = projectPath + DIAGONAL + "src/main/resources";

            copyFolder(Paths.get(projectResources), Paths.get(root));
        }

    }

}

