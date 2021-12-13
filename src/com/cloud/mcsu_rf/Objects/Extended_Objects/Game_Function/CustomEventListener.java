package com.cloud.mcsu_rf.Objects.Extended_Objects.Game_Function;

import com.cloud.mcsu_rf.Objects.Lambdas.OnEventLambda;
import org.bukkit.event.Event;

public class CustomEventListener extends GameFunction {

    OnEventLambda onEvent;

    public CustomEventListener(String eventName, OnEventLambda onEvent) { this.boundEventNames.add(eventName); this.onEvent = onEvent; }

    public void onBoundEvent(Event event) { this.onEvent.exec(event); }

}