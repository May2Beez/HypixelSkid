package me.may2beez.hypixelskid.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class rotate_command implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        // Check the number of arguments
        if (args.length != 3) {
            sender.sendMessage("Usage: /rotate <playerName> <yawChange> <pitchChange>");
            return true;
        }

        // Parse the yaw and pitch changes
        float yawChange;
        float pitchChange;
        try {
            yawChange = Float.parseFloat(args[1]);
            pitchChange = Float.parseFloat(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage("Invalid yaw or pitch change.");
            return true;
        }

        // Get the player
        Player player = Bukkit.getPlayer(args[0]);

        // Calculate the new yaw and pitch values
        float newYaw = player.getLocation().getYaw() + yawChange;
        float newPitch = player.getLocation().getPitch() + pitchChange;

        // Create and send the rotation packet
        player.teleport(new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), newYaw, newPitch));

        sender.sendMessage("Rotation sent successfully.");

        return true;
    }
}
