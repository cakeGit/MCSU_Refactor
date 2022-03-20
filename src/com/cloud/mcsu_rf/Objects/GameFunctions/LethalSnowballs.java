package com.cloud.mcsu_rf.Objects.GameFunctions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class LethalSnowballs extends GameFunctionBase {

    int damage = 20;

    public LethalSnowballs() {
        boundEventNames.add("EntityDamageByEntityEvent");
    }

    public void onBoundEvent(Event event) {
        EntityDamageByEntityEvent entityAttack = (EntityDamageByEntityEvent) event;

        Bukkit.broadcastMessage("thwoe" + (entityAttack.getDamager().getType() == EntityType.SNOWBALL));
    }
}
