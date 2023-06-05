package me.may2beez.hypixelskid;

import me.may2beez.hypixelskid.commands.*;
import me.may2beez.hypixelskid.items.AspectOfTheVoid_item;
import me.may2beez.hypixelskid.listeners.GhostRespawner_listener;
import me.may2beez.hypixelskid.listeners.UpdateScoreboard_listener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class HypixelSkid extends JavaPlugin implements Listener {

    public static HypixelSkid plugin;


    @Override
    public void onEnable() {
        plugin = this;
        // items
        AspectOfTheVoid_item.addAspectOfTheVoidItem();

        // commands
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new AspectOfTheVoid_item(), this);
        getServer().getPluginManager().registerEvents(new GhostRespawner_listener(), this);
        getServer().getPluginManager().registerEvents(new UpdateScoreboard_listener(), this);
        getServer().getPluginManager().registerEvents(new cloneCheck_command(), this);
        getCommand("setspeed").setExecutor(new setSpeed_command());
        getCommand("rotate").setExecutor(new rotate_command());
        getCommand("giveAotv").setExecutor(new giveAotv_command());
        getCommand("warp").setExecutor(new warp_command());
        getCommand("killAll").setExecutor(new killAll_command());
        getCommand("cloneCheck").setExecutor(new cloneCheck_command());

        // listeners
        GhostRespawner_listener.onEnable();
        UpdateScoreboard_listener.onEnable();
    }

    @Override
    public void onDisable() {
        GhostRespawner_listener.onDisable();
        UpdateScoreboard_listener.onDisable();
    }

}
