package org.openhab.binding.mqttcontrol.internal;

/**
 * Created by jonas on 18.10.16.
 */
public class MQTTCommand {
    private String commandtype;
    private String command;
    private String itemname;

    public MQTTCommand(String commandtype, String command, String itemname) {
        this.commandtype = commandtype;
        this.command = command;
        this.itemname = itemname;
    }

    public MQTTCommand() {

    }

    public String getCommandtype() {
        return commandtype;
    }

    public String getCommand() {
        return command;
    }

    public String getItemname() {
        return itemname;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s %d", commandtype, command, itemname);
    }
}
