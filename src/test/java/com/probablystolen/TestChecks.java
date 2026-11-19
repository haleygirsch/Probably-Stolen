package com.probablystolen;

/** Assertions that run even when Java's -ea option is not enabled. */
public final class TestChecks {
    private TestChecks() { }

    public static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
