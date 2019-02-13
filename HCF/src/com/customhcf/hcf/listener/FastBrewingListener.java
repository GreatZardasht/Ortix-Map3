package com.customhcf.hcf.listener;

import com.customhcf.hcf.HCF;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

/**
 * Created by nyang on 4/2/2017.
 */
public class FastBrewingListener implements Listener {

        public FastBrewingListener(HCF plugin) {}

        private void startUpdate(final Furnace tile, final int increase)
        {
            new BukkitRunnable()
            {
                public void run()
                {
                    if ((tile.getCookTime() > 0) || (tile.getBurnTime() > 0))
                    {
                        tile.setCookTime((short)(tile.getCookTime() + increase));
                        tile.update();
                    }
                    else
                    {
                        cancel();
                    }
                }
            }.runTaskTimer(HCF.getPlugin(), 1L, 1L);
        }


}
