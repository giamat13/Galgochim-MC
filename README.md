# Galgochim

**Galgochim** is a Fabric mod that adds three brand-new creatures — and three original
music discs — to Minecraft. It started as a kid's drawing and grew into a small living
ecosystem you can find while exploring.

---

## ✨ Features

- 🐔 **Galgoach** — a wheeled, chicken-like bird
- 🦒 **Pilapa** — a tall, long-legged grazer (breeding can produce a **faster, taller** variant)
- 🐺 **Karner** — a predator that hunts Pilapot
- 🛸 **Hagit spaceship** & 👽 **alien ship** — drift across the night sky
- 🧑‍🎤 **Doron Fishler** — a villager whose workstation is a **microphone**
- 🧺 **Washing machine** & **microphone** blocks
- 🍿 New items: **Shmamba**, **Shmilki**, the book **"I can explain"**, **laundry powder**,
  the **chrono-thing**, and single **socks**
- 💿 **3 original music discs** you can play in a jukebox
- 🌍 Natural spawning across forests, plains and savanna
- 🥚 Spawn eggs, discs, blocks and items available in the vanilla creative menu

---

## 🆕 Issue #4 — Pilapa breeding variant

Breeding two Pilapot has a **20% chance** to produce an improved Pilapa: it runs
**faster** and stands **taller** (long legs!). The boost is permanent and survives reloads.

---

## 🆕 Issue #1 — Doron, spaceships, washing machines & more

- **Doron Fishler** (דורון פישלר): a villager profession whose job site is the **microphone**
  block. Craft and place a microphone near an unemployed villager to make a Doron.
- **Hagit spaceship** (ספינת החלל חגית): drifts overhead at night. Shoot it with an arrow and
  it drops the **chrono-thing**. Summon/toggle it with **`/tooglehgit`**.
- **Alien ship** (חללית חייזרים): at night it vacuums up your **laundry powder** — but only if
  you're standing under open sky — and 25% of the time turns a pair of leather boots into a
  single **sock**. Summon/toggle it with **`/tooglealians`**.
- **Washing machine**: full when placed; right-click it for **64 laundry powder**. A full
  machine under open sky is **bait** that lures the alien ship; building a **roof** over it
  keeps the powder safe. Turns up in ~50% of village house chests.
- **Chrono-thing**: using it costs 5 health, with a 25% chance to lose an extra 3–20, and a
  10% chance to die on the spot — and it hands you a **Shmilki that fizzles away** a second later.
- **Socks**: use a sock to combine two of the **same colour** back into dyed leather boots.
- **Wilhelm scream**: a 1% chance to play on any hit.

### 🔊 Placeholder audio
`wilhelm_scream.ogg` and `amar_hakaptan.ogg` are currently placeholders — see
[SOUNDS_TODO.md](SOUNDS_TODO.md) to drop in the real audio.

### 🔜 Documented follow-ups
A few sub-features depend on heavily reworked MC 26.1.2 systems and are left as follow-ups:
- **Doron's trades** — villager trades are now fully data-driven
  (`Int2ObjectMap<ResourceKey<TradeSet>>` + `trade_set` / `villager_trade` datapack registries).
  Doron is registered as a working profession; his Shmamba/Shmilki/book trade sets need
  datapack JSON authored against the new schema. All those items are obtainable via the creative
  menu / village loot meanwhile.
- **Jukebox proximity effects** (Shekem discount near Doron, boat acceleration near the
  "Tekef Yavo Shekemist" disc) and **auto-generated signs in abandoned villages** — pending.

---

## 🐾 The Creatures

### 🐔 Galgoach (גלגוח)
Like a chicken, but bigger — and instead of legs it rolls around on a pair of **wheels**.
It does **not** lay eggs. Tempt and breed it with **wheat seeds**.

> When killed, a Galgoach has a **75% chance** to drop the **"Galgochim baStav"** music disc.

Spawns in **forests** and **plains**.

### 🦒 Pilapa (פילאפה)
A tall, peaceful animal that stands on **long legs**. Tempt and breed it with **wheat**.
It will **sprint away** from any Karner that comes hunting.

Spawns in the **savanna**.

### 🐺 Karner (קרנר)
A predator that roams the savanna **day and night**, hunting Pilapot to eat them.
It won't attack players unprovoked — its eyes are on the Pilapot.

Spawns in the **savanna**.

---

## 💿 Music Discs

| Disc | How to get it |
|------|----------------|
| **Galgochim baStav** (גלגוחים בסתיו) | 75% drop from a slain Galgoach |
| **Tekef Yavo Shekemist** (תכף יבוא שקמיסט) | Almost always found in **shipwreck treasure** chests |
| **Fishland Anthem** (המנון פישלנד) | A chance to find in **village** chests (also sold by Doron, once trade sets land) |

Pop one into a jukebox and give it a listen. 🎵

---

## 📦 Requirements

- Minecraft **26.1.2**
- **Fabric Loader** ≥ 0.19.3
- **Fabric API**
- **Java 25**

---

## 🎮 Getting the Items

All spawn eggs and music discs appear in the **vanilla creative tabs**
(Spawn Eggs, and Tools & Utilities alongside the other discs). In survival, find the
creatures and discs out in the world as described above. You can also use commands, e.g.:

```
/give @s galgochim:galgoach_spawn_egg
/give @s galgochim:music_disc_galgochim_bastav
```

---

## 📜 License

Released under the **MIT License**. See [LICENSE](LICENSE) for details.
