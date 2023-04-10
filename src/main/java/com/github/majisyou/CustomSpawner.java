package com.github.majisyou;

import com.github.majisyou.Command.Cmd_admin;
import com.github.majisyou.Command.Cmd_debug;
import com.github.majisyou.Command.Cmd_user;
import com.github.majisyou.Event.Ev_playerEvent;
import com.github.majisyou.Event.SpawnerEvent;
import com.github.majisyou.FileManager.ConfigManager;
import com.github.majisyou.Main.Sys_Enchantment;
import com.github.majisyou.Main.Sys_GuiItem;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class CustomSpawner extends JavaPlugin {
    private static CustomSpawner instance;

    public CustomSpawner(){
        instance = this;
    }
    public static CustomSpawner getInstance(){
        return instance;
    }
    private static final Logger log = Logger.getLogger("Minecraft");
    private static Economy econ = null;

    @Override
    public void onEnable() {
        // Plugin startup logic
        //Vault
        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }


        //Event
        new SpawnerEvent(this);
        new Ev_playerEvent(this);

        //config
        saveDefaultConfig();
        ConfigManager.loadConfig(this);
        Sys_GuiItem.loadItems();

        //Enchant
        Sys_Enchantment.registerEnchant();

        //Command
        new Cmd_debug(this);
        new Cmd_user(this);
        new Cmd_admin(this);

        getLogger().info("CustomSpawner is Enable");
    }

    @Override
    public void onDisable() {
        log.info(String.format("[%s] Disabled Version %s", getName(), Bukkit.getVersion()));
        // Plugin shutdown logic
        getLogger().info("CustomSpawner is Disable");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
//        return econ != null;
        return true;
    }


    public static Economy getEconomy() {
        return econ;
    }

}
