package me.may2beez.hypixelskid.listeners;

import me.may2beez.hypixelskid.HypixelSkid;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Random;

public class GhostRespawner_listener implements Listener {

    private static int taskId;
    private static final int respawnInterval = 5; // Respawn interval in ticks (1 second = 20 ticks)
    private static final int maxCreepers = 6;
    private static final ArrayList<Pair<Location, ArrayList<Creeper>>> spawnLocations = new ArrayList<>();

    public static void onEnable() {
        spawnLocations.add(Pair.of(new Location(Bukkit.getWorld("world"), 150, 77, 79), new ArrayList<>()));
        spawnLocations.add(Pair.of(new Location(Bukkit.getWorld("world"), 154, 77, 52), new ArrayList<>()));
        spawnLocations.add(Pair.of(new Location(Bukkit.getWorld("world"), 130, 77, 61), new ArrayList<>()));
        spawnLocations.add(Pair.of(new Location(Bukkit.getWorld("world"), 168, 77, 59), new ArrayList<>()));
        spawnLocations.add(Pair.of(new Location(Bukkit.getWorld("world"), 150, 77, 66), new ArrayList<>()));
        spawnLocations.add(Pair.of(new Location(Bukkit.getWorld("world"), 154, 77, 79), new ArrayList<>()));

        // Start the task to respawn creepers
        startRespawnTask();
    }

    public static void onDisable() {
        Bukkit.getScheduler().cancelTask(taskId); // Cancel the respawn task when the plugin is disabled
    }

    @EventHandler
    public void onCreeperSpawn(EntitySpawnEvent event) {
        if (event.getEntityType() == EntityType.CREEPER && UpdateScoreboard_listener.getLocation(event.getLocation()).equals("The Mist")) {
            Creeper creeper = (Creeper) event.getEntity();
            creeper.setHealth(1); // Set the creeper's health to 1
            creeper.setPowered(true);
        } else if (!(event.getEntity() instanceof EntityPlayer)) {
            event.setCancelled(true); // Cancel the spawn event
        }
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        if (event.getEntityType() == EntityType.CREEPER) {
            event.setCancelled(true); // Cancel the explosion event
        }
    }

    @EventHandler
    public void onExplosionPrime(ExplosionPrimeEvent event) {
        if (event.getEntityType() == EntityType.CREEPER) {
            event.setCancelled(true); // Cancel the explosion event
        }
    }

    private static void startRespawnTask() {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(HypixelSkid.plugin, () -> {
            World world = Bukkit.getWorld("world"); // Change "world" to your desired world name
            Random random = new Random();


            for (Pair<Location, ArrayList<Creeper>> loc : spawnLocations) {
                ArrayList<Creeper> nonDeadCreepers = loc.getValue().stream().filter(creeper -> !creeper.isDead()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
                int count = nonDeadCreepers.size();
                if (count < maxCreepers) {
                    for (int i = 0; i < maxCreepers - count; i++) {
                        Creeper creeper = (Creeper) world.spawnEntity(loc.getKey().add(new Vector(random.nextInt(6) - 3, 0, random.nextInt(6) - 3)), EntityType.CREEPER);
                        creeper.setPowered(true);
                        creeper.setHealth(1);
                        creeper.setMaxHealth(1);
                        nonDeadCreepers.add(creeper);
                        // send info on console that "Spawning creeper at: " + creeper.getLocation().toString()
//                        System.out.println("Spawning creeper at: " + creeper.getLocation().toString() + " in location: " + spawnLocations.indexOf(loc));
                    }
                }
                spawnLocations.set(spawnLocations.indexOf(loc), Pair.of(loc.getKey(), nonDeadCreepers));
            }
        }, 0, respawnInterval * 20L); // Convert the delay from seconds to ticks
    }

}
