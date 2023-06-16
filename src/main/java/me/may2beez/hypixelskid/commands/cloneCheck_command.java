package me.may2beez.hypixelskid.commands;

import com.mojang.authlib.GameProfile;
import me.may2beez.hypixelskid.HypixelSkid;
import net.minecraft.server.v1_8_R3.*;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;


public class cloneCheck_command implements CommandExecutor, Listener {

    private static EntityPlayer clone;
    private static Player player;
    private static final ArrayList<Pair<Location, Integer>> changedBlocks = new ArrayList<>();

    public cloneCheck_command() {
        Bukkit.getPluginManager().registerEvents(this, HypixelSkid.plugin);
        startTickEvent();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length != 1) {
            commandSender.sendMessage("Usage: /cloneCheck <playerName>");
            return true;
        }

        String playerName = strings[0];

        player = Bukkit.getPlayer(playerName);
        if (player == null) {
            commandSender.sendMessage("Player not found.");
            return true;
        }

        Location playerLocation = player.getLocation();
        Location cloneLocation = playerLocation.clone().add(playerLocation.getDirection().multiply(2));

        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld)player.getWorld()).getHandle();

        clone = new EntityPlayer(server, world, new GameProfile(player.getUniqueId(), player.getName()),
                new PlayerInteractManager(world));

        clone.setLocation(cloneLocation.getX(), cloneLocation.getY(), cloneLocation.getZ(), cloneLocation.getYaw(), cloneLocation.getPitch());
        PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, clone));
        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(clone));

        changedBlocks.addAll(changeBlocksToBedrock(playerLocation, 9));

        new BukkitRunnable() {
            @Override
            public void run() {
                PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
                connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, clone));
                connection.sendPacket(new PacketPlayOutEntityDestroy(clone.getId()));
                changedBlocks.forEach(pair -> {
                    Location location = pair.getLeft();
                    location.getBlock().setTypeIdAndData(pair.getRight(), (byte) 0, true);
                });
                clone = null;
                player = null;
            }
        }.runTaskLater(HypixelSkid.plugin, 200); // Remove the clone after 10 seconds

        return true;
    }

    public ArrayList<Pair<Location, Integer>> changeBlocksToBedrock(Location playerLocation, int radius) {
        ArrayList<Pair<Location, Integer>> changedBlocks = new ArrayList<>();

        // Get the world and player's location
        World world = playerLocation.getWorld();
        int playerX = playerLocation.getBlockX();
        int playerY = playerLocation.getBlockY();
        int playerZ = playerLocation.getBlockZ();

        // Iterate through the blocks within the specified radius
        for (int x = playerX - radius; x <= playerX + radius; x++) {
            for (int y = playerY - radius; y <= playerY + radius; y++) {
                for (int z = playerZ - radius; z <= playerZ + radius; z++) {
                    Location blockLocation = new Location(world, x, y, z);
                    org.bukkit.block.Block block = blockLocation.getBlock();

                    // Check if the block is visible from at least one side and is not air
                    if (isVisible(block) && block.getTypeId() != 0) {
                        // Change the block to bedrock
                        changedBlocks.add(Pair.of(blockLocation, block.getTypeId()));
                        block.setTypeIdAndData(7, (byte) 0, true);

                        // Store the original block in the ArrayList
                    }
                }
            }
        }

        return changedBlocks;
    }

    public boolean isVisible(org.bukkit.block.Block block) {
        // Check if the block has at least one side visible
        for (BlockFace face : BlockFace.values()) {
            if (block.getRelative(face).getType().isTransparent()) {
                return true;
            }
        }
        return false;
    }

    public void startTickEvent() {
        // Create a new BukkitRunnable
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                if (player == null || clone == null) return;

                Location playerLocation = player.getLocation();
                Location cloneLocation = playerLocation.clone().add(playerLocation.getDirection().multiply(1.5));

                teleportClone(cloneLocation);
                updateCloneLook(playerLocation);
            }
        };

        // Run the task every tick (20 ticks per second)
        // Adjust the delay and period values as needed
        task.runTaskTimer(HypixelSkid.plugin, 0L, 1L);
    }

    public void teleportClone(Location cloneLocation) {
        clone.setLocation(cloneLocation.getX(), cloneLocation.getY(), cloneLocation.getZ(), cloneLocation.getYaw(), cloneLocation.getPitch());

        PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutEntityTeleport(clone));
    }

    public void updateCloneLook(Location playerLocation) {
        double dx = playerLocation.getX() - clone.locX;
        double dy = playerLocation.getY() - clone.locY;
        double dz = playerLocation.getZ() - clone.locZ;

        double distanceXZ = Math.sqrt(dx * dx + dz * dz);
        double distanceY = Math.sqrt(distanceXZ * distanceXZ + dy * dy);

        double yaw = Math.toDegrees(Math.atan2(dz, dx)) - 90;
        double pitch = Math.toDegrees(-Math.atan2(dy, distanceXZ));

        clone.yaw = (float) yaw;
        clone.pitch = (float) pitch;

        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutEntityHeadRotation(clone, (byte) ((int) (yaw * 256 / 360))));
        connection.sendPacket(new PacketPlayOutEntityTeleport(clone));
    }
}
