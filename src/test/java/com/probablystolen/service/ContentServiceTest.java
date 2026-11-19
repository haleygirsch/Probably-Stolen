package com.probablystolen.service;

import com.probablystolen.repository.WikiRepository;
import static com.probablystolen.TestChecks.check;

public final class ContentServiceTest {
    public static void run() throws Exception {
        var service = new ContentService();
        for (var category : new WikiRepository().categories()) {
            for (var page : category.pages()) {
                check(service.content(page).contains("Content coming soon"),
                        "Missing placeholder content: " + page.id());
            }
        }
    }
}
