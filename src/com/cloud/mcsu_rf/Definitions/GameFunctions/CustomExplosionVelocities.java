package com.cloud.mcsu_rf.Definitions.GameFunctions;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Objects;

public class CustomExplosionVelocities extends GameFunctionBase {

    double power = 0.75;
    double range = 5;
    double exponent = 3.2;

    double yBase = 0.1;

    double yBoost = 1.1;
    double fireballYBoost = 1.3;

    double blockAbsorption = 0.5;
    double maxAbsorption = 0.2;

    private double calc(double x) {
        return (
                ( ( -1 / Math.pow( range, exponent ) ) * Math.pow( Math.abs(x), exponent ) ) + 1
        ) * power * Math.signum(x);
    }

    public CustomExplosionVelocities() {
        boundEventNames.add("EntityExplodeEvent");
    }

    public void onBoundEvent(Event event) {

        EntityExplodeEvent explodeEvent = (EntityExplodeEvent) event;
        Location explosionLoc = explodeEvent.getLocation();
        List<Entity> affected =  explodeEvent.getEntity().getNearbyEntities(range,range,range);//Get full size of range

        double trueYBoost = (explodeEvent.getEntity() instanceof Fireball ? fireballYBoost : yBoost );

        for (Entity entity : affected) {

            Vector velocity = entity.getVelocity();

            Location entityLoc = entity.getLocation();

            //See if non-air blocks between

            Double strength = 1.0;

            int x1 = (int) Math.min(Math.floor(entityLoc.getX()), Math.floor(explosionLoc.getX()));
            int x2 = (int) Math.max(Math.floor(entityLoc.getX()), Math.floor(explosionLoc.getX()));

            int y1 = (int) Math.min(Math.floor(entityLoc.getY()), Math.floor(explosionLoc.getY()));
            int y2 = (int) Math.max(Math.floor(entityLoc.getY()), Math.floor(explosionLoc.getY()));

            int z1 = (int) Math.min(Math.floor(entityLoc.getZ()), Math.floor(explosionLoc.getZ()));
            int z2 = (int) Math.max(Math.floor(entityLoc.getZ()), Math.floor(explosionLoc.getZ()));

            for (int x = x1; x <= x2; x++) {
                for (int y = y1; y <= y2; y++) {
                    for (int z = z1; z <= z2; z++) {
                        if (
                                Objects.requireNonNull(
                                    entityLoc.getWorld()
                                ).getBlockAt(x, y, z).getType() != Material.AIR
                        ) {
                            strength = Math.max(maxAbsorption, strength-blockAbsorption);

                        }
                    }
                }
            }


            Vector explosionForce = new Vector(
                    calc(entityLoc.getX() - explosionLoc.getX()) * strength,
                    calc(entityLoc.getY() - explosionLoc.getY() + yBase) * trueYBoost * strength,
                    calc(entityLoc.getZ() - explosionLoc.getZ()) * strength
            );

            velocity.add(explosionForce);

            entity.setVelocity(velocity);

        }


    }

}
