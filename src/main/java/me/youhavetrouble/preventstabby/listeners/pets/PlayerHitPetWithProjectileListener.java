package me.youhavetrouble.preventstabby.listeners.pets;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.config.ConfigCache;
import me.youhavetrouble.preventstabby.players.SmartCache;
import me.youhavetrouble.preventstabby.util.CombatTimer;
import me.youhavetrouble.preventstabby.util.PluginMessages;
import me.youhavetrouble.preventstabby.util.PreventStabbyListener;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.UUID;

@PreventStabbyListener
public class PlayerHitPetWithProjectileListener implements Listener {

     /**
     * Cancels damage done by projectiles to pets of players with pvp off
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerHitPetWithProjectile(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Tameable && event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();
            if (!(projectile.getShooter() instanceof Player)) return;
            Tameable tameable = (Tameable) event.getEntity();
            if (tameable.getOwner() == null) return;

            UUID damager = ((Player) projectile.getShooter()).getUniqueId();
            UUID victim = tameable.getOwner().getUniqueId();

            if (damager.equals(victim)) return;

            ConfigCache config = PreventStabby.getPlugin().getConfigCache();
            SmartCache smartCache = PreventStabby.getPlugin().getSmartCache();

            if (!smartCache.getPlayerData(damager).isPvpEnabled()) {
                PluginMessages.sendActionBar(damager, config.getCannot_attack_pets_attacker());
                event.setCancelled(true);
                return;
            }
            if (!smartCache.getPlayerData(victim).isPvpEnabled()) {
                PluginMessages.sendActionBar(damager, config.getCannot_attack_pets_victim());
                event.setCancelled(true);
                return;
            }
            CombatTimer.refreshPlayersCombatTime(damager);

        }
    }
}
