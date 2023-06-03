package me.may2beez.hypixelskid.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class killAll_command implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            return true;
        }

        ((Player) commandSender).getWorld().getEntities().forEach(entity -> {
            if (entity instanceof Player) {
                return;
            }

            entity.remove();
        });

        return true;
    }
}
