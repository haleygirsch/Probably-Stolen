package com.probablystolen.controller;

import com.probablystolen.repository.WikiRepository;
import com.probablystolen.service.ContentService;
import com.probablystolen.view.WikiView;
import com.sun.net.httpserver.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

/** Serves the local site and only explicitly approved web assets. */
public final class WikiController implements HttpHandler {
    private final WikiRepository repository = new WikiRepository();
    private final ContentService service = new ContentService();
    private final WikiView view = new WikiView();

    @Override public void handle(HttpExchange exchange) throws IOException {
        try {
            if (!"GET".equals(exchange.getRequestMethod())) {
                send(exchange,405,"text/plain","Method not allowed".getBytes(StandardCharsets.UTF_8)); return;
            }
            String path = exchange.getRequestURI().getPath();
            if (path.matches("/assets/(style\\.css|app\\.js|items\\.js|items-data\\.js|items\\.css|customers\\.css|customers-data\\.js|customers\\.js|images/[a-z0-9-]+\\.svg|images/customers/[A-Za-z0-9_-]+\\.png|images/items/[a-z0-9-]+\\.png)")) {
                try (InputStream in = getClass().getResourceAsStream(path)) {
                    if (in != null) {
                        String mime = path.endsWith(".css") ? "text/css" : path.endsWith(".js") ? "text/javascript" : path.endsWith(".png") ? "image/png" : "image/svg+xml";
                        send(exchange,200,mime,in.readAllBytes()); return;
                    }
                }
            }
            if (path.equals("/")) {
                html(exchange,200,view.render(repository.categories(),null,service.home(repository.categories()))); return;
            }
            if (path.startsWith("/page/")) {
                String slug = path.substring(6);
                if (slug.equals("interactive-map")) {
                    html(exchange,200,view.render(repository.categories(),
                            new com.probablystolen.model.WikiPage("interactive-map","Interactive map","TOOLS"),service.map()));
                    return;
                }
                var page = repository.find(slug);
                if (page.isPresent()) {
                    html(exchange,200,view.render(repository.categories(),page.get(),service.content(page.get())));
                    return;
                }
            }
            html(exchange,404,view.render(repository.categories(),null,
                    "<section class='notice-card'><h2>Page not found</h2><p>This route does not exist in your local handbook.</p><a class='primary-button' href='/'>Return home</a></section>"));
        } finally { exchange.close(); }
    }
    private static void html(HttpExchange e,int status,String body) throws IOException {
        send(e,status,"text/html",body.getBytes(StandardCharsets.UTF_8));
    }
    private static void send(HttpExchange e,int status,String type,byte[] bytes) throws IOException {
        e.getResponseHeaders().set("Content-Type",type+"; charset=utf-8");
        e.getResponseHeaders().set("X-Content-Type-Options","nosniff");
        e.getResponseHeaders().set("Cache-Control","no-store");
        e.getResponseHeaders().set("Referrer-Policy","strict-origin-when-cross-origin");
        e.sendResponseHeaders(status,bytes.length);
        try(var out=e.getResponseBody()){out.write(bytes);}
    }
}
