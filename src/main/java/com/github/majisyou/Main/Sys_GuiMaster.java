package com.github.majisyou.Main;

import com.github.majisyou.FileManager.ConfigManager;
import com.github.majisyou.FileManager.SpawnerConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Sys_GuiMaster {

    public static Inventory getSpawnerGui(Location loc){
        SpawnerConfigManager scm = new SpawnerConfigManager(loc);
        Component title = Component.text("CustomSpawner");
        Inventory inventory = Bukkit.createInventory(null,27,title);

        ItemStack main__Icon = Sys_GuiItem.Icon(scm.getBlock(),Component.translatable(scm.getBlock()).color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC,false),Sys_GuiItem.getLore(scm,"mainIcon"));
        ItemStack freq__Icon = Sys_GuiItem.Icon(ConfigManager.getItemS("FrequencyIcon","ItemType"),Component.translatable("Spawn frequency").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC,false),Sys_GuiItem.getLore(scm,"frequency"));
        ItemStack range_Icon = Sys_GuiItem.Icon(ConfigManager.getItemS("RangeIcon","ItemType"),Component.translatable("Spawn range").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC,false),Sys_GuiItem.getLore(scm,"range"));
        ItemStack miningIcon = Sys_GuiItem.Icon(ConfigManager.getItemS("MiningIcon","ItemType"),Component.translatable("Mining times").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC,false),Sys_GuiItem.getLore(scm,"mining"));

        Sys_GuiItem.setLoc(main__Icon,loc);
        Sys_GuiItem.setType(freq__Icon,"Frequency");
        Sys_GuiItem.setType(range_Icon,"Range");
        Sys_GuiItem.setType(miningIcon,"Mining");

        ItemStack backGround = Sys_GuiItem.getItem("BackGround");
        ItemStack redCross__ = Sys_GuiItem.getItem("RedCross");

        ItemStack[] container = {
                backGround,backGround,backGround,backGround,backGround,backGround,backGround,backGround,backGround,
                backGround,main__Icon,backGround,freq__Icon,backGround,range_Icon,backGround,miningIcon,backGround,
                backGround,backGround,backGround,backGround,backGround,backGround,backGround,backGround,redCross__,
        };

        inventory.setContents(container);
        return inventory;
    }

    public static Inventory getConfirm(ItemStack main__Icon,ItemStack type_Plate){
        Component title = Component.text("CustomSpawner Confirm");
        Inventory inventory = Bukkit.createInventory(null,27,title);

        ItemStack back__Page = Sys_GuiItem.getItem("BackPage");

        ItemStack backGround = Sys_GuiItem.getItem("BackGround");
        ItemStack greenCheck = Sys_GuiItem.getItem("GreenCheck");
        ItemStack redCross__ = Sys_GuiItem.getItem("RedCross");



        ItemStack[] container = {
                backGround,backGround,backGround,backGround,backGround,backGround,backGround,backGround,backGround,
                backGround,greenCheck,backGround,backGround,backGround,backGround,backGround,redCross__,backGround,
                backGround,backGround,backGround,backGround,backGround,backGround,main__Icon,type_Plate,back__Page,
        };

        inventory.setContents(container);
        return inventory;
    }



}
