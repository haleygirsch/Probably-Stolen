package com.probablystolen.view;

import com.probablystolen.model.WikiCategory;
import com.probablystolen.model.WikiPage;
import java.util.List;

/** Site shell; page bodies live in src/main/webapp/content. */
public final class WikiView {
    /** Small, locally rendered outline icons, with no external font or network dependency. */
    private static String outlineIcon(String pageId) {
        String type = switch (pageId) {
            case "items", "supply-crates", "storage-bays", "display-case" -> "package";
            case "customers", "characters", "wanted-customers" -> "users";
            case "prices-and-selling", "price-modifiers", "barter", "haggling" -> "tag";
            case "stolen-goods-and-heat", "security-and-evidence", "counterfeits" -> "shield";
            case "augs" -> "scan";
            case "appraisal" -> "gem";
            case "a-day-in-the-store", "calendar", "store-events" -> "clock";
            case "store-requests", "storylines", "manuals-and-guides" -> "list";
            case "gun-orders", "gun-mods", "crafting-recipes" -> "wrench";
            case "item-conditions", "modding", "controls-and-options" -> "settings";
            case "food-and-water", "water-grades-and-purification", "aquaculture" -> "droplet";
            case "machines", "modules", "dr-jackson" -> "cpu";
            case "energy-cells", "turbo-boosters" -> "zap";
            case "plants", "plant-traits-and-genetics" -> "sprout";
            case "animal-husbandry" -> "paw";
            case "wine", "chemistry", "chemical-reactions" -> "flask";
            case "dumping-ground", "commissary" -> "map";
            case "health-and-wounds" -> "heart";
            case "progression", "starting-scenarios", "difficulty", "endings", "achievements" -> "flag";
            case "starting-perks", "reputation", "luck" -> "star";
            case "joe-wilds-network" -> "network";
            case "rent-and-buying-the-store" -> "home";
            default -> "file";
        };
        String drawing = switch (type) {
            case "package" -> "<path d='M21 8 12 3 3 8v9l9 5 9-5V8Z'/><path d='m3.5 8.3 8.5 4.8 8.5-4.8M12 13v9'/>";
            case "users" -> "<path d='M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2M16 3.4a4 4 0 0 1 0 7.2M22 21v-2a4 4 0 0 0-3-3.87'/><circle cx='9' cy='7' r='4'/>";
            case "tag" -> "<path d='M20.6 13.6 13.6 20.6a2 2 0 0 1-2.8 0l-8.4-8.4V3h9.2l9 8.8a2 2 0 0 1 0 2.8Z'/><circle cx='7.5' cy='7.5' r='1'/>";
            case "shield" -> "<path d='M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10Z'/><path d='m9 12 2 2 4-4'/>";
            case "scan" -> "<path d='M4 9V5a1 1 0 0 1 1-1h4M15 4h4a1 1 0 0 1 1 1v4M20 15v4a1 1 0 0 1-1 1h-4M9 20H5a1 1 0 0 1-1-1v-4'/><circle cx='12' cy='12' r='3'/>";
            case "gem" -> "<path d='M6 3h12l4 6-10 13L2 9l4-6ZM2 9h20M6 3l6 6 6-6M12 9v13'/>";
            case "clock" -> "<circle cx='12' cy='12' r='9'/><path d='M12 7v5l3 2'/>";
            case "list" -> "<path d='M9 6h12M9 12h12M9 18h12M4 6h.01M4 12h.01M4 18h.01'/>";
            case "wrench" -> "<path d='M14.7 6.3a5 5 0 0 0-6.6 6.6L3 18a2 2 0 0 0 3 3l5.1-5.1a5 5 0 0 0 6.6-6.6L14 12l-3-3 3.7-2.7Z'/>";
            case "settings" -> "<circle cx='12' cy='12' r='3'/><path d='M19.4 15a2 2 0 0 0 .4 2.2l-2.6 2.6a2 2 0 0 0-2.2-.4l-1.1 1.5h-3.8L9 19.4a2 2 0 0 0-2.2.4l-2.6-2.6a2 2 0 0 0 .4-2.2L3 14v-4l1.6-1a2 2 0 0 0-.4-2.2l2.6-2.6a2 2 0 0 0 2.2.4L10 3h4l1 1.6a2 2 0 0 0 2.2-.4l2.6 2.6a2 2 0 0 0-.4 2.2L21 10v4Z'/>";
            case "droplet" -> "<path d='M12 22a8 8 0 0 0 8-8c0-5-8-12-8-12S4 9 4 14a8 8 0 0 0 8 8Z'/>";
            case "cpu" -> "<rect x='5' y='5' width='14' height='14' rx='2'/><rect x='9' y='9' width='6' height='6'/><path d='M9 1v4m6-4v4M9 19v4m6-4v4M1 9h4m-4 6h4m14-6h4m-4 6h4'/>";
            case "zap" -> "<path d='m13 2-9 12h7l-1 8 10-12h-7l0-8Z'/>";
            case "sprout" -> "<path d='M12 22v-9M12 14C5 14 3 9 3 5c6 0 9 3 9 9ZM12 18c0-8 3-11 9-11 0 6-3 11-9 11Z'/>";
            case "paw" -> "<circle cx='5' cy='9' r='1'/><circle cx='10' cy='5' r='1'/><circle cx='15' cy='5' r='1'/><circle cx='20' cy='9' r='1'/><path d='M12 11c-3 0-6 5-6 7 0 3 3 2 6 2s6 1 6-2c0-2-3-7-6-7Z'/>";
            case "flask" -> "<path d='M9 3h6m-5 0v7l-6 9a2 2 0 0 0 2 3h12a2 2 0 0 0 2-3l-6-9V3M7 16h10'/>";
            case "map" -> "<path d='m3 6 6-3 6 3 6-3v15l-6 3-6-3-6 3V6ZM9 3v15M15 6v15'/>";
            case "heart" -> "<path d='M20.8 4.6a5.5 5.5 0 0 0-7.8 0L12 5.7l-1.1-1.1a5.5 5.5 0 0 0-7.8 7.8L12 21l8.8-8.6a5.5 5.5 0 0 0 0-7.8Z'/>";
            case "flag" -> "<path d='M5 22V3m0 1c5-3 9 3 15 0v11c-6 3-10-3-15 0'/>";
            case "star" -> "<path d='m12 2 3.1 6.3 6.9 1-5 4.8 1.2 6.9-6.2-3.3-6.2 3.3L7 14.1 2 9.3l6.9-1L12 2Z'/>";
            case "network" -> "<circle cx='12' cy='4' r='2'/><circle cx='5' cy='19' r='2'/><circle cx='19' cy='19' r='2'/><path d='M11 6 6 17m7-11 5 11M7 19h10'/>";
            case "home" -> "<path d='m3 10 9-8 9 8v11H3V10ZM9 21v-8h6v8'/>";
            default -> "<path d='M5 2h9l5 5v15H5V2ZM14 2v5h5M8 12h8M8 16h8'/>";
        };
        return "<svg class='nav-svg' aria-hidden='true' viewBox='0 0 24 24' fill='none' stroke='currentColor' stroke-width='1.7' stroke-linecap='round' stroke-linejoin='round'>" + drawing + "</svg>";
    }

    public static String escape(String s) {
        if(s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }

    public String render(List<WikiCategory> categories, WikiPage page, String content) {
        StringBuilder nav = new StringBuilder();
        for (WikiCategory cat : categories) {
            nav.append("<section class='nav-group' data-group='").append(escape(cat.name())).append("'>")
               .append("<h2><span class='rule'></span><span>").append(escape(cat.name()))
               .append("</span><span class='rule'></span></h2>");
            for (WikiPage p : cat.pages()) {
                boolean active = page != null && p.id().equals(page.id());
                nav.append("<a class='nav-link").append(active ? " active" : "")
                    .append("' href='/page/").append(p.id()).append("' data-title='")
                    .append(escape(p.title())).append("' data-category='").append(escape(cat.name()))
                    .append("'").append(active ? " aria-current='page'" : "")
                    .append("><span class='nav-icon' aria-hidden='true'>")
                    .append(outlineIcon(p.id())).append("</span><span class='nav-text'>")
                    .append(escape(p.title())).append("</span>");
                if (p.id().equals("items")) nav.append("<strong class='nav-count'>291</strong>");
                if (p.id().equals("customers")) nav.append("<strong class='nav-count'>137</strong>");
                nav.append("</a>");
            }
            nav.append("</section>");
        }
        String title = page == null ? "Probably Stolen" : page.title();
        String category = page == null ? "HOME" : page.category();
        String originalLink = page == null ? "https://wikily.gg/probably-stolen" :
            "https://wikily.gg/probably-stolen/" + page.id();
        String originalButton = "<a class='outline-button source-link' href='"+escape(originalLink)+"' target='_blank' rel='noopener noreferrer'>Original wiki ↗</a>";
        String heading = page == null || (page.id().equals("customers") || page.id().equals("items")) ? "" : "<div class='page-heading'><div class='eyebrow'>"+escape(category)+" <span class='dot-sep'>/</span> REFERENCE PAGE</div>"
            +"<div class='heading-row'><h1>"+escape(title)+"</h1>"+originalButton+"</div></div>";
        return """
            <!doctype html><html lang='en'><head><meta charset='utf-8'>
            <meta name='viewport' content='width=device-width, initial-scale=1'>
            <meta name='description' content='Unofficial, locally hosted Probably Stolen reference handbook.'>
            <title>%s | Probably Stolen Handbook</title>
            <link rel='stylesheet' href='/assets/style.css'>
            <link rel='stylesheet' href='/assets/customers.css'>
            <link rel='stylesheet' href='/assets/items.css'>
            <script src='/assets/app.js' defer></script>
            <script src='/assets/items-data.js' defer></script>
            <script src='/assets/items.js' defer></script>
            <script src='/assets/customers-data.js' defer></script>
            <script src='/assets/customers.js' defer></script>
            </head><body class='%s'>
            <div class='app-shell'>
             <aside class='sidebar' id='sidebar' aria-label='Site menu'>
              <a class='brand' href='/'><span class='brand-trim'></span><span class='brand-name'>PROBABLY STOLEN</span><span class='brand-sub'>THE SHOPKEEPER'S HANDBOOK</span></a>
              <div class='sidebar-controls'><button class='sidebar-search-launch' data-open-search type='button' aria-label='Search all wiki pages'><span>⌕</span> Search pages... <kbd>⌘ K</kbd></button></div>
              <nav class='primary-nav' aria-label='Wiki pages'>
               <div class='nav-special'><a class='nav-link %s' href='/'><span class='nav-icon'>%s</span><span class='nav-text'>Home</span></a>
                <a class='nav-link %s' href='/page/interactive-map'><span class='nav-icon'>%s</span><span class='nav-text'>Interactive map</span></a></div>
               %s
               <p id='sidebar-empty' hidden class='nav-empty'>No matching pages.</p>
              </nav>
              <div class='barcode' aria-hidden='true'></div>
              <div class='sidebar-foot'><button id='collapse' class='collapse-button' type='button'>‹ COLLAPSE</button></div>
             </aside>
             <div class='sidebar-shade' id='sidebar-shade'></div>
             <div class='app-main'>
              <header class='topbar'><div class='topbar-left'>
                 <button id='toggle' class='icon-button' aria-controls='sidebar' aria-expanded='true' type='button' title='Toggle sidebar'>☰</button>
                 <a class='breadcrumb-home' href='/'>PROBABLY STOLEN</a><span class='breadcrumb-chevron'>›</span><span class='breadcrumb-current'>%s</span>
                 </div><div class='topbar-right'>
                  <button class='top-search' data-open-search type='button'>⌕ <span>Search wiki</span> <kbd>⌘ K</kbd></button>
                 </div></header>
              <div class='content-layout'><main id='main' class='main-content'><article>
                %s<div class='page-content'>%s</div>
              </article><footer><span>♡ Unofficial fan-made handbook for Probably Stolen.</span><span>Not affiliated with Wikily, Questing Goose Studio or tinyBuild.</span></footer></main>
              <aside class='toc' id='toc' aria-label='On this page'><div class='toc-title'>ON THIS PAGE</div><div id='toc-links'></div><div class='toc-source'>"Created for your own local project"</div></aside></div>
             </div>
            </div>
            <dialog id='search-dialog' aria-label='Search wiki pages'><div class='search-modal-head'><span class='search-big-icon'>⌕</span><input id='global-search' placeholder='Search pages, systems, or guides...' autocomplete='off' aria-label='Search pages'><button id='search-close' type='button' aria-label='Close search'>ESC</button></div><div id='search-results' class='search-results'></div><div class='search-help'>Search all navigation pages · ↑↓ choose · Enter open · Esc close</div></dialog>
            </body></html>
            """.formatted(escape(title),page!=null ? (page.id().equals("customers") ? "page-customers" : page.id().equals("items") ? "page-items" : "") : "",page==null?"active":"",outlineIcon("rent-and-buying-the-store"),page!=null&&page.id().equals("interactive-map")?"active":"",outlineIcon("dumping-ground"),nav,escape(category),heading,content);
    }
}
