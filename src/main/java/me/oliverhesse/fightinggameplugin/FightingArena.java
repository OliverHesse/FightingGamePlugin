package me.oliverhesse.fightinggameplugin;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class FightingArena {

    private final Plugin plugin;
    private final Player player1;
    private final Player player2;
    private final Location ArenaLocation;
    private List<Block> ArenaBlock = new ArrayList<Block>();
    public FightingArena(Plugin plugin, Player player1, Player player2, Location location){
        this.plugin = plugin;
        this.player1 = player1;
        this.player2 = player2;
        this.ArenaLocation = location;
    }

    private void createArena(){}

    public void destroyArena(){}


}
