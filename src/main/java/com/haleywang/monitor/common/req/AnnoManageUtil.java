package com.haleywang.monitor.common.req;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


public final class AnnoManageUtil { 

    public static List<Class<?>> scan(String packageName, Class<? extends Annotation> annotation) {
        List<Class<?>> classList = new ArrayList<Class<?>>(); 
        String packageDirName = packageName.replace('.', '/'); 
        Enumeration<URL> dirs = null; 

        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 

        while (dirs.hasMoreElements()) { 
            URL url = dirs.nextElement(); 

            String protocol = url.getProtocol();

            if ("file".equals(protocol)) {
                try {
                    String filePath = Paths.get(url.toURI()).toFile().getPath();
                    getFilePathClasses(packageName, filePath, classList, annotation);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        } 
        return classList; 
    } 

    private static void getFilePathClasses(String packageName, String filePath, List<Class<?>> classList,
                                           Class<? extends Annotation> annotation) { 
        Path dir = Paths.get(filePath); 
        DirectoryStream<Path> stream = null; 

        try { 
            stream = Files.newDirectoryStream(dir);
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 

        for (Path path : stream) { 
            String fileName = String.valueOf(path.getFileName()); 
            String className = fileName.substring(0, fileName.length() - 6); 
            Class<?> classes = null; 
            try { 
                classes = Thread.currentThread().getContextClassLoader().loadClass(packageName + "." + className); 
            } catch (ClassNotFoundException e) { 
                e.printStackTrace(); 
            } 
            if (null != classes && null != classes.getAnnotation(annotation)) {
                classList.add(classes);
            } 
        } 
    } 


} 
