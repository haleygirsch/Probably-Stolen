package com.probablystolen.view;
import com.probablystolen.model.*;
import java.util.List;
public final class WikiView {
    public static String escape(String s) { return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#39;"); }
    public String render(List<WikiCategory> categories, WikiPage page, String content) {
        StringBuilder nav = new StringBuilder();
        for (WikiCategory c : categories) {
            nav.append("<section class='nav-group'><h2>").append(escape(c.name())).append("</h2>");
            for (WikiPage p : c.pages()) {
                boolean active = page != null && p.id().equals(page.id());
                nav.append("<a class='nav-link").append(active ? " active" : "").append("' href='/page/").append(p.id()).append("'").append(active ? " aria-current='page'" : "").append("><span class='nav-icon' aria-hidden='true'>◇</span>").append(escape(p.title())).append("</a>");
            }
            nav.append("</section>");
        }
        String title = page == null ? "Welcome to your handbook" : page.title();
        String category = page == null ? "YOUR PERSONAL GAME WIKI" : page.category();
        return """
            <!doctype html><html lang='en'><head><meta charset='utf-8'><meta name='viewport' content='width=device-width,initial-scale=1'><title>%s | Probably Stolen</title><link rel='stylesheet' href='/assets/style.css'><script src='/assets/app.js' defer></script></head>
            <body><div class='layout'><aside id='sidebar'><a class='brand' href='/'><span>♡</span> PROBABLY STOLEN<small>THE PINK HANDBOOK</small></a><label class='search-label' for='nav-search'>Find a page</label><input id='nav-search' placeholder='Search tabs…' autocomplete='off'><nav aria-label='Wiki navigation'>%s<p id='no-results' hidden>No matching tabs.</p></nav><button id='collapse' type='button'>‹ Collapse sidebar</button></aside><main><header class='topbar'><button id='toggle' type='button' aria-controls='sidebar' aria-expanded='true'>☰ Menu</button><a href='/'>Home</a><span> / %s</span></header><article><div class='eyebrow'>%s</div><h1>%s</h1>%s</article><footer>Probably Stolen • Unofficial personal fan guide</footer></main></div></body></html>
            """.formatted(escape(title), nav, escape(category), escape(category), escape(title), content);
    }
}
