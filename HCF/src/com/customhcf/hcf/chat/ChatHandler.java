package com.customhcf.hcf.chat;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ChatHandler {

    public static ArrayList<Player> sc = new ArrayList<>();

    private boolean mutechat = false;


    public boolean isMutechat() {
        return mutechat;
    }

    public void setMutechat(boolean mutechat) {
        this.mutechat = mutechat;
    }
}