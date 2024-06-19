package me.oliverhesse.fightinggameplugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class CommandSpawnAvatar implements CommandExecutor {
    private final Plugin plugin;
    public CommandSpawnAvatar(Plugin plugin){this.plugin = plugin;}

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        //for now the avatar will move in the z plane
        //so spawn it in the x plane
        if(commandSender instanceof Player player){
            Location location = player.getLocation();
            location.setX(location.getX()-10f);
            ArmorStand avatar = createArmorStand(location);
            player.getPersistentDataContainer().set(new NamespacedKey(plugin,"avatar"),new UUIDDataType(),avatar.getUniqueId());
        }

        return false;
    }
    public ArmorStand createArmorStand(Location location) {
        // Get the world from the location
        World world = location.getWorld();

        // Ensure the world is not null
        if (world == null) {
            Bukkit.getLogger().severe("World is null for the given location");
            return null;
        }

        // Spawn the armor stand at the specified location
        ArmorStand armorStand = (ArmorStand) world.spawnEntity(location, EntityType.ARMOR_STAND);

        // Customize the armor stand (optional)
        armorStand.setVisible(true); // Set visibility
        armorStand.setGravity(true); // Set gravity

        armorStand.setCustomNameVisible(true); // Make custom name visible
        armorStand.setArms(true); // Add arms
        armorStand.setBasePlate(false); // Remove base plate
        armorStand.setSmall(false); // Set size

        return  armorStand;
    }
}
