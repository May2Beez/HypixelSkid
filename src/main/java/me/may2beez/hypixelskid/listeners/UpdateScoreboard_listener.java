package me.may2beez.hypixelskid.listeners;

import me.may2beez.hypixelskid.HypixelSkid;
import me.may2beez.hypixelskid.helpers.AxisAlignedBB;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateScoreboard_listener implements Listener {

    private static int taskId2;
    private static DecimalFormat df;

    public static void onEnable() {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator(',');
        dfs.setGroupingSeparator('.');
        df = new DecimalFormat();
        df.setDecimalFormatSymbols(dfs);
        startLocationTask();
    }

    public static void onDisable() {
        Bukkit.getScheduler().cancelTask(taskId2);
    }

    private static void startLocationTask() {
        taskId2 = Bukkit.getScheduler().scheduleSyncRepeatingTask(HypixelSkid.plugin, new Runnable() {
            @Override
            public void run() {
                World world = Bukkit.getWorld("world"); // Change "world" to your desired world name
                ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();

                for (Player player : Bukkit.getOnlinePlayers()) {
                    Scoreboard scoreboard = scoreboardManager.getNewScoreboard();

                    // Create the objective
                    Objective objective = scoreboard.registerNewObjective("yourObjective", "dummy");
                    objective.setDisplayName("SKIDPIXEL");
                    objective.setDisplaySlot(DisplaySlot.SIDEBAR);

                    objective.getScore("").setScore(7);

                    // Add the current date as a score
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    String currentDate = dateFormat.format(new Date());
                    objective.getScore("§f" + currentDate).setScore(6);

                    // Add the location as a score
                    String location = getLocation(player.getLocation());
                    objective.getScore("§aArea: " + location).setScore(5);

                    // Add purse balance as a score
                    double purseBalance = 69420.0;
                    String purseString = formatNumber(purseBalance);
                    objective.getScore("§ePurse: " + purseString).setScore(4);

                    // Add bits count as a score
                    int bitsCount = 1337;
                    String bitsString = formatNumber(bitsCount);
                    objective.getScore("§dBits: " + bitsString).setScore(3);

                    objective.getScore("").setScore(2);

                    // Add the Ghoster Buster text as a score
                    String lastLine = "§bGhoster Buster";
                    int maxLength = scoreboard.getObjectives().stream().mapToInt(objective1 -> objective1.getDisplayName().length()).max().orElse(0);
                    int padding = (maxLength - lastLine.length()) / 2;
                    String centeredLastLine = repeatString(" ", padding) + lastLine + repeatString(" ", padding);
                    objective.getScore(centeredLastLine).setScore(0);

                    // Set the scoreboard for the player
                    player.setScoreboard(scoreboard);
                }
            }
        }, 0, 5L);
    }

    static String getLocation(Location currentLocation) {

        AxisAlignedBB aabb = new AxisAlignedBB(33, 120, -42, -22, 180, -108);

        if (aabb.isVecInside(currentLocation.toVector())) {
            return "The Forge";
        }

        if (currentLocation.getBlockY() < 90) {
            return "The Mist";
        }

        return "Dwarven Mines";
    }

    private static String repeatString(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    private static String formatNumber(double number) {
        try {
            return df.format(number);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
