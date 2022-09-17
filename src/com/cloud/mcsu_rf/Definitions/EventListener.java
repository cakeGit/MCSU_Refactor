package com.cloud.mcsu_rf.Definitions;

import com.cloud.mcsu_rf.Definitions.Lambdas.EventLambda;

public class EventListener {

    public final String eventName;
    EventLambda onEvent;

    public EventListener(String eventName, EventLambda onEvent) {
        this.eventName = eventName;
        this.onEvent = onEvent;
    }

    public String getEventName() { return eventName; }
    public EventLambda getOnEvent() { return onEvent; }

}
