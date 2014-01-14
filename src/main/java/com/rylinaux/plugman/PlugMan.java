package com.rylinaux.plugman;

import com.rylinaux.plugman.messaging.MessageManager;

import org.bukkit.plugin.java.JavaPlugin;

public class PlugMan extends JavaPlugin {

    private static PlugMan instance = null;

    private MessageManager messageManager = null;

    @Override
    public void onEnable() {

        instance = this;

        messageManager = new MessageManager(this);

    }

    @Override
    public void onDisable() {

    }

    public static PlugMan getInstance() {
        return instance;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

}