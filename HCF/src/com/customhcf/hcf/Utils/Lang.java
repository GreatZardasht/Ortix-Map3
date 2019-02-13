package com.customhcf.hcf.Utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public enum Lang
{
    TEAMSPEAK("TEAMSPEAK", "&cOur server Teamspeak is ts.example.net"),
    ECONOMY_PAY_SENDER("ECONOMY.PAY.SENDER", "&cYou have paid %player% %amount%!"),
    ECONOMY_PAY_TARGET("ECONOMY.PAY.TARGET", "&c%player% have paid you %amount%!"),
    ECONOMY_SET("ECONOMY.SET", "&cYou have set the balance of %player% to %amount%"),
    BLACKLISTED("BLACKLIST.BLACKLISTED", "&c%player% has been blacklisted for %reason%"),
    BLACKLIST_ALREADY("BLACKLIST.ATTEMPT_TO_JOIN", "&cThat player has already been blacklisted!"),
    BLACKLIST_AUTOBAN("BLACKLIST.AUTOBAN", "&c%player% has been blacklisted automatically from the ServerNetwork!"),
    BOTTLED_XP("BOTTLE_XP.COMPLETE", "&aYou have successfully converted your XP into BottleXP!"),
    BOTTLED_XP_FAIL("BOTTLE_XP.FAIL", "&cYou werent able to convert your XP due to not having any XP!"),
    COBBLE_PICKUP_ON("COBBLE.PICKUP.ENABLED", "&eYou are now unable to pickup cobblestone!"),
    COBBLE_PICKUP_OFF("COBBLE.PICKUP.DISABLED", "&eYou are now able to pickup cobblestone!"),
    GRANTPVPTIMER_GRANTER("GRANTPROTECTION.SENDER", "&aYou have granted %player% his/her pvptimer back!"),
    GRANTPVPTIMER_GETTER("GRANTPROTECTION.RECEIVER", "&aYou were granted 30m on your PVP Timer!"),
    GRANTPVPTIMER_NOTSPAWN("GRANTPROTECTION.RECEIVER.NOT_IN_SPAWN", "&cThat player isnt in spawn so wasnt able to receive the pvptimer!"),
    MUTECHAT_ALREADYMUTED("MUTECHAT.MUTE.ALREADY", "&cChat is already muted!"),
    MUTECHAT_MUTED("MUTECHAT.MUTE.SUCCESS", "&eYou have muted chat!"),
    MUTECHAT_MUTEDBROADCAST("MUTECHAT.MUTE.BROADCAST", "&cChat has been muted by %player%!"),
    UNMUTECHAT_NOTMUTED("UNMUTECHAT.NOTMUTED", "&cThe chat is currently not muted!"),
    UNMUTECHAT_UNMUTE("UNMUTECHAT.UNMUTE", "&cYou have unmuted the chat!"),
    UNMUTECHAT_UNMUTEBROADCAST("UNMUTECHAT.UNMUTE.BROADCAST", "&c%player% has unmuted chat!"),
    INSTAFFCHAT("STAFFCHAT.ENTERED", "&bStaffChat&7: &fEntered"),
    OUTSTAFFCHAT("STAFFCHAT.LEAVE", "&bStaffChat&7: &fLeft"),
    STAFFREVIVE("STAFFREVIVE.BROADCAST", "&c%staffrevive% &7has revived &c%player%"),
    VANISHOFF("VANISH.TOGGLEDOFF", "&cYou have toggled Vanish Off!"),
    VANISHON("VANISH.TOGGLEDON", "&aYou have toggled Vanish On!"),
    BUYCRAFT_LINK("BUYCRAFT.LINK", "store.example.net");

    private String path;
    private String value;
    private static YamlConfiguration language;

    private Lang(String paramString2, String paramString3)
    {
        this.path = paramString2;
        this.value = paramString3;
    }

    public static void setLangFile(YamlConfiguration paramYamlConfiguration)
    {
        language = paramYamlConfiguration;
    }

    public String toString()
    {
        return ChatColor.translateAlternateColorCodes('&', language.getString(this.path, this.value));
    }

    public String getPath()
    {
        return this.path;
    }

    public String getValue()
    {
        return this.value;
    }


}
