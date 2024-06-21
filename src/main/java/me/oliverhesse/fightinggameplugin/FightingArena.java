package me.oliverhesse.fightinggameplugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class FightingArena implements Listener {
    public class Pair<L,R> {

        private final L left;
        private final R right;

        public Pair(L left, R right) {
            assert left != null;
            assert right != null;

            this.left = left;
            this.right = right;
        }

        public L getLeft() { return left; }
        public R getRight() { return right; }

        @Override
        public int hashCode() { return left.hashCode() ^ right.hashCode(); }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Pair)) return false;
            Pair pairo = (Pair) o;
            return this.left.equals(pairo.getLeft()) &&
                    this.right.equals(pairo.getRight());
        }

    }
    private final Plugin plugin;
    private final Player player1;
    private final Player player2;
    private final Location ArenaLocation;
    private boolean gameState = false;
    private final double AVATAR_SPEED = 0.4f;
    private final double JUMP_POWER = 1;
    private List<Pair<BlockData,Location>> ArenaBlock = new ArrayList<>();
    public FightingArena(Plugin plugin, Player player1, Player player2, Location location){
        this.plugin = plugin;
        this.player1 = player1;
        this.player2 = player2;
        this.ArenaLocation = location;
        createArena();
        //do some sort of countdown like 5 seconds
        gameState = true;
    }
    private void setupGame(){
        // set each player to be in game.
        //teleport each one to either side of the arena
        //create avatar for each player and set it to be their avatar
        //players are 5 blocks away
        Location player1Loc = ArenaLocation.clone();
        Location player2Loc = ArenaLocation.clone();

        player1Loc.add(5,0,0);
        player2Loc.add(-5,0,0);

        ArenaBlock.add(new Pair<>(player1Loc.getBlock().getBlockData(),player1Loc.clone()));
        ArenaBlock.add(new Pair<>(player2Loc.getBlock().getBlockData(),player2Loc.clone()));

        player1Loc.getBlock().setType(Material.GREEN_TERRACOTTA);
        player2Loc.getBlock().setType(Material.RED_TERRACOTTA);


        player1Loc.add(0,1,0);
        player2Loc.add(0,1,0);


        player1.getPersistentDataContainer().set(new NamespacedKey(plugin,"InGame"), PersistentDataType.BOOLEAN,true);
        player1.setWalkSpeed(0.1f);

        player2.getPersistentDataContainer().set(new NamespacedKey(plugin,"InGame"), PersistentDataType.BOOLEAN,true);
        player2.setWalkSpeed(0.1f);

        player1Loc.setYaw(90);
        player1Loc.setPitch(0);

        player2Loc.setYaw(-90);
        player2Loc.setPitch(0);

        player1.teleport(player1Loc);
        player2.teleport(player2Loc);

        //player 1 avatar needs to face north and player 2 south



        //start countdown
        runTimer(5);
    }
    private void createArena(){
        // Z plane is where the avatar move on
        // X plane is the input plane used to detect jumps

        //for now center block will be green teracota
        plugin.getServer().getLogger().info("Trying to create arena");
        Location BlockLocation = ArenaLocation.toBlockLocation();

        ArenaBlock.add(new Pair<>(BlockLocation.getBlock().getBlockData(),BlockLocation.clone()));

        plugin.getServer().getLogger().info(BlockLocation.getBlock().getBlockData().getMaterial().toString());
        BlockLocation.getBlock().setType(Material.GREEN_TERRACOTTA);

        Location negativeBlock = BlockLocation.clone();
        Location positiveBlock = BlockLocation.clone();
        for(int i = 0;i<7;i++){
            negativeBlock.setZ(negativeBlock.getZ()-1);
            positiveBlock.setZ(positiveBlock.getZ()+1);

            ArenaBlock.add(new Pair<>(negativeBlock.getBlock().getBlockData(),negativeBlock.clone()));
            ArenaBlock.add(new Pair<>(positiveBlock.getBlock().getBlockData(),positiveBlock.clone()));


            plugin.getServer().getLogger().info(positiveBlock.getBlock().getBlockData().getMaterial().toString());
            plugin.getServer().getLogger().info(negativeBlock.getBlock().getBlockData().getMaterial().toString());

            negativeBlock.getBlock().setType(Material.RED_TERRACOTTA);
            positiveBlock.getBlock().setType(Material.RED_TERRACOTTA);
        }
        //now build walls on either side
        negativeBlock.setZ(negativeBlock.getZ()-1);
        positiveBlock.setZ(positiveBlock.getZ()+1);

        ArenaBlock.add(new Pair<>(negativeBlock.getBlock().getBlockData(),negativeBlock.clone()));
        ArenaBlock.add(new Pair<>(positiveBlock.getBlock().getBlockData(),positiveBlock.clone()));

        negativeBlock.getBlock().setType(Material.GREEN_TERRACOTTA);
        positiveBlock.getBlock().setType(Material.GREEN_TERRACOTTA);

        for(int i = 0;i <10;i++){
            negativeBlock.setY(negativeBlock.getY()+1);
            positiveBlock.setY(positiveBlock.getY()+1);

            ArenaBlock.add(new Pair<>(negativeBlock.getBlock().getBlockData(),negativeBlock.clone()));
            ArenaBlock.add(new Pair<>(positiveBlock.getBlock().getBlockData(),positiveBlock.clone()));

            negativeBlock.getBlock().setType(Material.GREEN_TERRACOTTA);
            positiveBlock.getBlock().setType(Material.GREEN_TERRACOTTA);
        }
        setupGame();
    }

    public void destroyArena(){
        plugin.getServer().getLogger().info("=======Clearing Arena=======");
        for(Pair<BlockData,Location> block: ArenaBlock){
            plugin.getServer().getLogger().info("Clearing Block");

            block.getRight().getBlock().setBlockData(block.getLeft());
        }
        //in the future clean up armour stands aswell
    }

    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent event){

        //make sure the players are part of this arena
        if(player1.getUniqueId() != event.getPlayer().getUniqueId() && player2.getUniqueId() != event.getPlayer().getUniqueId()){
            return;
        }
        //make sure gamestate is true
        if(!gameState){
            return;
        }
        //determine which player is the one being controlled
        Player player = event.getPlayer();
        Player enemy = player1;
        if(player1.getUniqueId() != event.getPlayer().getUniqueId()){enemy = player2;}

        //since they are players they must have avatars
        Entity avatar = player.getWorld().getEntity(player.getPersistentDataContainer().get(new NamespacedKey(plugin,"avatar"),new UUIDDataType()));
        Entity enemyAvatar= enemy.getWorld().getEntity(enemy.getPersistentDataContainer().get(new NamespacedKey(plugin,"avatar"),new UUIDDataType()));

        //used to maintain velocity after teleport
        double avatarYVelocity = avatar.getVelocity().getY();

        Vector direction = event.getTo().subtract(event.getFrom()).toVector();
        //Z is for Horizontal motion. X is for Vertical Motion
        direction.setY(0);
        if(direction.getX() != 0){direction.setY((direction.getX()/abs(direction.getX()))*-1);direction.setX(0);}
        if(direction.getZ() != 0){direction.setZ(direction.getZ()/abs(direction.getZ()));}


        Location avatarLocation = avatar.getLocation();
        Location enemyLocation = enemyAvatar.getLocation();
        //some code to determine if they need to swap directions

        if(avatar.getYaw() == -180 && (avatarLocation.getZ()<enemyLocation.getZ())){
            //rotate avatar to south and enemy to south
            avatarLocation.setYaw(0);
            enemyLocation.setYaw(-180);
            enemyAvatar.teleport(enemyLocation);

        }else if(avatar.getYaw() == 0 && (avatarLocation.getZ()>enemyAvatar.getLocation().getZ())){
            //rotate avator to north and enemy to south
            avatarLocation.setYaw(-180);
            enemyLocation.setYaw(0);
            enemyAvatar.teleport(enemyLocation);
        }

        avatarLocation.setZ(avatarLocation.getZ()+direction.getZ()*AVATAR_SPEED);
        //used to look for collisions
        Location checkLocation = avatarLocation.clone();
        checkLocation.setZ(checkLocation.getZ()+0.5*direction.getZ());
        if(checkLocation.getBlock().getBlockData().getMaterial() != Material.AIR || abs(checkLocation.getY()-enemyLocation.getY())<2){
            // should be a wall or an enemy
            //reverse the speed
            double change = -1*direction.getZ()*AVATAR_SPEED;
            avatarLocation.setZ(avatarLocation.getZ()+change);


        }

        avatar.teleport(avatarLocation);
        avatar.setVelocity(new Vector(0,avatarYVelocity,0));
        if(avatar.isOnGround()){
            direction.setZ(0);
            if(direction.getY()>0){
                direction.setY(direction.getY()*JUMP_POWER);
                avatar.setVelocity(direction);
            } else if (direction.getY()<0) {
                //will be used for crouching combos and stuff
            }
        }

    }

    public void runTimer(Integer time){
        plugin.getServer().getScheduler().runTaskLater(plugin, /* Lambda: */ () -> {
            if(time == 0) {
                player1.showTitle(Title.title(Component.text("Start"), Component.text(""), Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ZERO)));
                player2.showTitle(Title.title(Component.text("Start"), Component.text(""), Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ZERO)));
                gameState = true;
            }else{
                player1.showTitle(Title.title(Component.text(time.toString()), Component.text(""), Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ZERO)));
                player2.showTitle(Title.title(Component.text(time.toString()), Component.text(""), Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ZERO)));
                runTimer(time-1);
            }

            }, /* End of the lambda */ 20);
    }
}
