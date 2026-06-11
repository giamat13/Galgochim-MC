"""Generate placeholder textures + item JSON for the issue #1 / #4 additions.

Pure-Python PNG writer (no Pillow dependency) so it runs anywhere.
"""
import json
import os
import random
import struct
import zlib

ROOT = os.path.dirname(os.path.abspath(__file__))
ASSETS = os.path.join(ROOT, "src", "main", "resources", "assets", "galgochim")
ITEM_TEX = os.path.join(ASSETS, "textures", "item")
ENT_TEX = os.path.join(ASSETS, "textures", "entity")
ITEM_DEF = os.path.join(ASSETS, "items")
ITEM_MODEL = os.path.join(ASSETS, "models", "item")
for d in (ITEM_TEX, ENT_TEX, ITEM_DEF, ITEM_MODEL):
    os.makedirs(d, exist_ok=True)


def write_png(path, width, height, pixels):
    """pixels: list of (r,g,b,a) of length width*height (row-major)."""
    def chunk(typ, data):
        return (struct.pack(">I", len(data)) + typ + data
                + struct.pack(">I", zlib.crc32(typ + data) & 0xffffffff))

    raw = bytearray()
    for y in range(height):
        raw.append(0)  # filter type 0 (None)
        for x in range(width):
            r, g, b, a = pixels[y * width + x]
            raw.extend((r & 255, g & 255, b & 255, a & 255))
    sig = b"\x89PNG\r\n\x1a\n"
    ihdr = struct.pack(">IIBBBBB", width, height, 8, 6, 0, 0, 0)
    idat = zlib.compress(bytes(raw), 9)
    with open(path, "wb") as f:
        f.write(sig)
        f.write(chunk(b"IHDR", ihdr))
        f.write(chunk(b"IDAT", idat))
        f.write(chunk(b"IEND", b""))


def noisy_item(name, base, accent, seed):
    """A simple 16x16 blob with a little noise and an accent stripe."""
    rnd = random.Random(seed)
    w = h = 16
    px = [(0, 0, 0, 0)] * (w * h)
    px = list(px)
    for y in range(2, 14):
        for x in range(3, 13):
            jitter = rnd.randint(-16, 16)
            col = accent if (4 <= y <= 7) else base
            px[y * w + x] = (
                max(0, min(255, col[0] + jitter)),
                max(0, min(255, col[1] + jitter)),
                max(0, min(255, col[2] + jitter)),
                255,
            )
    write_png(os.path.join(ITEM_TEX, name + ".png"), w, h, px)


def ship_tex(name, hull, dome, seed):
    rnd = random.Random(seed)
    w = h = 128
    px = [(0, 0, 0, 0)] * (w * h)
    for y in range(h):
        for x in range(w):
            if x < 64 and y < 64:
                col = hull          # hull faces (rough region)
            elif y < 96:
                col = dome
            else:
                col = hull
            j = rnd.randint(-12, 12)
            px[y * w + x] = (
                max(0, min(255, col[0] + j)),
                max(0, min(255, col[1] + j)),
                max(0, min(255, col[2] + j)),
                255,
            )
    write_png(os.path.join(ENT_TEX, name + ".png"), w, h, px)


def item_json(name):
    with open(os.path.join(ITEM_DEF, name + ".json"), "w") as f:
        json.dump({"model": {"type": "minecraft:model",
                              "model": "galgochim:item/" + name}}, f, indent=2)
    with open(os.path.join(ITEM_MODEL, name + ".json"), "w") as f:
        json.dump({"parent": "minecraft:item/generated",
                   "textures": {"layer0": "galgochim:item/" + name}}, f, indent=2)


# Item textures + JSON.
ITEMS = {
    "shmamba": ((226, 198, 120), (180, 120, 60)),
    "shmilki": ((235, 235, 240), (120, 90, 200)),
    "book_ani_yachol_lehasbir": ((150, 60, 60), (220, 200, 120)),
    "laundry_powder": ((220, 230, 245), (120, 160, 210)),
    "chrono": ((80, 80, 110), (180, 120, 220)),
    "sock": ((240, 240, 240), (200, 60, 60)),
}
for i, (name, (base, accent)) in enumerate(ITEMS.items()):
    noisy_item(name, base, accent, seed=100 + i)
    item_json(name)

# Entity (ship) textures.
ship_tex("hagit", hull=(150, 150, 160), dome=(120, 200, 230), seed=11)
ship_tex("alien_ship", hull=(90, 150, 90), dome=(150, 240, 150), seed=22)

print("issue assets generated")
