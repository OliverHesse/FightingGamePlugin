package me.oliverhesse.fightinggameplugin;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class CommandCreateArena implements CommandExecutor {
    private final Plugin plugin;
    public CommandCreateArena(Plugin plugin){this.plugin = plugin;}

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(commandSender instanceof Player player){

            if(args.length != 2){
                return false;
            }
            Player player1 = plugin.getServer().getPlayer(args[0]);
            Player player2 = plugin.getServer().getPlayer(args[1]);
            if(player1 == null || player2 == null){
                return false;
            }
            //create a new arena
            ((FightingGamePlugin) plugin).create_arena(player1,player2,player.getLocation());

            return true;
        }
        return false;
    }
}
