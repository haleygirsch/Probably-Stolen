package com.probablystolen.service;

import com.probablystolen.model.WikiCategory;
import com.probablystolen.model.WikiPage;
import com.probablystolen.view.WikiView;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/** The individual page fragments are intentionally easy to edit. */
public final class ContentService {
    public String content(WikiPage page) throws IOException {
        try (InputStream in = getClass().getResourceAsStream("/content/"+page.id()+".html")) {
            return in == null ? missing(page) : new String(in.readAllBytes(),StandardCharsets.UTF_8);
        }
    }
    private String missing(WikiPage page) {
        return "<div class='notice-card'><h2>Draft page</h2><p>The "+ WikiView.escape(page.title())
            +" section is ready for content. Add its HTML in <code>src/main/webapp/content/"
            +page.id()+".html</code>.</p></div>";
    }
    public String map() {
        return """
            <p class='lead'>A quick reference for the places you can visit. This is a navigation index, not a replica of the in-game map.</p>
            <div class='feature-grid columns-3'>
              <a class='feature-card' href='/page/dumping-ground'><span class='feature-icon'>♻</span><h3>Dumping Ground</h3><p>Sort, scavenge and manage unwanted goods.</p><span class='feature-more'>Open guide →</span></a>
              <a class='feature-card' href='/page/commissary'><span class='feature-icon'>▤</span><h3>Commissary</h3><p>Purchase supplies when you are off the clock.</p><span class='feature-more'>Open guide →</span></a>
              <a class='feature-card' href='/page/dr-jackson'><span class='feature-icon'>⚙</span><h3>Dr. Jackson</h3><p>Look for machinery, modules and energy supplies.</p><span class='feature-more'>Open guide →</span></a>
            </div>
            <section class='notice-card'><h2>Need the exact station map?</h2><p>Use the live interactive map at Wikily for location markers and in-game geography.</p><a class='primary-button' href='https://wikily.gg/probably-stolen/interactive-map' target='_blank' rel='noopener noreferrer'>Open the original map ↗</a></section>
            """;
    }
    public String home(List<WikiCategory> categories) {
        StringBuilder grid = new StringBuilder();
        String[] descriptions = {"Items, customers & appraisals", "Requests, trade & daily routines",
            "Conditions, mods & consumables", "Crafting, machines & modules",
            "Plants, water, fish & chemistry", "Nightly stops & recovery",
            "Perks, rent & reputation", "Characters, endings & events", "Controls, manuals & achievements"};
        int i=0;
        for(var cat:categories){
            grid.append("<a class='category-tile' href='/page/")
                .append(cat.pages().get(0).id()).append("'><span class='category-tile-icon'>")
                .append(new String[]{"▣","◷","◇","⚙","♧","☾","⚑","▤","✧"}[i])
                .append("</span><span class='category-tile-name'>")
                .append(WikiView.escape(cat.name())).append("</span><small>")
                .append(descriptions[i++]).append("</small><span class='tile-arrow'>↗</span></a>");
        }
        return """
            <section class='hero'><div class='hero-copy'><p class='hero-kicker'>◇ YOUR LOWER LEVELS FIELD MANUAL ◇</p>
              <h1>PROBABLY<br><span>STOLEN</span></h1><p class='hero-subtitle'>The shopkeeper’s handbook</p>
              <p>A locally hosted companion for the game. Find a system, compare item prices, or follow a character route — without leaving your own project.</p>
              <div class='hero-actions'><button data-open-search class='primary-button' type='button'>⌕ &nbsp; Search the handbook</button><a class='outline-button' href='/page/items'>Browse items →</a></div>
              <div class='hero-footnote'>Fan-made layout · Original content · Your pages are editable</div></div>
              <div class='hero-visual'><img src='/assets/images/shop-scene.svg' alt='Original pixel-style illustration of a futuristic corner shop' /></div></section>
            <section class='section-space'><div class='section-heading'><div><span class='section-eyebrow'>PICK UP THE STORY</span><h2>Storylines players follow</h2></div><a class='section-link' href='/page/storylines'>All storylines ↗</a></div>
            <div class='feature-grid story-grid'>
              <a href='/page/storylines#officer-lun' class='feature-card'><span class='feature-tag'>SECURITY</span><div class='feature-icon'>◈</div><h3>Officer Lun</h3><p>Six visits, a key choice and two possible bounty payouts.</p><span class='feature-more'>View storyline →</span></a>
              <a href='/page/storylines#eleanor' class='feature-card'><span class='feature-tag'>STORY</span><div class='feature-icon'>♡</div><h3>Eleanor</h3><p>Care Package, bail, and her final data-drive decision.</p><span class='feature-more'>View storyline →</span></a>
              <a href='/page/storylines#serpico' class='feature-card'><span class='feature-tag'>SECURITY</span><div class='feature-icon'>⚑</div><h3>Cadet Serpico</h3><p>Plan a food delivery before his rescue window closes.</p><span class='feature-more'>View storyline →</span></a>
              <a href='/page/storylines#georgio' class='feature-card'><span class='feature-tag'>LUCK</span><div class='feature-icon'>✧</div><h3>Georgio</h3><p>Follow his scheduled visits and Liquid Luck opportunities.</p><span class='feature-more'>View storyline →</span></a>
            </div></section>
            <section class='section-space'><div class='section-heading'><div><span class='section-eyebrow'>QUICK REFERENCE</span><h2>Most looked up</h2></div></div>
             <div class='quick-links'><a href='/page/augs'><span>◎</span><strong>Identifying Augs</strong><small>Screen, look, listen, test</small>↗</a>
                <a href='/page/water-grades-and-purification'><span>♧</span><strong>Pure water</strong><small>Quality thresholds</small>↗</a>
                <a href='/page/wine'><span>▥</span><strong>Master Vintage</strong><small>Wine scoring and aging</small>↗</a>
                <a href='/page/plant-traits-and-genetics'><span>✣</span><strong>Gene editing</strong><small>Heat and complexity</small>↗</a></div>
            </section>
            <section class='section-space'><div class='section-heading'><div><span class='section-eyebrow'>EXPLORE</span><h2>Browse the handbook</h2></div></div>
             <div class='category-grid'>%s</div></section>
            <section class='notice-card home-notice'><strong>About this project</strong><p>This is a personal fan-made guide, not a mirror of Wikily. It follows familiar wiki navigation patterns and links to the original site for deeper reference. Specific pages can be expanded with your own notes, images and data.</p></section>
            """.formatted(grid);
    }
}
