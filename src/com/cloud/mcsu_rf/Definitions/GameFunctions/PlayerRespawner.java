package com.cloud.mcsu_rf.Definitions.GameFunctions;

import com.cloud.mcsu_rf.Definitions.Lambdas.PlayerLambda;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerRespawner extends GameFunctionBase {

    private int respawnTime;
    private PlayerLambda respawnCondition;
    private PlayerLambda onRespawn;

    public PlayerRespawner() {init( 5, null, null );}
    public PlayerRespawner( int respawnTime ) {init( respawnTime, null, null );}
    public PlayerRespawner( int respawnTime, PlayerLambda respawnCondition ) {init( 5, respawnCondition, null );}
    public PlayerRespawner( int respawnTime, PlayerLambda respawnCondition, PlayerLambda onRespawn ) {init( respawnTime, respawnCondition, onRespawn );}

    private void init( int respawnTime, PlayerLambda respawnCondition, PlayerLambda onRespawn ) {
        boundEventNames.add("PlayerDeathEvent");
        this.respawnTime = respawnTime;
        this.respawnCondition = respawnCondition;
        this.onRespawn = onRespawn;
    }

    public void onBoundEvent(Event event) {
        if( event.getEventName().equals("PlayerDeathEvent") ) {
            PlayerDeathEvent deathEvent = (PlayerDeathEvent) event;
            boolean respawnConditionReturn = false;

            try{respawnCondition.exec( deathEvent.getEntity() );}
            finally {respawnConditionReturn = true;}

            if( respawnCondition == null || respawnConditionReturn ) {
                if( onRespawn != null ) onRespawn.exec( deathEvent.getEntity() );
                ((PlayerDeathEvent) event).getEntity().spigot().respawn();
            }
        }
    }

}
