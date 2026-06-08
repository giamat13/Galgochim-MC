"""Generate placeholder textures for the Galgochim mod."""
import os
import random
from PIL import Image, ImageDraw

BASE = os.path.join("src", "main", "resources", "assets", "galgochim", "textures")
ENT = os.path.join(BASE, "entity")
ITEM = os.path.join(BASE, "item")
os.makedirs(ENT, exist_ok=True)
os.makedirs(ITEM, exist_ok=True)

random.seed(7)


def noisy(img, box, color, jitter=14):
    d = ImageDraw.Draw(img)
    x0, y0, x1, y1 = box
    for x in range(x0, x1):
        for y in range(y0, y1):
            j = random.randint(-jitter, jitter)
            r = max(0, min(255, color[0] + j))
            g = max(0, min(255, color[1] + j))
            b = max(0, min(255, color[2] + j))
            d.point((x, y), fill=(r, g, b, 255))


def entity_texture(name, body, accent, accent_box, detail, detail_box):
    img = Image.new("RGBA", (64, 64), (0, 0, 0, 0))
    noisy(img, (0, 0, 64, 32), body)        # body region
    noisy(img, (0, 32, 64, 45), detail)     # legs/wheels region
    noisy(img, accent_box, accent)          # head accent
    noisy(img, detail_box, detail)          # beak / snout / extra
    img.save(os.path.join(ENT, name + ".png"))


# Galgoach: grey body, orange head, yellow beak, dark wheels.
entity_texture("galgoach",
               body=(90, 92, 96), accent=(196, 104, 48),
               accent_box=(0, 22, 22, 33),
               detail=(60, 60, 64), detail_box=(24, 0, 36, 10))
# Repaint beak yellow.
g = Image.open(os.path.join(ENT, "galgoach.png")).convert("RGBA")
noisy(g, (24, 0, 36, 10), (230, 190, 40))
g.save(os.path.join(ENT, "galgoach.png"))

# Pilapa: tan body, long legs.
entity_texture("pilapa",
               body=(198, 168, 120), accent=(176, 146, 100),
               accent_box=(0, 20, 22, 32),
               detail=(150, 124, 86), detail_box=(28, 0, 36, 14))

# Karner: dark reddish-brown predator, pale horns.
entity_texture("karner",
               body=(120, 64, 52), accent=(96, 48, 40),
               accent_box=(0, 22, 24, 35),
               detail=(150, 88, 60), detail_box=(28, 0, 36, 6))
k = Image.open(os.path.join(ENT, "karner.png")).convert("RGBA")
noisy(k, (0, 35, 16, 39), (220, 210, 190))  # horns
k.save(os.path.join(ENT, "karner.png"))


def spawn_egg(name, base, spots):
    img = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    d = ImageDraw.Draw(img)
    d.ellipse((4, 1, 11, 14), fill=base + (255,))
    d.ellipse((3, 4, 12, 15), fill=base + (255,))
    random.seed(hash(name) % 1000)
    for _ in range(10):
        x = random.randint(4, 11)
        y = random.randint(3, 14)
        d.point((x, y), fill=spots + (255,))
        d.point((x + 1, y), fill=spots + (255,))
    img.save(os.path.join(ITEM, name + ".png"))


spawn_egg("galgoach_spawn_egg", (90, 92, 96), (220, 120, 40))
spawn_egg("pilapa_spawn_egg", (198, 168, 120), (120, 96, 60))
spawn_egg("karner_spawn_egg", (120, 64, 52), (40, 24, 20))


def music_disc():
    img = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    d = ImageDraw.Draw(img)
    d.ellipse((1, 1, 14, 14), fill=(30, 30, 34, 255))
    d.ellipse((2, 2, 13, 13), outline=(60, 60, 66, 255))
    d.ellipse((5, 5, 10, 10), fill=(214, 120, 40, 255))   # autumn-orange label
    d.ellipse((7, 7, 8, 8), fill=(20, 20, 22, 255))
    img.save(os.path.join(ITEM, "music_disc_galgochim_bastav.png"))


music_disc()
print("textures generated")
