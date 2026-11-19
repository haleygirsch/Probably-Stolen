package com.probablystolen.service;
import com.probablystolen.model.WikiPage;
import java.io.*;
import java.nio.charset.StandardCharsets;
public final class ContentService {
    public String content(WikiPage page) throws IOException {
        try (InputStream in = getClass().getResourceAsStream("/content/" + page.id() + ".html")) {
            return in == null ? "<div class='empty'>Content coming soon.</div>" : new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
