# LogsUpdate.md

---

## v1.0.1

### Features
- Added slot configuration in config.yml (`gui.milestone-slots`)
- Added command tab completer (`/milestone` auto-complete)
- Added `/milestone help` command

### Fixes
- Fixed color codes not showing in chat messages
- Fixed items in GUI being movable (now locked)
- Fixed lambda variable errors (final variables)
- Fixed SQLite Collection type error
- Fixed `selectedChoiceId` not effectively final

---

## v1.0.0

### Initial Release
- Progression Tree GUI system
- Choice-based rewards system
- Multiple milestone types:
  - PLAYTIME
  - BLOCK_BREAK
  - BLOCK_PLACE
  - MOB_KILL
  - PLAYER_KILL
  - JOIN
  - COMMUNITY_PLAYTIME
- SQLite & MySQL support
- PlaceholderAPI integration
- Commands:
  - `/milestone` - Open GUI
  - `/milestone open` - Open GUI
  - `/milestone claim <id>` - Claim milestone
  - `/milestone check` - Check progress
  - `/milestone reload` - Reload config
- Permissions system
- Configurable messages and GUI
