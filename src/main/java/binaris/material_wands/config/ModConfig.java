package binaris.material_wands.config;

import binaris.material_wands.MaterialWands;

import java.io.IOException;

public class ModConfig {

    public static Config CONFIG;
    public static void registerConfig(){
        CONFIG = new Config(MaterialWands.MOD_ID, MaterialWands.MOD_ID, false);
        setCONFIG();

        try {
            CONFIG.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void setCONFIG(){
        CONFIG.set("message.load.enable", true);

        CONFIG.set("EmptyLine", "");

        CONFIG.set("message.load", "this is a enable message");
    }
}
