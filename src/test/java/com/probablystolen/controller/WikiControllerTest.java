package com.probablystolen.controller;

import com.probablystolen.Main;
import com.probablystolen.repository.WikiRepository;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import static com.probablystolen.TestChecks.check;

public final class WikiControllerTest {
    public static void run() throws Exception {
        var server = Main.start(0);
        try {
            var client = HttpClient.newHttpClient();
            String base = "http://127.0.0.1:" + server.getAddress().getPort();
            for (var category : new WikiRepository().categories()) {
                for (var page : category.pages()) {
                    var response = client.send(
                            HttpRequest.newBuilder(URI.create(base + "/page/" + page.id())).build(),
                            HttpResponse.BodyHandlers.ofString());
                    check(response.statusCode() == 200, "Page request failed: " + page.id());
                    check(response.body().contains("Content coming soon"),
                            "Placeholder missing from page: " + page.id());
                }
            }
            for (String path : new String[]{"/", "/assets/style.css", "/assets/app.js"}) {
                var response = client.send(HttpRequest.newBuilder(URI.create(base + path)).build(),
                        HttpResponse.BodyHandlers.ofString());
                check(response.statusCode() == 200, "Static route failed: " + path);
            }
            var missing = client.send(HttpRequest.newBuilder(URI.create(base + "/page/missing")).build(),
                    HttpResponse.BodyHandlers.ofString());
            check(missing.statusCode() == 404, "Unknown pages should return 404");
        } finally {
            server.stop(0);
        }
    }
}
