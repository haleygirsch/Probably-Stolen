package com.probablystolen.view;

import com.probablystolen.repository.WikiRepository;
import static com.probablystolen.TestChecks.check;

public final class WikiViewTest {
    public static void run() {
        var repository = new WikiRepository();
        var page = repository.find("items").orElseThrow();
        var html = new WikiView().render(repository.categories(), page, "<p>Test</p>");
        check(html.contains("aria-current='page'"), "Active page not marked");
        check(html.contains("<h1>Items</h1>"), "Page heading missing");
        check(html.contains("<p>Test</p>"), "Page content missing");
        check(WikiView.escape("<script>").equals("&lt;script&gt;"), "HTML escaping failed");
    }
}
