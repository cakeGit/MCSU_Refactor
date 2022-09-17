package com.cloud.mcsu_rf.Definitions.GameFunctions;

import com.cloud.mcsu_rf.Definitions.Lambdas.EventLambda;
import org.bukkit.event.Event;

import java.util.Arrays;

public class CustomEventListener extends GameFunctionBase {

    EventLambda onEvent;

    public CustomEventListener(EventLambda onEvent, String eventName) { this.boundEventNames.add(eventName); this.onEvent = onEvent; }
    public CustomEventListener(EventLambda onEvent, String... eventNames) {
        this.boundEventNames.addAll(Arrays.asList(eventNames));
        this.onEvent = onEvent;
    }

    public void onBoundEvent(Event event) { this.onEvent.exec(event); }

}