package com.cloud.mcsu_rf.Definitions.GameFunctions;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.Objects;

public class LethalSnowballs extends GameFunctionBase {

    int damage;

    public LethalSnowballs(int damage) {
        this.damage = damage;
        boundEventNames.add("ProjectileHitEvent");
    }

    public void onBoundEvent(Event event) {
        ProjectileHitEvent hitEvent = (ProjectileHitEvent) event;
        Entity hitEntity = hitEvent.getHitEntity();

        if (hitEntity != null) {
            Player hitPlayer = null;
            try {
                hitPlayer = (Player) hitEntity;
            } finally {
                hitPlayer.damage(damage, ((Player) Objects.requireNonNull(hitEvent.getEntity().getShooter())));
                hitPlayer.setLastDamageCause(new EntityDamageByEntityEvent((Entity) Objects.requireNonNull(hitEvent.getEntity().getShooter()),
                                hitPlayer, DamageCause.PROJECTILE, damage));
            }
        }
    }

}
