# Probably Stolen — Local Wiki Handbook

A Java 17+ / Maven website built from your uploaded `Probably_Stolen` project. This edition adds the original soft pink wiki skin, all original navigation tabs, Game info pages, local page search, item filters, sortable item cards/table, and consistent pink styling.

**Original fan project.** Not affiliated with Wikily, Questing Goose Studio or tinyBuild. It does not bulk copy Wikily's articles or logos. The **customer portrait images** in this edition are supplied by the project owner, exclusively transparent PNGs from customers.zip; these pictures depict copyrighted in-game artwork. Confirm appropriate permissions before redistributing the asset pack. The item catalog is a *curated sample* using facts provided in chat, not a complete live wiki mirror. Item artwork is taken from the screenshots supplied by the project owner, where available; other item entries have placeholders. Original SVG illustrations remain for the homepage and category guides.

## Open in IntelliJ IDEA

1. Extract this ZIP to your preferred project location.
2. In IntelliJ, **File > Open** and choose the folder containing `pom.xml`.
3. Wait for Maven source folders to index; set Project SDK to Java 17 or newer.
4. Run `src/main/java/com/probablystolen/Main.java` as a Java Application.
5. Open http://localhost:8080/ in Safari or Chrome.

If port 8080 is occupied, set program argument `8081` and open http://localhost:8081/ .

To run the tests without `-ea`, run `src/test/java/com/probablystolen/AllTests.java`. Or use `mvn test`.

## Maven commands

```bash
mvn clean package
java -jar target/probably-stolen-wiki-1.0.0.jar
mvn test
```

## Directory structure

```text
src/main/java/com/probablystolen/   Java server, controller, model, repository, service, view
src/main/resources/              Optional configuration and assets
src/main/webapp/assets/          CSS, JavaScript, local SVG illustrations
src/main/webapp/content/         One editable HTML fragment per wiki tab
src/test/java/com/probablystolen/ Four test suites + standalone AllTests runner
pom.xml                           Maven build and executable JAR configuration
```

## New: Customers page and portraits

Open **Shop Counter → Customers**. The page now follows the requested search/filter/card/table structure while keeping the soft pink theme.

- 137 distinct customer-type names from the online customer directory, with reference buying/budget information where verified. Other fields are deliberately left unfilled rather than invented.
- 209 transparent PNG sprite variants supplied by the project owner; no older screenshot crops are used. Some uploaded sprite IDs have distinct alternate appearances. A customer type and a portrait are not the same thing: some types have multiple possible appearances.
- Switch **Customer directory / Portrait gallery**. The directory supports name search, Regular/Story tabs, faction filters, documented buyers/sellers, budget or name sorting, ascending/descending order, card/table mode, pagination, and customer details. The gallery adds sprite group/source filters and natural numeric sorting.
- The gallery supports portrait group filters, sprite-name search, card/table view, pagination, and an enlarged portrait popup.
- A generic customer card may use a **representative** sprite, explicitly marked as such, rather than falsely presenting it as a unique official portrait.
- Customer list and portrait mapping: `src/main/webapp/assets/customers-data.js` (generated JS data). For easier edits, change `tools/customer-notes.md` or modify the row objects directly.
- User interface logic: `src/main/webapp/assets/customers.js`. Styles: `src/main/webapp/assets/customers.css`. Layout: `src/main/webapp/content/customers.html`.
- Portrait files: `src/main/webapp/assets/images/customers/`. Source-filename mapping: `src/main/webapp/assets/customers-portrait-manifest.json`. The assets are derived only from the customers.zip PNGs; transparent outer margins have been trimmed to make the portraits larger.

**Source data caveat:** The faction filters reflect locally classified customers, not the complete wiki's exact faction counts. Unverified factions are explicitly labelled **Unverified**, rather than incorrectly treated as factionless. Wikily and wiki.gg group customer types differently, so their headline totals may differ.

## What was changed

- **Java packages retained.** `controller`, `model`, `repository`, `service`, and `view` are still used.
- **Original navigation preserved.** The 51 original sections remain. Four additional Game info pages and a special Interactive map page were added.
- **Home page enhanced.** Storyline cards, system shortcuts, game category grid and original SVG storefront illustration.
- **Search works.** Click the search buttons or press `⌘K` on Mac (`Ctrl+K` on Windows). Search filters local tabs, not remote Wikily articles.
- **Item catalog works.** Text search, category and legality filters, sorting, table/card view, pagination. Data is edited in `src/main/webapp/assets/items.js` (one line per item in the `RAW` string, `Name|Credits|Category|Legality`).
- **Pink layout restored.** Soft pink sidebar, blush accents, pink cards and rose buttons are always used (no cream mode switch).
- **Page content upgraded.** User-supplied transcript details for Augs, Hydroponics, Water, Wine, Chemistry, Genetics, Fish and story choices are summarized in individual HTML fragments. Every tab now has a concise source-backed reference section; major story routes have expanded step-by-step consequences.
- **No external runtime dependencies.** Illustrations and styling load from the project itself. Original-site links open only when clicked.

## Extend a page

To update Hydroponics, edit `src/main/webapp/content/plants.html`. For Storylines, edit `storylines.html`. Save the file and **restart the Java app** so Maven/IntelliJ recopy resources into the classpath. Then refresh the browser.

To add an entirely new tab:
1. Add the page name to `WikiRepository.java` under the desired category.
2. Create `src/main/webapp/content/<page-id>.html`.
3. Update tests if the number of pages changes.

For additional authorized portraits, place PNG files under `src/main/webapp/assets/images/customers/` and update `customers-data.js`. The controller safely serves whitelisted PNG portrait filenames alongside the existing SVG icons.

## Known limitations

The site is a working local handbook and navigation system, **not** a complete Wikily data mirror. The local catalog contains 291 items and 137 customer profiles; some detailed fields are pending verification. The sidebar counts refer to the local data. The character routes and reference pages contain independent summaries, not exhaustive reproductions of external articles. Some very detailed data tables (all recipes, customers, every trait) still require further research or future user input. Links to the original wiki are for personal reference only; some source page slugs may differ from the local page IDs.

## November 2026 pink directory update

- **Customers** (`/page/customers`): 137 profile entries; 209 portrait variants built **only** from `customers.zip`. Empty transparent padding was trimmed from each image so the game sprites can display larger. No previous screenshot portraits are included. Portrait IDs beginning with `BM`, `Rev`, `Scav`, `Upper`, and `Sec` are assigned to Black Market, Revolution, Lower Levels, Upper Levels and Security, respectively (case-insensitive and including `male`/`female` prefixes).
- **Items** (`/page/items`): 291 locally indexed names, **228 cropped pixel-art item icons** from the screenshots uploaded in this conversation. Searchable by name, type, legality, price, with Cards/Table views, sorting, and a details window. The remaining 63 items remain searchable but have no supplied icon yet. The catalog is not a full copy of the live Wiki.
- Image assets live at `src/main/webapp/assets/images/customers/` and `src/main/webapp/assets/images/items/`.
- `src/main/webapp/assets/items-data.js` is the editable item catalog. `customers-data.js` contains customer profiles and the supplied portrait manifest.
- Each icon crop uses the game artwork visible in the user-provided screenshot; advertisements and surrounding card UI are not included. Items not yet illustrated show a placeholder.

## November 21, 2026 — Storylines and remaining reference tabs

The existing 55 HTML tab files were updated without changing Maven source-folder conventions, the pink theme, the 291-item local database or 209 supplied customer PNGs. All former empty guide templates now have topic-specific explanations, selected exact mechanics, practical checklists and direct links to their supporting source pages.

- **Characters:** 33 named or recurring visitors sorted into Story/Services, Wanted visitors, Other visitors, and Sector 13 Cartel, with exact locally supplied portrait PNGs for named individuals where available. Missing original portraits are not substituted with unrelated sprites.
- **Storylines:** Expanded interactive jump index and consequence-focused walkthroughs for Lou, Eleanor, Cadet Serpico, Officer Lun, Georgio, Li Bei Wen, Joe Wild, Sawyer, Odin and John Smith. Includes chronology, important deadlines, wanted rewards, and game-over warnings.
- **Calendar / Wanted customers:** Filled with the referenced story timetable and wanted rewards.
- **Other tabs:** The previously blank shopping, workshop, farming, after-hours, and progression topics now contain verified introductory data and source links. Their extensive external tables are linked rather than copied in bulk.

**Sources:**
- https://wikily.gg/probably-stolen/characters
- https://wikily.gg/probably-stolen/storylines
- https://wikily.gg/probably-stolen/storylines/lou
- https://probablystolen.wiki.gg/wiki/Lou/Guide
- Individual Wikily topic pages linked at the bottom of each local page.

The wiki.gg `Lou/Guide` URL could not be directly retrieved during this edit; Lou's detailed progression was cross-checked against Wikily's Lou storyline, while the wiki.gg guide remains linked for further reading. Some content depends on current game version and difficulty. Every page is an original compact summary, **not a complete copy** of the wiki's articles, full customer database or exhaustive crafting recipes.

Run `src/test/java/com/probablystolen/AllTests.java` in IntelliJ or use `mvn test` after importing the project. Tests now also confirm that draft-template phrases do not remain and that the storyline/character guides are present.

## November 2026: navigation and reference update

- **Sidebar position is preserved** when a page is opened. The sidebar stores its own scroll offset in sessionStorage and restores it after navigation. The old page-load `scrollIntoView()` behavior was removed. The main reading area still opens at the new page normally.
- **Line-art sidebar icons** are inline SVG shapes rendered by `WikiView.java`. No external fonts or icon libraries are needed.
- **Expanded reference data**: 20 system pages now include tables with numerical mechanics and practical notes, in addition to their existing source-backed summaries. All 55 prior content pages, character portraits and local item images are retained.
- **Source links**: references throughout the guide link back to the public game wikis. The wiki.gg site sometimes blocks automated retrieval; the updated detailed reference content is primarily based on the accessible Wikily articles. The local guide does **not** mirror the sites' entire copyrighted prose or artwork.

### Testing the sidebar behavior

1. Run `Main.java` in IntelliJ and open the site in Safari or Chrome.
2. Scroll the left navigation down to Story or Farming and Processing.
3. Click `Wine` or `Characters` and confirm the sidebar stays at the same vertical offset; the main content can open at its top.
4. Use the search button or keyboard shortcut to navigate to another page; confirm the sidebar remains at the previously selected position.
5. Collapse and reopen the menu; the navigation still keeps its scroll position within the current session.

The sidebar icons are embedded inline in page HTML for reliable offline display.
