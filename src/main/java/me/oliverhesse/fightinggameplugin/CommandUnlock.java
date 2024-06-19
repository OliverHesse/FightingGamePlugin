package me.oliverhesse.fightinggameplugin;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class CommandUnlock implements CommandExecutor {
    private final Plugin plugin;
    public CommandUnlock(Plugin plugin){this.plugin = plugin;}

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player player){

            player.getPersistentDataContainer().set(new NamespacedKey(plugin,"InGame"), PersistentDataType.BOOLEAN,false);
            player.setWalkSpeed(0.2f);

            return true;
        }
        return false;

    }
}
