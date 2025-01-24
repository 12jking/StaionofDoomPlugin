package de.j.stationofdoom.enchants;

import de.j.stationofdoom.main.Main;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FlightEvents implements Listener {

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if(!CustomEnchantsEnum.FLIGHT.isEnabled()) return;
        if (!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();
        if (!player.getInventory().getItemInMainHand().hasItemMeta()) return;
        if (!CustomEnchants.checkEnchant(player.getInventory().getItemInMainHand(), CustomEnchantsEnum.FLIGHT)) return;
        if (!(event.getEntity() instanceof LivingEntity)) return;

        LivingEntity entity = (LivingEntity) event.getEntity();
        if (!entity.isDead()) {
            entity.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 15, 8));
        }

    }
}
