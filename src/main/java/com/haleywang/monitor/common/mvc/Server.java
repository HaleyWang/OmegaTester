package com.haleywang.monitor.common.mvc;

import com.haleywang.monitor.App;
import com.haleywang.monitor.dto.VersionObj;
import com.haleywang.monitor.utils.FileUtils;
import com.haleywang.monitor.utils.JsonUtils;
import com.haleywang.monitor.utils.PathUtils;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Created by haley on 2018/12/1.
 */
public class Server {

    private Server(){}

    private static final Logger LOG = LoggerFactory.getLogger(Server.class);

    private static final int DEFAULT_PORT = 8000;
    static int nThreads = 500;

    public static void start(String[] args) throws IOException {
        init();
        String args0 = (args != null && args.length >=1) ? args[0] : null ;
        int port = NumberUtils.toInt(args0, DEFAULT_PORT);


        HttpServer server = HttpServer.create(new InetSocketAddress(port), 20);
        server.createContext("/", new StaticResourcesHandler());
        server.createContext("/index.html", new StaticResourcesHandler());
        server.createContext("/js", new StaticResourcesHandler());
        server.createContext("/css", new StaticResourcesHandler());
        server.createContext("/dist", new StaticResourcesHandler());
        server.createContext("/assets", new StaticResourcesHandler());

        server.createContext("/v1", new ApiHandler());

        server.setExecutor(Executors.newFixedThreadPool(nThreads));
        //server.setExecutor(null); // creates a default executor
        server.start();
        String startMessage = String.format("--------------------------------[ server.start at %d ]---------------------------------", port);
        LOG.info(startMessage);
    }

    private static void init() throws IOException{
        if(PathUtils.isStartupFromJar(App.class)) {

            copyJarStaticFiles();

            copyDbDataFromJarToDBFolder();

        }else {
            copyDbDataFromResourceToDBFolder();
        }
    }

    private static void copyDbDataFromResourceToDBFolder() {
        URL dbInitData = App.class.getResource("/data/h2db.mv.db");
        String parentPath =  new File("").getAbsolutePath();

        File dbFolder = new File(parentPath+"/data1");
        File dbFile = new File(parentPath+"/data1/h2db.mv.db");
        if(!dbFile.exists()) {
            FileUtils.ensureDirectoryExists(dbFolder);
            FileUtils.copyResourcesRecursively(dbInitData, dbFolder);
        }
    }

    private static void copyDbDataFromJarToDBFolder() {
        URL dbInitData = App.class.getResource("/data");

        File dbFolder = new File(PathUtils.getRoot()+"/data1");
        File dbFile = new File(PathUtils.getRoot()+"/data1/h2db.mv.db");

        if(!dbFile.exists()) {
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
