package me.oliverhesse.fightinggameplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class CommandClearArenas implements CommandExecutor {
    private final Plugin plugin;
    public CommandClearArenas(Plugin plugin){this.plugin = plugin;}

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        ((FightingGamePlugin) plugin).clear_arena();
        return false;
    }
}
