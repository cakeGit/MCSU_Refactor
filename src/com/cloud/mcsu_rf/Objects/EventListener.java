package com.cloud.mcsu_rf.Objects;

import com.cloud.mcsu_rf.Objects.Lambdas.onEventLambda;

public class EventListener {

    String eventName;
    onEventLambda onEvent;

    public EventListener(String eventName, onEventLambda onEvent) {
        this.eventName = eventName;
        this.onEvent = onEvent;
    }

    public String getEventName() {
        return eventName;
    }

    public onEventLambda getOnEvent() {
        return onEvent;
    }

}
