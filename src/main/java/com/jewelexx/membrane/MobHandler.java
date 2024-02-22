package com.jewelexx.membrane;

import java.util.List;
import java.util.logging.Level;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class MobHandler implements Listener {
    final Random rng;
    final String disablePhantoms;

    MobHandler(final String disablePhantoms) {
        this.disablePhantoms = disablePhantoms;
        rng = new Random();
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getEntityType() == EntityType.PHANTOM) {
            switch (this.disablePhantoms) {
                case "always":
                    event.setCancelled(true);
                    break;
                case "natural":
                    if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL) {
                        event.setCancelled(true);
                    }
                    break;
                case "never":
                    break;
                default:
                    Bukkit.getLogger().log(Level.SEVERE, "Invalid config options for \"disable-phantoms\"");
                    break;
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();

        if (entity.getType() == EntityType.BAT) {
            List<ItemStack> drops = event.getDrops();

            double rand = Math.random();
            double rngOffset = 0;

            Player killer = entity.getKiller();

            if (killer != null) {
                ItemStack killerItem = killer.getInventory().getItemInMainHand();
                Map<Enchantment, Integer> enchantments = killerItem.getEnchantments();

                try {
                    Integer lootingLevel = enchantments.get(Enchantment.LOOT_BONUS_MOBS);

                    rngOffset += lootingLevel * .05;
                } catch (NullPointerException ignore) {}
            }

            // 20% chance + offset
            if (rand < (0.2 + rngOffset)) {
                drops.add(new ItemStack(Material.PHANTOM_MEMBRANE, 1));
            }
        }
    }
}
