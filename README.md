# 🎮 Lifesteal Plugin - Minecraft 1.21.1

A competitive PvP game mode plugin where players steal hearts from each other. When a player loses all their hearts, they get banned until someone unbans them using a special Unban Book or an admin command.

## 📋 Features

### ⚔️ Player Kill System
- **Stealing Hearts**: When you kill another player, you gain 1 heart (2 health points)
- **Losing Hearts**: The killed player loses 1 heart
- **Auto-Ban**: When a player reaches 0 hearts, they are automatically banned from the server
- **Notifications**: Real-time broadcasts show who killed whom and their remaining hearts

### 💓 Heart Redemption System (NEW!)
- **Right-Click Hearts**: Right-click a heart item to consume it and gain 1 heart instantly
- **Command Redeem**: Use `/redeem heart [count]` to consume multiple hearts at once
- **Smart Inventory**: Check how many hearts you have with intelligent parsing
- **Max Health Check**: Can't redeem more hearts than your max health allows
- **Sound & Feedback**: Get instant feedback with sounds and action bar messages

**Heart Crafting Recipe:**
```
D D D
D R D
D D D

D = Diamond Block (8 needed)
R = Redstone Block (1 needed)
```

**Example Usage:**
```
/redeem heart 1    → Consume 1 heart, gain +1❤
/redeem heart 5    → Consume 5 hearts, gain +5❤
Right-click heart  → Instant consumption
```

### 🚫 Ban System
- **Automatic Banning**: Players reaching 0 hearts are automatically banned
- **Ban Persistence**: Bans are saved and survive server restarts
- **Join Prevention**: Banned players cannot join the server and see a ban message

### 📖 Unban Book System
**Crafting Recipe:**
```
H  T  h
CBBc
H  T  h

H  = Heart (crafted from diamond blocks)
T  = Totem of Undying
C  = Crying Obsidian (corners)
B  = Book
```

**Usage Steps:**
1. Craft the Unban Book using the recipe above
2. Right-click with the Unban Book
3. The system displays a numbered list of all banned players
4. Type the player's number in chat (e.g., `2`)
5. The selected player is unbanned with 4 hearts
6. The book is consumed

**Example:**
```
[1] GPT
[2] Claude
[3] Steve

Type 2 → Claude is unbanned!
```

### 🧟 Mob Kill Mechanic
- **Random Chance**: 15% chance to gain a heart when killing a hostile mob
- **Supported Mobs**: Zombies, Skeletons, Creepers, Endermen, Witches, and more
- **Lucky Message**: "✦ Lucky! You gained a heart from a mob!"

### 🔧 Admin Commands

#### `/redeem heart <count>` ⭐ NEW!
Consume hearts from your inventory to gain health.

```
/redeem heart 1      → Use 1 heart
/redeem heart 10     → Use 10 hearts
```

**Features:**
- Check if you have enough hearts
- Prevent redeeming more hearts than max health allows
- Multiple hearts in one command
- Instant feedback with sound and action bar

#### `/unban <player>`
Unbans a player immediately. Only admins with `lifesteal.unban` permission can use this.

```
/unban Steve
→ Steve has been UNBANNED!
```

#### `/lifesteal reload`
Reloads the plugin configuration without restarting the server.

```
/lifesteal reload
→ Configuration reloaded!
```

#### `/lifesteal stats`
Displays server statistics including the number of banned players and online players.

```
/lifesteal stats
→ Banned Players: 3
→ [1] GPT
→ [2] Claude
→ [3] Alex
```

#### `/lifesteal help`
Shows all available commands and information.

## ⚙️ Configuration

Edit `plugins/Lifesteal/config.yml` to customize:

```yaml
settings:
  mob-kill-chance: 0.15    # 15% chance for mob kills to grant hearts
  unban-hearts: 4          # Players get 4 hearts when unbanned

messages:
  ban-message: "..."           # Message when banned
  unban-message: "..."         # Message when unbanned
  player-kill-message: "..."   # Message when killing a player
  lucky-mob-message: "..."     # Message for lucky mob kills
```

## 📊 Game Mechanics Summary

| Event | Effect |
|-------|--------|
| Kill Player | You gain +1 ❤ (max 10 ❤) |
| Get Killed | You lose -1 ❤ (min 0 ❤) |
| Right-Click Heart | Consume 1 heart, gain +1 ❤ |
| `/redeem heart 5` | Consume 5 hearts, gain +5 ❤ |
| Reach 0 ❤ | You are BANNED |
| Use Unban Book | Target player unbanned with 4 ❤ |
| Kill Mob (15% chance) | You gain +1 ❤ (lucky!) |
| Rejoin Server | Banned players kicked with message |

## 🔐 Permissions

| Permission | Description | Default |
|-----------|-------------|---------|
| `lifesteal.unban` | Can use `/unban` command | OP |
| `lifesteal.admin` | Can use admin commands | OP |
| `lifesteal.stats` | Can view statistics | OP |
| `lifesteal.redeem` | Can use `/redeem` command | TRUE |

## 📦 Installation

1. Download the latest `Lifesteal-1.1.0.jar` file
2. Place it in your server's `plugins/` folder
3. Restart the server
4. The plugin will auto-generate config files in `plugins/Lifesteal/`
5. Customize `config.yml` if needed
6. Restart again to apply changes

## 📝 Tips & Tricks

- **Early Game**: Survive and farm mobs for hearts
- **Heart Trading**: Farm hearts with `/redeem` command
- **Strategic Farming**: Kill mobs to build reserves (15% lucky chance)
- **PvP Strategy**: Don't attack if you only have 1-2 hearts!
- **Team Play**: Share hearts with teammates or unban them

## ✨ What's New in v1.1.0

✅ Heart Redemption System  
✅ Right-click heart consumption  
✅ `/redeem heart [count]` command  
✅ Intelligent heart inventory checking  
✅ Max health validation  
✅ Sound effects and action bar feedback  
✅ Improved error messages  

## 🐛 Troubleshooting

**Q: Banned players can still join?**
A: Make sure the server has reloaded the plugin. Check `banned_players.yml` exists.

**Q: Heart redemption not working?**
A: Ensure hearts are properly crafted with correct recipe. Check they have the custom name.

**Q: Can't redeem hearts?**
A: You might be at max health. Make sure you're not full HP.

**Q: Config won't reload?**
A: Use `/lifesteal reload` command or restart the server.

## 📞 Support

For issues, questions, or feature requests, please check the server logs and ensure:
- Java 21+ is installed
- Spigot/PaperMC 1.21.1+ is running
- Plugin has write permissions to its data folder

## 📜 License

This plugin is provided as-is for use on Minecraft servers.

---

**Version**: 1.1.0  
**Minecraft**: 1.21.1  
**Latest Update**: Heart Redemption System Added  
**Made with ❤️ for competitive Minecraft**
