package me.oliverhesse.fightinggameplugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class FightingArena {
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
    private List<Pair<BlockData,Location>> ArenaBlock = new ArrayList<>();
    public FightingArena(Plugin plugin, Player player1, Player player2, Location location){
        this.plugin = plugin;
        this.player1 = player1;
        this.player2 = player2;
        this.ArenaLocation = location;
        createArena();
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


        player1Loc.add(0.5,1,0.5);
        player2Loc.add(0.5,1,0.5);


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


}
