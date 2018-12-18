package com.haleywang.monitor.mvc;

import com.sun.net.httpserver.HttpServer;
import lombok.Data;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Created by haley on 2018/12/1.
 */
@Data
public class Server {

    private static final Logger LOG = LoggerFactory.getLogger(Server.class);

    private static final int DEFAULT_PORT = 8000;
    static int nThreads = 500;

    public static void start(String[] args) throws Exception {
        String args0 = (args != null && args.length >=1) ? args[0] : null ;
        int port = NumberUtils.toInt(args0, DEFAULT_PORT);


        HttpServer server = HttpServer.create(new InetSocketAddress(port), 20);
        //server.createContext("/static", new MyHandler());
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

}
