# Sound assets to replace

Two sound files in `src/main/resources/assets/galgochim/sounds/` are currently
**placeholders** (copies of existing tracks). They are wired up and the build is
green, but they play the wrong audio until you drop in the real files. They could
not be produced automatically in the build environment (no `ffmpeg`/`oggenc`, and
`upload.wikimedia.org` is blocked by the network policy).

| File | Should contain | Notes |
|------|----------------|-------|
| `wilhelm_scream.ogg` | The Wilhelm scream | Download from <https://upload.wikimedia.org/wikipedia/commons/d/d9/Wilhelm_Scream.ogg> and convert to Ogg Vorbis. Played 1% of the time on any hit. |
| `amar_hakaptan.ogg` | `אמר הקפטן .mp3` (in the repo root) converted to Ogg Vorbis | Heard from the Hagit spaceship. |

### Converting `אמר הקפטן .mp3` to ogg
```sh
ffmpeg -i "אמר הקפטן .mp3" -c:a libvorbis -q:a 5 \
  src/main/resources/assets/galgochim/sounds/amar_hakaptan.ogg
```

Just overwrite the placeholder `.ogg` with the same filename — no code changes
are needed.
