package com.haleywang.monitor.common.mvc;

import com.google.common.base.Splitter;
import com.haleywang.monitor.utils.PathUtils;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;

/**
 * Created by haley on 2018/8/17.
 */
public class StaticResourcesHandler implements HttpHandler {

    private static final String EXTENSION_MIME = "js=application/javascript,css=text/css," +
            "woff=application/octet-stream,ttf=application/octet-stream,woff2=application/font-woff2";
    private static final Map<String, String> EXTENSION_MIME_MAP = Splitter.on(",")
            .withKeyValueSeparator("=").split(EXTENSION_MIME);
    private static final String DEFAULT_MIME = "text/html";

    public void handle(HttpExchange t) throws IOException {
        String root = PathUtils.getRoot();
        URI uri = t.getRequestURI();
        String path = uri.getPath();
        if("/".equals(path)) {
            path = "/index.html";
        }
        String rootPath = root.endsWith("/") ? root : root +"/";
        String filePath = rootPath + "static" + path;



        filePath = filePath.replaceAll("//", "/");
        System.out.println("looking for path: " + filePath);

        File file = new File(filePath).getCanonicalFile();

        if (!file.isFile()) {
            // Object does not exist or is not a file: reject with 404 error.
            String response = "404 (Not Found)\n";
            t.sendResponseHeaders(404, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            //os.close();
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

}

