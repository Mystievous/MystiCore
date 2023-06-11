package io.github.mystievous.mysticore;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;

public class BlockUtil {

    public static void fillBlocks(BlockData blockData, Location corner1, Location corner2) {
        checkerBlocks(blockData, blockData, corner1, corner2);
    }

    public static void fillBlocks(Material material, Location corner1, Location corner2) {
        BlockData blockData = Bukkit.createBlockData(material);
        fillBlocks(blockData, corner1, corner2);
    }

    public static void checkerBlocks(BlockData blockData1, BlockData blockData2, Location corner1, Location corner2) {
        for (int x = corner1.getBlockX(); x <= corner2.getBlockX(); x++) {
            for (int y = corner1.getBlockY(); y <= corner2.getBlockY(); y++) {
                for (int z = corner1.getBlockZ(); z <= corner2.getBlockZ(); z++) {
                    Location location = new Location(corner1.getWorld(), x, y, z);
                    location.getBlock().setBlockData((x + y + z) % 2 == 0 ? blockData1 : blockData2);
                }
            }
        }
    }

    public static void checkerBlocks(Material material1, Material material2, Location corner1, Location corner2) {
        checkerBlocks(Bukkit.createBlockData(material1), Bukkit.createBlockData(material2), corner1, corner2);
    }

}
