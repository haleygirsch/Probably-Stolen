package com.probablystolen.repository;

import static com.probablystolen.TestChecks.check;

public final class WikiRepositoryTest {
    public static void run() {
        var repository = new WikiRepository();
        check(repository.categories().size() == 8, "Expected 8 categories");
        check(repository.categories().stream().mapToInt(c -> c.pages().size()).sum() == 51,
                "Expected 51 pages");
        check(repository.find("items").isPresent(), "Items page missing");
        check(repository.find("joe-wilds-network").isPresent(), "Joe Wild page missing");
        check(repository.find("missing").isEmpty(), "Unexpected missing page");
        check(repository.categories().stream().flatMap(c -> c.pages().stream())
                .map(p -> p.id()).distinct().count() == 51, "Page IDs must be unique");
    }
}
