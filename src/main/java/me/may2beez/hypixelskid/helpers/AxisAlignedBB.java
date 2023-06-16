package me.may2beez.hypixelskid.helpers;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class AxisAlignedBB {
    public final double minX;
    public final double minY;
    public final double minZ;
    public final double maxX;
    public final double maxY;
    public final double maxZ;

    public AxisAlignedBB(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        this.minX = Math.min(x1, x2);
        this.minY = Math.min(y1, y2);
        this.minZ = Math.min(z1, z2);
        this.maxX = Math.max(x1, x2);
        this.maxY = Math.max(y1, y2);
        this.maxZ = Math.max(z1, z2);
    }

    public AxisAlignedBB(Location pos1, Location pos2)
    {
        this.minX = (double)pos1.getX();
        this.minY = (double)pos1.getY();
        this.minZ = (double)pos1.getZ();
        this.maxX = (double)pos2.getX();
        this.maxY = (double)pos2.getY();
        this.maxZ = (double)pos2.getZ();
    }

    public boolean isVecInside(Vector vec)
    {
        return vec.getX() > this.minX && vec.getX() < this.maxX && (vec.getBlockY() > this.minY && vec.getBlockY() < this.maxY && vec.getZ() > this.minZ && vec.getZ() < this.maxZ);
    }
}
