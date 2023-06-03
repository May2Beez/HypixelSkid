package me.may2beez.hypixelskid.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class warp_command implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only players can use this command.");
            return true;
        }

        if (strings.length != 1) {
            commandSender.sendMessage("Usage: /warp <warpName>");
            return true;
        }

        String warpName = strings[0];

        switch (warpName) {
            case "forge": {
                ((Player) commandSender).teleport(new Location(((Player) commandSender).getWorld(), 0, 149, -68));
                return true;
            }
            case "mist": {
                ((Player) commandSender).teleport(new Location(((Player) commandSender).getWorld(), 149, 76, 65));
                return true;
            }
        }

        return true;
    }
}
