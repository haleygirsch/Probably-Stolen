package com.probablystolen.view;

import com.probablystolen.repository.WikiRepository;
import static com.probablystolen.TestChecks.check;

public final class WikiViewTest {
    public static void run() {
        var repository = new WikiRepository();
        var page = repository.find("items").orElseThrow();
        var html = new WikiView().render(repository.categories(), page, "<p>Test</p>");
        check(html.contains("aria-current='page'"), "Active page not marked");
        check(html.contains("page-items"), "Items page CSS class missing");
        check(html.contains("items.css") && html.contains("items-data.js"), "Items stylesheet or data script missing");
        check(html.contains("<p>Test</p>"), "Page content missing");
        check(html.contains("search-dialog"), "Global search dialog missing");
        check(html.contains("class='nav-svg'"), "Sidebar outline SVG icons missing");
        check(html.contains("stroke='currentColor'"), "Sidebar icon stroke styling missing");
        check(!html.contains("theme-toggle"), "Pink layout must stay consistent");
        check(WikiView.escape("<script>").equals("&lt;script&gt;"), "HTML escaping failed");
    }
}
