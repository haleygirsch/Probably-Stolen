package com.probablystolen.model;
import java.util.List;
public record WikiCategory(String name, List<WikiPage> pages) {
    public WikiCategory { pages = List.copyOf(pages); }
}
