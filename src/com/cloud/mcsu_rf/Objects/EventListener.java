package com.cloud.mcsu_rf.Objects;

import com.cloud.mcsu_rf.Objects.Lambdas.OnEventLambda;

public class EventListener {

    String eventName;
    OnEventLambda onEvent;

    public EventListener(String eventName, OnEventLambda onEvent) {
        this.eventName = eventName;
        this.onEvent = onEvent;
    }

    public String getEventName() { return eventName; }
    public OnEventLambda getOnEvent() { return onEvent; }

}
