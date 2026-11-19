package com.probablystolen;
import com.probablystolen.controller.WikiController;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
public final class Main {
    public static HttpServer start(int port) throws java.io.IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1",port),0);
        server.createContext("/",new WikiController()); server.start(); return server;
    }
    public static void main(String[] args) throws Exception {
        int port = args.length==0 ? 8080 : Integer.parseInt(args[0]);
        var server = start(port);
        System.out.println("Open http://localhost:"+server.getAddress().getPort()+"/ in your browser.");
    }
}
