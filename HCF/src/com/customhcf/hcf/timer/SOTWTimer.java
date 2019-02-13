package com.customhcf.hcf.timer;

import java.util.concurrent.TimeUnit;

import com.customhcf.hcf.HCF;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class SOTWTimer
        extends GlobalTimer
{
    public SOTWTimer()
    {
        super("SOTW", TimeUnit.MINUTES.toMillis(30L));

    }



    public String getScoreboardPrefix()
    {
        return ChatColor.GREEN.toString() + ChatColor.BOLD;
    }
}
