package me.may2beez.hypixelskid.items;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.Set;

public class AspectOfTheVoid_item implements Listener {

    public static ItemStack aotv;

    public static void addAspectOfTheVoidItem() {
        // Create the item stack with diamond shovel texture
        ItemStack itemStack = new ItemStack(Material.DIAMOND_SPADE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("Aspect of the Void");
        itemStack.setItemMeta(itemMeta);
        aotv = itemStack;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        // Check if the player right-clicked with the "Aspect of the Void" item
        if (item != null && item.getType() == Material.DIAMOND_SPADE && item.hasItemMeta()
                && item.getItemMeta().getDisplayName().equals("Aspect of the Void")) {
            if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                Location loc1 = player.getLocation();
                player.getWorld().playSound(loc1, Sound.ENDERMAN_TELEPORT, 3, 1);
                Block b = player.getTargetBlock((Set<Material>)null, player.isSneaking() ? 61 : 12);
                Location loc = null;
                if (player.isSneaking() && b == null)
                    return;
                else if (player.isSneaking()) {
                    loc = new Location(b.getWorld(), b.getX() + 0.5, b.getY() + 1, b.getZ() + 0.5, player.getLocation().getYaw(), player.getLocation().getPitch());
                } else {
                    loc = new Location(b.getWorld(), b.getX(), b.getY(), b.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
                }
                player.teleport(loc);
            }
        }
    }
}
