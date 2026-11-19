package com.probablystolen.repository;
import com.probablystolen.model.*;
import java.util.*;
public final class WikiRepository {
    private final List<WikiCategory> categories = List.of(
        category("Shop Counter", "Items", "Customers", "Prices and selling", "Haggling", "Price modifiers", "Stolen goods and heat", "Counterfeits", "Augs", "Appraisal"),
        category("Running the Store", "A day in the store", "Store requests", "Gun orders", "Barter", "Supply crates", "Display case"),
        category("Goods", "Item conditions", "Gun mods", "Food and water"),
        category("Workshop", "Crafting recipes", "Machines", "Modules", "Energy cells", "Storage bays", "Turbo boosters"),
        category("Farming and Processing", "Plants", "Plant traits and genetics", "Animal husbandry", "Aquaculture", "Water grades and purification", "Wine", "Chemistry", "Chemical reactions"),
        category("After Hours", "Dumping Ground", "Commissary", "Dr. Jackson", "Health and wounds"),
        category("Progression", "Progression", "Starting scenarios", "Starting perks", "Difficulty", "Reputation", "Luck", "Joe Wild's network", "Rent and buying the store", "Security and evidence", "Endings"),
        category("Story", "Storylines", "Characters", "Calendar", "Wanted customers", "Store events"));
    private static WikiCategory category(String name, String... titles) {
        return new WikiCategory(name, Arrays.stream(titles).map(title -> new WikiPage(title.toLowerCase(Locale.ROOT).replace("'", "").replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", ""), title, name)).toList());
    }
    public List<WikiCategory> categories() { return categories; }
    public Optional<WikiPage> find(String id) { return categories.stream().flatMap(c -> c.pages().stream()).filter(p -> p.id().equals(id)).findFirst(); }
}
