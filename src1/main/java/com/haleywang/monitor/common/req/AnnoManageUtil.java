package com.haleywang.monitor.common.req;

import com.haleywang.monitor.utils.AnnotationUtils;
import com.haleywang.monitor.utils.FileTool;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * @author haley
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public final class AnnoManageUtil {

    public static final String SCAN_ERROR = "scan error";
    public static final String CLASS_STRING = "class";

    public static Set<Class<?>> getClasses(String pack) {

        Set<Class<?>> classes = new LinkedHashSet<>();
        boolean recursive = true;
        String packageName = pack;
        String packageDirName = packageName.replace('.', '/');
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(
                    packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    log.info("file_scam");
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findAndAddClassesInPackageByFile(packageName, filePath,
                            recursive, classes);
                } else if ("jar".equals(protocol)) {
                    packageName = scanInJar(classes, recursive, packageName, packageDirName, url);
                }
            }
        } catch (IOException e) {
            log.error("scan error:", e);
        }

        return classes;
    }

    private static String scanInJar(Set<Class<?>> classes, boolean recursive, String packageName, String packageDirName, URL url) {
        log.info("jar_scan");
        JarFile jar;
        try {
            jar = ((JarURLConnection) url.openConnection())
                    .getJarFile();
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                packageName = scanJarEntry(classes, recursive, packageName, packageDirName, entry);

            }
        } catch (IOException e) {
            log.error("jar scan error:", e);
        }
        return packageName;
    }

    private static String scanJarEntry(Set<Class<?>> classes, boolean recursive, String packageName, String packageDirName, JarEntry entry) {
        String name = entry.getName();
        char pathDelimiter = FileTool.PATH_DELIMITER_CHAR;
        char nameDelimiter = FileTool.NAME_DELIMITER_CHAR;
        if (name.charAt(0) == pathDelimiter) {
            name = name.substring(1);
        }
        if (!name.startsWith(packageDirName)) {
            return packageName;
        }
        int idx = name.lastIndexOf(pathDelimiter);
        if (idx != -1) {
            packageName = name.substring(0, idx)
                    .replace(pathDelimiter, nameDelimiter);
        }
        if (idx != -1 || recursive) {
            if (name.endsWith(nameDelimiter + CLASS_STRING)
                    && !entry.isDirectory()) {

                String className = name.substring(
                        packageName.length() + 1, name
                                .length() - 6);
                try {
                    classes.add(Class
                            .forName(packageName + nameDelimiter
                                    + className));
                } catch (ClassNotFoundException e) {
                    log.error("can not find " + CLASS_STRING + " file:", e);
                }
            }
        }
        return packageName;
    }

    public static void findAndAddClassesInPackageByFile(String packageName,
                                                        String packagePath, final boolean recursive, Set<Class<?>> classes) {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            log.warn("package empty:", packageName);
            return;
        }
        File[] dirfiles = dir.listFiles(file -> (recursive && file.isDirectory())
                || (file.getName().endsWith("." + CLASS_STRING)));

        if (dirfiles == null) {
            return;
        }

        for (File file : dirfiles) {
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "."
                                + file.getName(), file.getAbsolutePath(), recursive,
                        classes);
            } else {
                String className = file.getName().substring(0,
                        file.getName().length() - 6);
                try {
                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    log.error("findAndAddClassesInPackageByFile error:", e);
                }
            }
        }
    }

    public static List<Class<?>> scan(String packageName, Class<? extends Annotation> annotation) {

        Set<Class<?>> ss = getClasses(packageName);
        log.info("scan size-->: {}", ss.size());
        return ss.stream().filter(it -> AnnotationUtils.findAnnotation(it, annotation) != null).collect(Collectors.toList());

    }

    private static void getFilePathClasses(String packageName, URI filePath, List<Class<?>> classList,
                                           Class<? extends Annotation> annotation) {
        Path dir = Paths.get(filePath);


        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path path : stream) {
                String fileName = String.valueOf(path.getFileName());
                if (fileName == null || !fileName.endsWith("." + CLASS_STRING)) {
                    continue;
                }
                String className = fileName.substring(0, fileName.length() - 6);

                getClsByAnn(packageName, classList, annotation, className);

            }
        } catch (IOException e) {
            log.error(SCAN_ERROR, e);
        }
    }

    private static void getClsByAnn(String packageName, List<Class<?>> classList, Class<? extends Annotation> annotation, String className) {
        try {
            Class<?> classes = Thread.currentThread().getContextClassLoader().loadClass(packageName + "." + className);
            if (null != classes && null != classes.getAnnotation(annotation)) {
                classList.add(classes);
            }
        } catch (ClassNotFoundException e) {
            log.error(SCAN_ERROR, e);
        }
    }


} 
