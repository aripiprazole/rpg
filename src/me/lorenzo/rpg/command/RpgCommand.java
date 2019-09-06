package me.lorenzo.rpg.command;

import net.eduard.api.lib.manager.CommandManager;

public class RpgCommand extends CommandManager {
    //olha esse comando aqui
    public RpgCommand(){
        super("rpg");
        register(new RpgSetAttributeCommand());
        register(new RpgClassCommand());
        register(new RpgSetSpawnCommand());
        register(new RpgLifeCommand());
        register(new RpgHelpCommand());
        register(new RpgSetNickCommand());
    }

}
