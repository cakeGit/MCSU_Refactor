package com.cak.what.CommandAPI;

public class HelpArgument {

    String desc;
    ArgType type;
    String name;
    String[] args;

    public HelpArgument(String desc, ArgType type, String name) {
        this.desc = desc;
        this.type = type;
        this.name = name;
        this.args = new String[]{};
    }

    public HelpArgument(String desc, ArgType type, String name, String... args) {
        this.desc = desc;
        this.type = type;
        this.name = name;
        this.args = args;
    }

}
