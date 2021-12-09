package com.cloud.mcsu_rf.Objects;

import com.cloud.mcsu_rf.EventListener_Main;
import com.cloud.mcsu_rf.Objects.Extended_Objects.Game_Function.GameFunction;

public class Game_Component {

    public Game_Component(GameFunction gameFunction) {

        for (String eventName : gameFunction.getBoundEventNames()) {

            EventListener_Main.addEventListener(new EventListener(eventName, gameFunction::runEventHandler)); // some weir adutogen code - passes the event to emitBoundEvent

        }

    }

}