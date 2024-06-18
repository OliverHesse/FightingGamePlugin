package me.oliverhesse.fightinggameplugin;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.plugin.Plugin;

public class CustomPlayerController implements Listener {

    private final float LockedMoveSpeed = 0.01f;

    private final Plugin plugin;
    public CustomPlayerController(Plugin plugin){
        this.plugin = plugin;
    }


    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent event){
        plugin.getServer().getLogger().info("Player tried to move");
        plugin.getServer().getLogger().info(event.getTo().toString());
        plugin.getServer().getLogger().info(event.getFrom().toString());
        event.setTo(event.getFrom());
        //event.setCancelled(true);
    }


    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent event){
        event.getPlayer().setFlySpeed(0.01f);
        //event.getPlayer().setGameMode(GameMode.SPECTATOR);
        //this.plugin.getServer().getScheduler().runTaskTimer(plugin, /* Lambda: */ task -> {
        //    Entity entity = createArmorStand(event.getPlayer().getLocation());
        //    event.getPlayer().setSpectatorTarget(entity);

       //     task.cancel(); // The entity is no longer valid, there's no point in continuing to run this task
        //} /* End of the lambda */, 20, 20);

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
        armorStand.setGravity(false); // Set gravity

        armorStand.setCustomNameVisible(true); // Make custom name visible
        armorStand.setArms(true); // Add arms
        armorStand.setBasePlate(false); // Remove base plate
        armorStand.setSmall(false); // Set size
        return  armorStand;
    }

}
