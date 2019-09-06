package me.lorenzo.rpg.util;

import me.lorenzo.rpg.Main;

import java.lang.reflect.Field;

public class Variables {

    private static String prefix = "§c[RPG]§r";

    public static void reload(){
        for(Field field : Variables.class.getDeclaredFields()){
            if(!field.getName().endsWith("Message")){
                try {
                    Main.getConfigs().add("configuration."+field.getName(),field.get(0));
                    field.set(0,Main.getConfigs().message("configuration."+field.getName()).replace("$prefix", prefix));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Main.getMessages().add("messages."+field.getName(),field.get(0));
                    field.set(0,Main.getMessages().message("messages."+field.getName()).replace("$prefix", prefix));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
