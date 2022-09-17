package com.cloud.mcsu_rf.Definitions.CustomEvents;

import com.cloud.mcsu_rf.Definitions.Game.Game;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameCountdownEndEvent extends Event implements Cancellable {

    private final Game game;

    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCancelled;

    public GameCountdownEndEvent(Game game){
        this.game = game;
        this.isCancelled = false;
    }

    @Override
    public boolean isCancelled() { return isCancelled; }

    @Override
    public void setCancelled(boolean cancelled) { this.isCancelled = cancelled; }

    @Override
    public HandlerList getHandlers() { return HANDLERS_LIST; }

    public static HandlerList getHandlerList() { return HANDLERS_LIST; }

    public Game getGame() { return game; }

}
