package me.may2beez.hypixelskid.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class setSpeed_command implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        // Check if the sender is a player or has permission
        if (!(sender instanceof Player)) {
            sender.sendMessage("You don't have permission to use this command.");
            return true;
        }

        // Check the number of arguments
        if (args.length != 1) {
            sender.sendMessage("Usage: /setspeed <speedMultiplier>");
            return true;
        }

        // Parse the speed multiplier
        double speedMultiplier;
        try {
            speedMultiplier = Double.parseDouble(args[0]) / 100f;
        } catch (NumberFormatException e) {
            sender.sendMessage("Invalid speed multiplier.");
            return true;
        }

        // Get the target player
        Player target = sender.getServer().getPlayer(sender.getName());
        if (target == null) {
            sender.sendMessage("Player not found.");
            return true;
        }

        // Set the custom speed for the target player
        setCustomSpeed(target, speedMultiplier);

        sender.sendMessage("Speed set successfully.");

        return true;
    }

    private void setCustomSpeed(Player player, double speedMultiplier) {
        float defaultSpeed = 0.2f;
        float customSpeed = (float) (defaultSpeed * speedMultiplier);

        player.setWalkSpeed(customSpeed);
    }
}
