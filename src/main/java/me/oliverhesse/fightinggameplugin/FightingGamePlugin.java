package me.oliverhesse.fightinggameplugin;

import org.bukkit.plugin.java.JavaPlugin;

public final class FightingGamePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new CustomPlayerController(this),this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
