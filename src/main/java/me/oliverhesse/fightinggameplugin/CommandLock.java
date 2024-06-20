package me.oliverhesse.fightinggameplugin;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

public class CommandLock implements CommandExecutor {
    private final Plugin plugin;
    public CommandLock(Plugin plugin){this.plugin = plugin;}

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(commandSender instanceof Player player){

            player.getPersistentDataContainer().set(new NamespacedKey(plugin,"InGame"), PersistentDataType.BOOLEAN,true);
            player.setWalkSpeed(0.1f);
            Location loc = player.getLocation();
            loc.setYaw(90);
            loc.setPitch(0);
            player.teleport(loc);

            return true;
        }
        return false;
    }

}
