package com.haleywang.monitor.common.mvc;

import com.haleywang.monitor.App;
import com.haleywang.monitor.dto.VersionObj;
import com.haleywang.monitor.service.impl.ReqBatchServiceImpl;
import com.haleywang.monitor.utils.FileUtils;
import com.haleywang.monitor.utils.JsonUtils;
import com.haleywang.monitor.utils.PathUtils;
import com.sun.net.httpserver.HttpServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author haley
 * @date 2018/12/16
 */
@Slf4j
public class Server {


    private Server(){}

    private static final Logger LOG = LoggerFactory.getLogger(Server.class);

    private static final int DEFAULT_PORT = 8000;
    private static int port = DEFAULT_PORT;


    public static void start(String[] args) throws IOException {
        String args0 = (args != null && args.length >=1) ? args[0] : null ;
        port = NumberUtils.toInt(args0, DEFAULT_PORT);

        init();
        new ReqBatchServiceImpl().initDb(false);

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 20);
        server.createContext("/", new StaticResourcesHandler());
        server.createContext("/index.html", new StaticResourcesHandler());
        server.createContext("/js", new StaticResourcesHandler());
        server.createContext("/css", new StaticResourcesHandler());
        server.createContext("/dist", new StaticResourcesHandler());
        server.createContext("/assets", new StaticResourcesHandler());

        server.createContext("/v1", new ApiHandler());

        int nThreads = 8;

        server.setExecutor(new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(), new DefaultThreadFactory()));

        //server.setExecutor(null); // creates a default executor
        server.start();
        String startMessage = String.format("--------------------------------[ server.start at %d ]---------------------------------", port);
        LOG.info(startMessage);
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI("http://127.0.0.1:" + port));
            } catch (URISyntaxException e) {
                log.warn("open browser: {}", e.getMessage());
            }
        }
    }

    static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        DefaultThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "server-pool-" +
                    ATOMIC_INTEGER.getAndIncrement() +
                    "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }

    public static int getPort() {
        return port;
    }

    private static void init() throws IOException {
        if (PathUtils.isStartupFromJar(App.class)) {

            copyJarStaticFiles();

            copyDbDataFromJarToDbFolder();

        } else {
            copyDbDataFromResourceToDbFolder();
        }
    }

    private static void copyDbDataFromResourceToDbFolder() {
        URL dbInitData = App.class.getResource("/data/h2db.mv.db");
        String parentPath = PathUtils.getRoot();

        File dbFolder = new File(parentPath + "/data1");
        //File jsLibFolder = new File(parentPath + "/data1/js_lib");
        File dbFile = new File(parentPath + "/data1/h2db.mv.db");
        if (!dbFile.exists()) {
            FileUtils.ensureDirectoryExists(dbFolder);
            FileUtils.copyResourcesRecursively(dbInitData, dbFolder);
        }
        //FileUtils.ensureDirectoryExists(jsLibFolder);
    }

    private static void copyDbDataFromJarToDbFolder() {
        URL dbInitData = App.class.getResource("/data");

        File dbFolder = new File(PathUtils.getRoot() + "/data1");
        File dbFile = new File(PathUtils.getRoot() + "/data1/h2db.mv.db");

        if (!dbFile.exists()) {
            FileUtils.ensureDirectoryExists(dbFolder);

            FileUtils.copyResourcesRecursively(dbInitData, dbFolder);
        }
    }

    private static void copyJarStaticFiles() throws IOException {

        File staticFile = new File(PathUtils.getRoot()+"/static");
        File versionFile = new File(PathUtils.getRoot()+"/static/version.json");
        if(versionFile.exists()) {
            InputStream versionStream = App.class.getResourceAsStream("/static/version.json");
            String versionJson1 = IOUtils.readLines(versionStream, "utf-8").stream().collect(Collectors.joining(" "));

            String versionJson2 = org.apache.commons.io.FileUtils.readFileToString(versionFile, "utf-8");
            VersionObj versionObj1 = JsonUtils.fromJson(versionJson1, VersionObj.class);
            VersionObj versionObj2 = JsonUtils.fromJson(versionJson2, VersionObj.class);
            if(versionObj1.getVersion().equals(versionObj2.getVersion())) {
                return;
            }
        }

        FileUtils.ensureDirectoryExists(staticFile);


        URL aa = App.class.getResource("/static");
        FileUtils.copyResourcesRecursively(aa, staticFile);
    }

}
