package me.oliverhesse.fightinggameplugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
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
        getCommand("create_arena").setExecutor(new CommandCreateArena(this));
        getCommand("clear_arenas").setExecutor(new CommandClearArenas(this));
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



        ActiveArena.add(NewArena);
    }
    public void clear_arena(){
        for(FightingArena arena: this.ActiveArena){
            arena.destroyArena();
        }
    }

}
