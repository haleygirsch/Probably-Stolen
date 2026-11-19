package com.probablystolen;

/** Run this main class directly in IntelliJ, without any VM arguments. */
public final class AllTests {
    private AllTests() { }

    public static void main(String[] args) throws Exception {
        com.probablystolen.repository.WikiRepositoryTest.run();
        com.probablystolen.service.ContentServiceTest.run();
        com.probablystolen.view.WikiViewTest.run();
        com.probablystolen.controller.WikiControllerTest.run();
        System.out.println("All four test suites passed.");
    }
}
