package com.probablystolen.service;

import com.probablystolen.repository.WikiRepository;
import static com.probablystolen.TestChecks.check;

public final class ContentServiceTest {
    public static void run() throws Exception {
        var service = new ContentService();
        for (var category : new WikiRepository().categories()) {
            for (var page : category.pages()) {
                String html = service.content(page);
                check(!html.isBlank(), "Blank content: " + page.id());
                check(!html.contains("Content coming soon"), "Old placeholder remains: " + page.id());
                check(!html.contains("Section prepared"), "Unfilled template remains: " + page.id());
                check(!html.contains("Information will be added here"), "Empty reference table remains: " + page.id());
                check(!html.contains("Ready for your game notes"), "Draft page remains: " + page.id());
            }
        }
        var characterPage = new WikiRepository().find("characters").orElseThrow();
        var storyPage = new WikiRepository().find("storylines").orElseThrow();
        check(service.content(characterPage).contains("character-card-grid"), "Character directory not rendered");
        check(service.content(characterPage).contains("/assets/images/customers/lou_sec.png"), "Lou game portrait missing");
        check(service.content(storyPage).contains("Box of Toys"), "Lou route missing");
        check(service.content(storyPage).contains("2,000 kcal"), "Serpico food rescue missing");
        check(service.home(new WikiRepository().categories()).contains("Storylines players follow"),
                "Home features missing");
        check(service.map().contains("Dumping Ground"), "Map index missing");
    }
}
