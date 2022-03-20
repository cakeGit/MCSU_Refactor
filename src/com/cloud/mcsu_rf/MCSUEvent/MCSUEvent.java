package com.cloud.mcsu_rf.MCSUEvent;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockVector;

import java.util.ArrayList;

public class MCSUEvent {

    public MCSUEvent() {
        addCustscene();
    }

    public void addCustscene() {
        MCSUCutscene custscene = new MCSUCutscene();
        // PORTALS BETWEEN GAMES YES
        World world = null;
        for(Player players : Bukkit.getOnlinePlayers()) {
            world = players.getWorld();
        }
        custscene.addSection("A long time ago..",PotionEffectType.CONFUSION,4,new Location(world,4399, 69, 5146, -145, -6));
        custscene.addSection("There was a man..",PotionEffectType.BLINDNESS,4,new Location(world,4394, 74, 5132, 72, 28));
        custscene.addSection("called Fluubs.",PotionEffectType.SLOW,4,new Location(world,4394, 82, 5111, 178, 35));
        custscene.startCutscene();
    }

    public void startEvent() {

    }

    public void wonEvent() {

    }
}
