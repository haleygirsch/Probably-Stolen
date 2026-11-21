package com.probablystolen.repository;

import static com.probablystolen.TestChecks.check;

public final class WikiRepositoryTest {
    public static void run() {
        var repository = new WikiRepository();
        check(repository.categories().size() == 9, "Expected nine navigational categories including Game info");
        check(repository.categories().stream().mapToInt(c -> c.pages().size()).sum() == 55,
                "Expected original 51 pages and four Game info pages");
        check(repository.find("items").isPresent(), "Items page missing");
        check(repository.find("joe-wilds-network").isPresent(), "Joe Wild page missing");
        check(repository.find("manuals-and-guides").isPresent(), "Game info page missing");
        check(repository.find("missing").isEmpty(), "Unexpected missing page");
        check(repository.categories().stream().flatMap(c -> c.pages().stream())
                .map(p -> p.id()).distinct().count() == 55, "Page IDs must be unique");
    }
}
