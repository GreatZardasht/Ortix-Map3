package com.bizarrealex.azazel.tab.example;

import com.bizarrealex.azazel.tab.TabAdapter;
import com.bizarrealex.azazel.tab.TabTemplate;
import com.customhcf.hcf.HCF;
import com.customhcf.hcf.classes.PvpClass;
import com.customhcf.hcf.classes.archer.ArcherClass;
import com.customhcf.hcf.classes.bard.BardClass;
import com.customhcf.hcf.classes.type.MinerClass;
import com.customhcf.hcf.faction.type.PlayerFaction;

import net.md_5.bungee.api.ChatColor;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

public class ExampleTabAdapter implements TabAdapter {
    public TabTemplate getTemplate(Player player) {
        TabTemplate template = new TabTemplate();

    	// Middle Tab
		template.middle(0, "&8&m---------------");
		template.middle(1, "&2&lZytro");
		template.middle(2, "&8&m---------------");

		// Left Tab"&8&m---------------");
		template.left(0, "&8&m---------------");
		template.left(1, "&2store.zytro.rip");
		template.left(2, "&8&m---------------");
		template.left(4, "&2&lPlayer Info:");
		template.left(5, "&fKills: " + player.getStatistic(Statistic.PLAYER_KILLS));
		template.left(6, "&fDeaths: " + player.getStatistic(Statistic.DEATHS));
		template.left(7, "§fBalance: " + HCF.getPlugin().getEconomyManager().getBalance(player.getUniqueId()));
		
        PvpClass pvpClass = HCF.getPlugin().getPvpClassManager().getEquippedClass(player);
		
        if (pvpClass instanceof MinerClass) {
        	template.left(8, "§eClass: §cMiner");
        }
        if (pvpClass instanceof ArcherClass) {
        	template.left(8, "§eClass: §cArcher");
        }
        if (pvpClass instanceof BardClass) {
        	template.left(8, "§eClass: §cArcher");
        }
    	template.left(9, "");
		
		template.left(10, "&2&lYour Location");
//		template.left(10, HCF.getInstance().getFactionManager().getFactionAt(player.getLocation())
			//	.getDisplayName(player));
		template.left(11,
				ChatColor.WHITE + "(" + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockZ()
						+ ")" + ChatColor.GRAY + " [" + getCardinalDirection(player) + "]");

		// FactionListener Stuff Currently Fixing

		// Middle Tab
		
	    String staffchat = (ChatColor.DARK_GREEN + "Chat§7: " +  (com.customhcf.base.BasePlugin.getPlugin().getServerHandler().isChatDisabled()
	    		? ChatColor.RED + "Locked" : ChatColor.GREEN + "Enabled"));
		
		if(com.customhcf.base.BasePlugin.getPlugin().getUserManager().getUser(player.getUniqueId()).isStaffUtil()) {
		template.middle(9, "§2§lStaff Mode");
		
		if(player.getGameMode() == GameMode.CREATIVE) {
			template.middle(10, "§2GM §7» §aCreative");
		}
		if(player.getGameMode() == GameMode.SURVIVAL) {
			template.middle(10, "§2GM §7» §cSurvival");
		}
		if(player.getGameMode() == GameMode.ADVENTURE) {
			template.middle(10, "§2GM §7» §cAdventure");
		}
		template.middle(11, "" + staffchat);
		}
	    PlayerFaction playerFaction = HCF.getPlugin().getFactionManager().getPlayerFaction(player);
	    String playertag = (playerFaction == null) ? (ChatColor.WHITE + "None") :   playerFaction.getDisplayName(Bukkit.getConsoleSender());
		
		template.middle(6, "§2§lFaction Info:");
		template.middle(7, "§2Name§7 » " + playertag);
		
		// Right Tab
		template.right(0, "&8&m---------------");
		template.right(1, "&2ts.zytro.rip");
		template.right(2, "&8&m---------------");
		template.right(4, "&2&lEnd Portal");
		template.right(5, "&fSpawn");
		template.right(6, "&f");
		template.right(7, "");
		template.right(8, "&2&lMap Kit:");
		template.right(9, "&fProt 2, Sharp 2");
		template.right(10, "");
		template.right(11, "&2&lWorld Border:");
		template.right(12, "&f2000");
		template.right(13, "");
		template.right(14, "&2&lPlayers Online:");
		template.right(15, "&f" + Bukkit.getServer().getOnlinePlayers().length);

		// Bottom
		template.left(19, "&8&m---------------");
		template.middle(19, "&8&m---------------");
		template.right(19, "&8&m---------------");

		// 1.8 users only
		template.farRight(8, "§4§lWARNING!");
		template.farRight(9, "§4we recommend");
		template.farRight(10, "§4you use version 1.7");
		template.farRight(11, "§4For Better");
		template.farRight(12, "§4Gaming Expierence.");

		return template;
    }
	public static String getCardinalDirection(Player player) {
		double rotation = (player.getLocation().getYaw() + 180F) % 360.0F;
		if (rotation < 0.0D) {
			rotation += 360.0D;
		}
		if ((0.0D <= rotation) && (rotation < 22.5D)) {
			return "N";
		}
		if ((22.5D <= rotation) && (rotation < 67.5D)) {
			return "NE";
		}
		if ((67.5D <= rotation) && (rotation < 112.5D)) {
			return "E";
		}
		if ((112.5D <= rotation) && (rotation < 157.5D)) {
			return "SE";
		}
		if ((157.5D <= rotation) && (rotation < 202.5D)) {
			return "S";
		}
		if ((202.5D <= rotation) && (rotation < 247.5D)) {
			return "SW";
		}
		if ((247.5D <= rotation) && (rotation < 292.5D)) {
			return "W";
		}
		if ((292.5D <= rotation) && (rotation < 337.5D)) {
			return "NW";
		}
		if ((337.5D <= rotation) && (rotation < 360.0D)) {
			return "N";
		}
		return null;
	}
}

