package com.probablystolen.controller;

import com.probablystolen.Main;
import com.probablystolen.repository.WikiRepository;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import static com.probablystolen.TestChecks.check;

public final class WikiControllerTest {
    private static HttpResponse<String> get(HttpClient client, String base, String path) throws Exception {
        return client.send(HttpRequest.newBuilder(URI.create(base + path)).build(),
                HttpResponse.BodyHandlers.ofString());
    }
    public static void run() throws Exception {
        var server = Main.start(0);
        try {
            var client = HttpClient.newHttpClient();
            String base = "http://127.0.0.1:" + server.getAddress().getPort();
            for (var category : new WikiRepository().categories()) {
                for (var page : category.pages()) {
                    var response = get(client,base,"/page/"+page.id());
                    check(response.statusCode() == 200, "Page request failed: " + page.id());
                    check(response.body().contains("page-content"),
                            "Page template missing: " + page.id());
                }
            }
            for (String path : new String[]{"/", "/page/interactive-map", "/assets/style.css",
                    "/assets/app.js", "/assets/items.js", "/assets/items.css", "/assets/items-data.js",
                    "/assets/images/items/aug-scanner.png", "/assets/images/items/3d-printer.png", "/assets/images/shop-scene.svg",
                    "/assets/images/water.svg", "/assets/customers.css", "/assets/customers.js", "/assets/customers-data.js", "/assets/images/customers/eleanor1.png", "/assets/images/customers/dr_jackson.png", "/assets/images/customers/john_smith.png", "/assets/images/customers/li_bei_wen.png", "/assets/images/customers/odin_haraldsson.png", "/assets/images/customers/maleLower1.png", "/assets/images/customers/maleBM1_alt2.png"}) {
                check(get(client,base,path).statusCode() == 200, "Static route failed: " + path);
            }
            check(get(client,base,"/page/items").body().contains("catalog-app"), "Item catalog missing");
            check(get(client,base,"/assets/items-data.js").body().contains("PS_ITEMS"), "Item data missing");
            check(get(client,base,"/assets/images/items/aug-scanner.png").headers().firstValue("content-type").orElse("").contains("image/png"), "Item icon MIME incorrect");
            check(!get(client,base,"/assets/customers-data.js").body().contains("Earlier screenshot crop"), "Screenshot crops still referenced");
            check(get(client,base,"/page/customers").body().contains("customers-dashboard"), "Customer page missing");
            check(get(client,base,"/assets/customers-data.js").body().contains("PS_PORTRAITS"), "Portrait registry missing");
            check(get(client,base,"/assets/customers-data.js").body().contains("dr_jackson"), "New named portrait mapping missing");
            check(get(client,base,"/assets/customers-data.js").body().contains("Unverified"), "Unverified faction handling missing");
            check(get(client,base,"/assets/images/customers/eleanor1.png")
                    .headers().firstValue("content-type").orElse("").contains("image/png"), "PNG MIME incorrect");
            check(get(client,base,"/page/missing").statusCode() == 404, "Unknown pages should return 404");
            check(get(client,base,"/assets/other.js").statusCode() == 404, "Unknown assets should return 404");
        } finally { server.stop(0); }
    }
}
