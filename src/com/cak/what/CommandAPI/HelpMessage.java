package com.cak.what.CommandAPI;

import com.cak.what.Util.ChCol;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;

public class HelpMessage {

    String cmdName;
    ArrayList<HelpArgument> args = new ArrayList<>();

    public HelpMessage() { }

    public void send(CommandSender sender) {
        sender.sendMessage(ChCol.GOLD + "Help for command '" +cmdName+ "' \n");

        StringBuilder syntax = new StringBuilder("/" + cmdName);

        for (HelpArgument argument : args) {
            syntax.append(" #"+argument.name+"#".replace("#", "<"));
        }

        sender.sendMessage(ChCol.GOLD + "Help for command '" +cmdName+ "'");
    }

    public String getCmdName() {
        return cmdName;
    }

    public void setCmdName(String cmdName) {
        this.cmdName = cmdName;
    }

    public void set(String cmdName, HelpArgument... args) {
        this.cmdName = cmdName;
        Collections.addAll(this.args, args);
    }

    public boolean isEmpty() {
        return args.isEmpty();
    }
}
