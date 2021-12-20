package com.cloud.mcsu_rf.Objects.GameFunctions;

import com.cloud.mcsu_rf.Objects.Lambdas.EventLambda;
import org.bukkit.event.Event;

public class CustomEventListener extends GameFunctionBase {

    EventLambda onEvent;

    public CustomEventListener(String eventName, EventLambda onEvent) { this.boundEventNames.add(eventName); this.onEvent = onEvent; }

    public void onBoundEvent(Event event) { this.onEvent.exec(event); }

}