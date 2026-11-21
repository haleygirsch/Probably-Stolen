# Editing the Customers page

**Current project:** 137 Wikily customer-directory entries, split into 120 regular and 17 story entries. The wiki.gg site publishes a different split between repeatable customer types and named NPCs. Do not mix these counts.

Most portraits have been replaced with transparent 288 × 211 PNG files from the project owner's `customers.zip`. No old screenshot crops remain; all portrait images are derived solely from customers.zip. Some of the supplied PNGs reuse a sprite-key suffix but depict different people: those have `alt2` filenames and are preserved in the portrait gallery.

## Reference sites
- https://wikily.gg/probably-stolen/customers — customer names, budgets, actions and browsing groups
- https://probablystolen.wiki.gg/wiki/Customers — repeatable customer type catalog
- https://probablystolen.wiki.gg/wiki/Customer_portraits — sprite IDs; **an appearance is not a unique NPC identity**
- https://probablystolen.wiki.gg/wiki/Characters — named character guides

## Where to edit
- `src/main/webapp/assets/customers-data.js` — `PS_CUSTOMER_DATA` and `PS_PORTRAITS` arrays
- `src/main/webapp/assets/customers.js` — filtering, sorting, cards and popup behavior
- `src/main/webapp/assets/customers.css` — the pink page styling
- `src/main/webapp/assets/customers-portrait-manifest.json` — metadata including original uploaded filenames
- `src/main/webapp/assets/images/customers/` — actual PNG artwork

## Customer fields
- `name`: canonical name from the customer index
- `kind`: `Regular` or `Story`
- `faction`: one of the displayed categories, or **Unverified** when an entry lacks a checked faction
- `factions`: array for rare multiple-faction cases
- `budget`: price in credits, e.g. `80–120`, or empty if unverified
- `buys`: comma-delimited item categories verified from reference directories
- `sellsCount`: number of distinct listed sell items (do not use a name heuristic)
- `behavior`: inspection, talk, barter, gifts, and so on, if stated
- `portrait`: basename without `.png`
- `portraitExact`: true only for a known named-character portrait; otherwise the card labels it an example appearance

## Adding more portraits
Add PNGs to the image folder and add a `PS_PORTRAITS` entry with `id`, `group`, `url`, `quality`, and `source`.
For a customer with many possible appearances, choose **one example** for the card and show the rest in the gallery. Keep alt variants as separate gallery records, not additional customers.

The site is an unofficial personal guide. These images are game artwork and should not be treated as freely redistributable without checking relevant licensing/permissions.
