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

---

## Commands

| Command | Description |
|---------|-------------|
| `/milestone` | Open milestone GUI |
| `/milestone open` | Open milestone GUI |
| `/milestone claim <id>` | Claim specific milestone |
| `/milestone check` | Check current progress |
| `/milestone reload` | Reload configuration (admin) |

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
| %milestone_mp_mobs_killed% | Mobs killed |
| `%milestone_mp_players_killed%` | PvP kills |
| `%milestone_mp_community_playtime%` | Total server playtime |
| `%milestone_mp_can_claim%` | Can claim (Yes/No) |

---

## Configuration

Example `config.yml`:

```yaml
database:
  type: sqlite

milestones:
  beginner:
    type: PLAYTIME
    amount: 3600
    choices:
      - id: reward_fly
        name: "Elytra 1 Jam"
        command: "give {player} elytra 1"
      - id: reward_money
        name: "1000 Money"
        command: "eco give {player} 1000"
```

---

## Support

- Issues: [GitHub Issues](https://github.com/Syaaddd/MilestoneMP/issues)
- Discord: Join our community

---

**License:** MIT  
**Version:** 1.0.0
