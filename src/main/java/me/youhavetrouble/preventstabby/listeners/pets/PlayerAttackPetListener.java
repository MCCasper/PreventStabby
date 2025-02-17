package me.youhavetrouble.preventstabby.listeners.pets;

import me.youhavetrouble.preventstabby.PreventStabby;
import me.youhavetrouble.preventstabby.config.ConfigCache;
import me.youhavetrouble.preventstabby.players.SmartCache;
import me.youhavetrouble.preventstabby.util.CombatTimer;
import me.youhavetrouble.preventstabby.util.PluginMessages;
import me.youhavetrouble.preventstabby.util.PreventStabbyListener;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.UUID;

@PreventStabbyListener
public class PlayerAttackPetListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerAttackPet(org.bukkit.event.entity.EntityDamageByEntityEvent event) {

        if (event.getDamager() instanceof Player && event.getEntity() instanceof Tameable) {
            SmartCache smartCache = PreventStabby.getPlugin().getSmartCache();
            Tameable tameable = (Tameable) event.getEntity();
            if (tameable.getOwner() == null) return;

            UUID damager = event.getDamager().getUniqueId();
            UUID victim = tameable.getOwner().getUniqueId();

            if (damager.equals(victim)) return;

            ConfigCache config = PreventStabby.getPlugin().getConfigCache();
            boolean damagerPvpState = PreventStabby.getPlugin().getPlayerManager().getPlayerPvPState(damager);
            if (!damagerPvpState) {
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
