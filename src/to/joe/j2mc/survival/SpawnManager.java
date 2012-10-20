package to.joe.j2mc.survival;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

public class SpawnManager {

    String players[];
    Location spawns[];
    int maxPlayers;
    J2MC_Survival plugin;

    public SpawnManager(World world, List<String> spawnStrings, J2MC_Survival survival) {
        this.plugin = survival;
        spawns = new Location[spawnStrings.size()];
        players = new String[spawnStrings.size()];
        for (int x = 0; x < spawnStrings.size(); x++) {
            String splitString[] = spawnStrings.get(x).split(",");
            spawns[x] = new Location(world, Double.parseDouble(splitString[0]) + .5, Double.parseDouble(splitString[1]) + 1, Double.parseDouble(splitString[2]) + .5);
        }
        Collections.shuffle(Arrays.asList(spawns));
    }

    public boolean addPlayer(String name) {
        for (int x = 0; x < players.length; x++) {
            if (players[x] == null) {
                players[x] = name;
                return true;
            }
        }
        return false;
    }

    public boolean addPlayerLate(Player player) {
        String name = player.getName();
        for (int x = 0; x < players.length; x++) {
            if (players[x] == null) {
                players[x] = name;
                preparePlayer(player);
                player.teleport(spawns[x]);
                return true;
            }
        }
        return false;
    }

    public void removePlayer(String name) {
        for (int x = 0; x < players.length; x++) {
            if (players[x].equals(name)) {
                players[x] = null;
                return;
            }
        }
    }

    public void spawnPlayers() {
        for (int x = 0; x < players.length; x++) {
            if (players[x] == null)
                continue;
            Player p = this.plugin.getServer().getPlayer(players[x]);
            if (p == null)
                continue;
            preparePlayer(p);
            p.teleport(spawns[x]);
        }
    }

    public void preparePlayer(Player p) {
        this.plugin.setSpectate(p, false);
        p.setGameMode(GameMode.SURVIVAL);
        p.setAllowFlight(false);
        p.setFlying(false);
        PlayerInventory pInv = p.getInventory();
        pInv.clear();
        pInv.setHelmet(null);
        pInv.setChestplate(null);
        pInv.setLeggings(null);
        pInv.setBoots(null);
        pInv.addItem(new ItemStack(Material.COMPASS));
        p.setHealth(p.getMaxHealth());
        p.setFoodLevel(20);
        Collection<PotionEffect> effects = p.getActivePotionEffects();
        for (PotionEffect e : effects) {
            p.removePotionEffect(e.getType());
        }
    }
}
