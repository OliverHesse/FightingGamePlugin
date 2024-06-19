package me.oliverhesse.fightinggameplugin;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class CommandLock implements CommandExecutor {
    private final Plugin plugin;
    public CommandLock(Plugin plugin){this.plugin = plugin;}

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player player){

            player.getPersistentDataContainer().set(new NamespacedKey(plugin,"InGame"), PersistentDataType.BOOLEAN,true);
            player.setWalkSpeed(0.1f);

            return true;
        }
        return false;
    }
}
