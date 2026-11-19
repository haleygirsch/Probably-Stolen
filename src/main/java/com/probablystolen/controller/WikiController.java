package com.probablystolen.controller;
import com.probablystolen.repository.WikiRepository;
import com.probablystolen.service.ContentService;
import com.probablystolen.view.WikiView;
import com.sun.net.httpserver.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
public final class WikiController implements HttpHandler {
    private final WikiRepository repository = new WikiRepository();
    private final ContentService service = new ContentService();
    private final WikiView view = new WikiView();
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (!exchange.getRequestMethod().equals("GET")) { send(exchange,405,"text/plain","Method not allowed".getBytes(StandardCharsets.UTF_8)); return; }
            String path = exchange.getRequestURI().getPath();
            if (path.equals("/assets/style.css") || path.equals("/assets/app.js")) {
                try (InputStream in = getClass().getResourceAsStream(path)) {
                    if (in != null) { send(exchange,200,path.endsWith(".css") ? "text/css" : "text/javascript",in.readAllBytes()); return; }
                }
            }
            if (path.equals("/")) { html(exchange,200,view.render(repository.categories(),null,"<p class='intro'>Select a tab to explore your personal game guide.</p><div class='home-grid'>" + repository.categories().stream().map(c -> "<a class='tile' href='/page/"+c.pages().get(0).id()+"'><strong>"+WikiView.escape(c.name())+"</strong><span>"+c.pages().size()+" pages →</span></a>").reduce("",String::concat)+"</div>")); return; }
            if (path.startsWith("/page/")) {
                var page = repository.find(path.substring(6));
                if(page.isPresent()) { html(exchange,200,view.render(repository.categories(),page.get(),service.content(page.get()))); return; }
            }
            html(exchange,404,view.render(repository.categories(),null,"<div class='empty'>Page not found. <a href='/'>Return home</a>.</div>"));
        } finally { exchange.close(); }
    }
    private static void html(HttpExchange e,int status,String body) throws IOException { send(e,status,"text/html",body.getBytes(StandardCharsets.UTF_8)); }
    private static void send(HttpExchange e,int status,String type,byte[] bytes) throws IOException { e.getResponseHeaders().set("Content-Type",type+"; charset=utf-8"); e.getResponseHeaders().set("X-Content-Type-Options","nosniff"); e.sendResponseHeaders(status,bytes.length); try(var out=e.getResponseBody()){out.write(bytes);} }
}
