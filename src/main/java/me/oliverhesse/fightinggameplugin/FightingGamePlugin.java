package me.oliverhesse.fightinggameplugin;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class FightingGamePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("lock").setExecutor(new CommandLock(this));
        getCommand("unlock").setExecutor(new CommandUnlock(this));
        getCommand("spawn_avatar").setExecutor(new CommandSpawnAvatar(this));
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new CustomPlayerController(this),this);

        //an attempt at setting a main loop
        //should allow me to buffer inputs


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
