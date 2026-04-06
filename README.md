# Lightweight Skin Changer

A lightweight Minecraft plugin that allows players to change their skin using a simple command.
Forked from ```SimpleMineCode/skin-changer``` – originally only provided a /skin command without any persistence.
tested: 1.21.x

### What's new in this fork?

- Added YAML‑based caching to permanently store player skins.
- Skins now persist across rejoins and server reloads – no more losing your skin after a restart!

### Features:
- /skin <username> – change your skin to any valid Minecraft player skin.
- Automatic YAML cache – saves the last used skin for each player.
- Lightweight, no external dependencies, minimal performance overhead.

Perfect for servers that want a simple, reliable skin changer with persistent storage.
