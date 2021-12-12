package com.cloud.mcsu_rf.Objects;

import com.cloud.mcsu_rf.Game_Handlers.Game_Main;

public class ActivityRule {

    String name;
    Boolean active;

    public ActivityRule(String name, Boolean active) { this.name = name; this.active = active; Game_Main.activityRules.add(this); }

    public void setActive(Boolean active) { this.active = active; }

    public Boolean getActive() {  return active; }
    public String getName() { return name; }
}
