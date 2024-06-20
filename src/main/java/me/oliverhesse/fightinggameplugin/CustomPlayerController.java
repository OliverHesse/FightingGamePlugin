package me.oliverhesse.fightinggameplugin;

import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.player.*;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.checkerframework.checker.units.qual.N;

import javax.naming.Name;
import java.util.UUID;

import static java.lang.Math.abs;
import static java.lang.Math.toRadians;

public class CustomPlayerController implements Listener {

    private final float AVATAR_SPEED = 0.2f;
    private final float JUMP_POWER = 1f;
    private final Plugin plugin;
    public CustomPlayerController(Plugin plugin){
        this.plugin = plugin;
    }
    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent event){
        //can be used to detect left or right click. which i will use for light and special
        //block will be backwards movement



    }


    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent event){
        Player player = event.getPlayer();
        if(!player.getPersistentDataContainer().has(new NamespacedKey(plugin,"InGame"))){
            return;
        }

        boolean gameState = player.getPersistentDataContainer().get(new NamespacedKey(plugin,"InGame"),PersistentDataType.BOOLEAN);
        player.getServer().getLogger().info(((Boolean) gameState).toString());
        if(!gameState){
            return;

        }
        //limit movement and handle movement stuff

        if(!player.getPersistentDataContainer().has(new NamespacedKey(plugin,"avatar"))){
            event.setCancelled(true);
            return;
        }

        UUID avatarID = player.getPersistentDataContainer().get(new NamespacedKey(plugin,"avatar"),new UUIDDataType());
        Entity avatar = player.getWorld().getEntity(avatarID);
        if(avatar instanceof ArmorStand){


            //local X is forward and backwards
            //local Z is left and right
            //store current y velocity
            double YVel = avatar.getVelocity().getY();
            //plugin.getServer().getLogger().info("curr velocity of: " +((Double) YVel).toString());
            Vector direction = event.getTo().subtract(event.getFrom()).toVector();
            //now i want to move it all to in terms of 1,-1 or 0
            //it is probably more efficient to do if > 0 but for now this works
            //currently Z is the players forward and backwards movement
            if(direction.getX() != 0){direction.setY((direction.getX()/abs(direction.getX()))*-1);direction.setX(0);}
            if(direction.getZ() != 0){direction.setZ(direction.getZ()/abs(direction.getZ()));}
            //plugin.getServer().getLogger().info(direction.toString());

            Location avatarLocation = avatar.getLocation();
            avatarLocation.setPitch(avatar.getPitch());
            avatarLocation.setYaw(avatar.getYaw());
            avatarLocation.setZ(avatarLocation.getZ()+direction.getZ()*AVATAR_SPEED);
            avatar.teleport(avatarLocation);
            avatar.setVelocity(new Vector(0,YVel,0));
            if(avatar.isOnGround()){
                //.getServer().getLogger().info("On ground:");
                direction.setZ(0);

                if(direction.getY()>0){
                    plugin.getServer().getLogger().info("Trying to jump");
                    direction.setY(direction.getY()*JUMP_POWER);
                    avatar.setVelocity(direction);
                } else if (direction.getY()<0) {
                    //currently do nothing
                }

            }else{
                //plugin.getServer().getLogger().info("Not on ground with curr velocity of: " +((Double) YVel).toString());

            }



        }else{
            //raise error here
        }


        event.setCancelled(true);

    }


    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent event){
        event.getPlayer().getInventory().setHeldItemSlot(0);
        event.getPlayer().getPersistentDataContainer().set(new NamespacedKey(this.plugin, "directionX"), PersistentDataType.INTEGER, 0);
        event.getPlayer().getPersistentDataContainer().set(new NamespacedKey(this.plugin, "directionY"), PersistentDataType.INTEGER, 0);
        //event.getPlayer().setFlySpeed(0.01f);
        //event.getPlayer().setGameMode(GameMode.SURVIVAL);
        //this.plugin.getServer().getScheduler().runTaskTimer(plugin, /* Lambda: */ task -> {
        //    Entity entity = createArmorStand(event.getPlayer().getLocation());
        //    event.getPlayer().setSpectatorTarget(entity);

       //     task.cancel(); // The entity is no longer valid, there's no point in continuing to run this task
        //} /* End of the lambda */, 20, 20);

    }



}
