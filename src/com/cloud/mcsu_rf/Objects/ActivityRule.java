package com.cloud.mcsu_rf.Objects;

import com.cloud.mcsu_rf.EventListenerMain;

public class ActivityRule {

    String name;
    Boolean active;
    Boolean activeDefault;

    public ActivityRule(String name, Boolean activeDefault) {

        this.name = name;
        this.activeDefault = activeDefault;
        this.active = activeDefault;

        EventListenerMain.activityRules.add(this);

    }

    public void resetActive() { this.active = activeDefault; }

    public void setActive(Boolean active) { this.active = active; }

    public Boolean getActive() {  return active; }
    public String getName() { return name; }

}
