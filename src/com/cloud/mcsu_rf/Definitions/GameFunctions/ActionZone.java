package com.cloud.mcsu_rf.Definitions.GameFunctions;

import com.cloud.mcsu_rf.Definitions.Lambdas.PlayerLambda;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.BlockVector;

import java.util.ArrayList;

public class ActionZone extends GameFunctionBase {

    public static boolean pointInside(double a, double b, double point) {
        return Math.abs(a-point) + Math.abs(b-point) == Math.abs(a-b);
    }

    protected BlockVector from;
    protected BlockVector to;

    protected ArrayList<Player> playersInside = new ArrayList<>();

    protected PlayerLambda onEnterEvent;
    protected PlayerLambda onExitEvent;
    protected PlayerLambda whilePlayerInside;

    protected boolean complexEventProcessing = false;

    public ActionZone(BlockVector from, BlockVector to) {

        this.from = from;
        this.to = to;

        boundEventNames.add("PlayerMoveEvent");

    }

    public boolean testLocationInsideZone(Location loc) {
        return pointInside( from.getX(), to.getX(), loc.getX() )
                &&
                pointInside( from.getY(), to.getY(), loc.getY() )
                &&
                pointInside( from.getZ(), to.getZ(), loc.getZ() );
    }

    public void onBoundEvent(Event event) {

        if (event.getEventName().equals("PlayerMoveEvent")) {

            PlayerMoveEvent playerMoveEvent = (PlayerMoveEvent) event;
            Player player = playerMoveEvent.getPlayer();
            Location pLoc = player.getLocation();

            if (
                    testLocationInsideZone(pLoc)
            ) {
                whilePlayerInside.exec(player);

                if ( complexEventProcessing ) {
                    if ( !playersInside.contains(player) ) {
                        playersInside.add(player);
                        onEnterEvent.exec(player);
                    }
                }
            } else {
                if ( complexEventProcessing ) {
                    if (playersInside.contains(player)) {
                        playersInside.remove(player);
                        onEnterEvent.exec(player);
                    }
                }
            }

        }

    }

    public ActionZone setOnEnterEvent(PlayerLambda onEnterEvent) { this.onEnterEvent = onEnterEvent; complexEventProcessing = true; return this; }
    public ActionZone setOnExitEvent(PlayerLambda onExitEvent) { this.onExitEvent = onExitEvent; complexEventProcessing = true; return this; }
    public ActionZone setWhilePlayerInside(PlayerLambda whilePlayerInside) { this.whilePlayerInside = whilePlayerInside; return this; }

}
