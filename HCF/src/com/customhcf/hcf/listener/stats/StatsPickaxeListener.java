package com.customhcf.hcf.listener.stats;

import com.customhcf.hcf.HCF;
import com.customhcf.hcf.Utils.StatsUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * Created by Nathan'PC on 1/23/2017.
 */
public class StatsPickaxeListener implements Listener {

    @EventHandler
    public void onCraft(PrepareItemCraftEvent event) {
        if (event.getInventory().getResult().getType() == Material.DIAMOND_PICKAXE)
            event.getInventory().setResult(StatsUtils.getNewPickaxe());

    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (!event.isCancelled()) {}
          //  event.getBlock().setMetadata("Placed", new FixedMetadataValue(HCF.getPlugin(), "Stats"));
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event)  {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        ItemStack hand = player.getItemInHand();

        if (hand != null && hand.getType() != Material.AIR && hand.getType() == Material.DIAMOND_PICKAXE) {
            int diamonds = StatsUtils.getCount(hand, 1);
            int emerald = StatsUtils.getCount(hand, 2);
            int gold = StatsUtils.getCount(hand, 3);
            int redstone = StatsUtils.getCount(hand, 4);
            int lapis = StatsUtils.getCount(hand, 5);
            int iron = StatsUtils.getCount(hand, 6);
            int coal = StatsUtils.getCount(hand, 7);
            int stone = StatsUtils.getCount(hand, 8);

            switch (block.getType()) {
                case DIAMOND_ORE:
                    if(!block.hasMetadata("Placed")) {
                        diamonds++;
                        StatsUtils.updateItem(hand, diamonds, emerald, gold, redstone, lapis, iron, coal, stone);
                    }
                    break;
                case EMERALD_ORE:
                    if(!block.hasMetadata("Placed")) {
                        emerald++;
                        StatsUtils.updateItem(hand, diamonds, emerald, gold, redstone, lapis, iron, coal, stone);
                    }
                    break;
                case GOLD_ORE:
                    if(!block.hasMetadata("Placed")) {
                        gold++;
                        StatsUtils.updateItem(hand, diamonds, emerald, gold, redstone, lapis, iron, coal, stone);
                    }
                    break;
                case REDSTONE_ORE:
                    if(!block.hasMetadata("Placed")) {
                        redstone++;
                        StatsUtils.updateItem(hand, diamonds, emerald, gold, redstone, lapis, iron, coal, stone);
                    }
                    break;
                case LAPIS_ORE:
                    if(!block.hasMetadata("Placed")) {
                        lapis++;
                        StatsUtils.updateItem(hand, diamonds, emerald, gold, redstone, lapis, iron, coal, stone);
                    }
                    break;
                case IRON_ORE:
                    if(!block.hasMetadata("Placed")) {
                        iron++;
                        StatsUtils.updateItem(hand, diamonds, emerald, gold, redstone, lapis, iron, coal, stone);
                    }
                    break;
                case COAL_ORE:
                    if(!block.hasMetadata("Placed")) {
                        coal++;
                        StatsUtils.updateItem(hand, diamonds, emerald, gold, redstone, lapis, iron, coal, stone);
                    }
                    break;
                case STONE:
                    if(!block.hasMetadata("Placed")) {
                        stone++;
                        StatsUtils.updateItem(hand, diamonds, emerald, gold, redstone, lapis, iron, coal, stone);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
