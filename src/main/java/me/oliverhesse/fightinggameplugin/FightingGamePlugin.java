package me.oliverhesse.fightinggameplugin;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class FightingGamePlugin extends JavaPlugin {
    private List<FightingArena> ActiveArena = new ArrayList<FightingArena>();
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
        for(FightingArena arena: this.ActiveArena){
            arena.destroyArena();
        }
    }
    public void create_arena(Player player1, Player player2, Location location){
        FightingArena NewArena = new FightingArena(this,player1,player2,location);

        // Z plane is where the avatar move on
        // X plane is the input plane used to detect jumps

        ActiveArena.add(NewArena);
    }

}
