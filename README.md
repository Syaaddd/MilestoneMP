# MilestoneMP

**Unlock Your Path, Not Just Rewards.**

A unique Minecraft server plugin that brings a progression tree system where players can choose their own rewards when reaching milestones.

---

## Features

- **Progression Tree GUI** - Visual milestone system with locked, available, and claimed states
- **Choice-Based Rewards** - Players choose from multiple reward options
- **Multiple Milestone Types**
  - `PLAYTIME` - Play time tracking
  - `BLOCK_BREAK` - Blocks broken
  - `BLOCK_PLACE` - Blocks placed
  - `MOB_KILL` - Mobs killed
  - `PLAYER_KILL` - PvP kills
  - `JOIN` - Login streak
  - `COMMUNITY_PLAYTIME` - Total server playtime
- **SQLite & MySQL Support** - Flexible database options
- **PlaceholderAPI Integration** - Display progress anywhere
- **Console Command Rewards** - Commands executed via console sender

---

## Commands

| Command | Description |
|---------|-------------|
| `/milestone` | Open milestone GUI |
| `/milestone open` | Open milestone GUI |
| `/milestone check` | Check current progress |
| `/milestone claim <id>` | Claim specific milestone |
| `/milestone help` | Show help menu |
| `/milestone reload` | Reload configuration (admin) |

**Aliases:** `/ms`, `/mp`

---

## Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `milestonemp.open` | Open milestone GUI | true |
| `milestonemp.claim` | Claim milestones | true |
| `milestonemp.check` | Check progress | true |
| `milestonemp.admin` | Admin commands | op |

---

## Placeholders

| Placeholder | Description |
|------------|-------------|
| `%milestone_mp_current%` | Current milestone ID |
| `%milestone_mp_next%` | Next available milestone |
| `%milestone_mp_progress%` | Progress percentage |
| `%milestone_mp_playtime%` | Player playtime (Xh Ym) |
| `%milestone_mp_blocks_broken%` | Blocks broken |
| `%milestone_mp_blocks_placed%` | Blocks placed |
| `%milestone_mp_mobs_killed%` | Mobs killed |
| `%milestone_mp_players_killed%` | PvP kills |
| `%milestone_mp_join_days%` | Login streak days |
| `%milestone_mp_community_playtime%` | Total server playtime |
| `%milestone_mp_can_claim%` | Can claim (Yes/No) |

---

## Configuration

Example `config.yml`:

```yaml
database:
  type: sqlite
  host: localhost
  port: 3306
  database: milestoneMP
  username: root
  password: ""

settings:
  check-interval: 60
  community-reward-broadcast: true

messages:
  prefix: "&8[&6Milestone&8] "
  milestone-available: "&aMilestone available! Click to claim."
  milestone-locked: "&cThis milestone is still locked."
  milestone-claimed: "&eReward claimed: %reward%"
  no-milestone: "&cNo milestone available."
  no-permission: "&cYou don't have permission."
  already-claimed: "&cThis milestone has already been claimed."

gui:
  title: "&8Progression Tree"
  available-color: "&a"
  locked-color: "&7"
  claimed-color: "&e"
  claim-button: "&aClick to Claim"
  choose-button: "&eChoose Reward"
  milestone-slots:
    - 10
    - 11
    - 12
    - 13
    - 14
    - 15
    - 16
    - 19
    - 21
    - 23
    - 25
    - 28
    - 30
    - 32
    - 34

milestones:
  beginner:
    type: PLAYTIME
    amount: 3600
    icon: CLOCK
    color: "&6"
    choices:
      - id: reward_fly
        name: "&bElytra Rental (1 Hour)"
        command: "give {player} elytra 1"
      - id: reward_money
        name: "&61000 Server Money"
        command: "eco give {player} 1000"

  miner:
    type: BLOCK_BREAK
    amount: 100
    icon: DIAMOND_PICKAXE
    color: "&e"
    choices:
      - id: reward_pickaxe
        name: "&eDiamond Pickaxe"
        command: "give {player} diamond_pickaxe 1"

  hunter:
    type: MOB_KILL
    amount: 50
    icon: DIAMOND_SWORD
    color: "&c"
    choices:
      - id: reward_sword
        name: "&cEnchanted Sword"
        command: "give {player} diamond_sword{Enchantments:[{id:sharpness,lvl:5}]} 1"
```

---

## Reward System

Rewards are executed as **console commands**. Use `{player}` as placeholder for player name.

**Examples:**
```yaml
command: "give {player} diamond 10"
command: "eco give {player} 5000"
command: "xp give {player} 50 levels"
command: "tokens give {player} 100"
```

---

## GUI Preview

The GUI features:
- Progress bar visual: `[████████░░] 80%`
- Color-coded items by milestone type
- Empty slots filled with glass panes for clean look
- Clear status indicators (Locked, Available, Claimed)
- Choice selection GUI for multiple reward options

---

## Installation

1. Download the plugin JAR file
2. Place it in your server's `plugins` folder
3. Start the server
4. Configure `config.yml` to your needs
5. Use `/milestone reload` to reload config after changes

---

## Changelog

### Version 1.0.7 (Patch)
- Fixed: Milestone notification not showing when player completes quest through activities (block break, block place, mob kill, playtime, join)
- Fixed: Removed break statement to notify all reached milestones (not just the first one)

### Version 1.0.6 (Patch)
- Fixed: Choices not loaded from config (use getMapList for YAML list format)
- Fixed: Player data loading to be synchronous
- Fixed: Milestone claim logic - auto-select first choice when choiceId is null
- Fixed: Milestone notification spam (now only notify once on join)
- Removed: Debug logs from production code

### Version 1.0.5
- Initial release with progression tree system
- Choice-based rewards system
- Multiple milestone types support
- SQLite & MySQL support
- PlaceholderAPI integration

---

## Support

- Issues: [GitHub Issues](https://github.com/Syaaddd/MilestoneMP/issues)

---

**License:** MIT  
**Version:** 1.0.7
