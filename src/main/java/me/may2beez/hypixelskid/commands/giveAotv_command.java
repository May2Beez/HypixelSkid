package me.may2beez.hypixelskid.commands;

import me.may2beez.hypixelskid.items.AspectOfTheVoid_item;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class giveAotv_command implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        ItemStack aotv = AspectOfTheVoid_item.aotv;

        ((Player) sender).getInventory().addItem(aotv);

        sender.sendMessage("Have fun with AOTV!");

        return true;
    }
}
