package com.github.majisyou.Main;

import com.github.majisyou.CustomSpawner;
import com.github.majisyou.FileManager.ConfigManager;
import com.github.majisyou.FileManager.OreConfigManager;
import com.github.majisyou.FileManager.SpawnerConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class Sys_GuiItem {

    private static CustomSpawner plugin = CustomSpawner.getInstance();

    private static Map<String, ItemStack> items = new HashMap<>();

    public static ItemStack getItem(String name){
        return items.get(name);}
    public static Map<String,ItemStack> getItems(){return items;}

    public static void loadItems(){
        List<String> itemsList = ConfigManager.getItems();
        for (String item:itemsList){
            items.put(item,Item(item));
        }
    }

    private static ItemStack Item(String ItemName){
        String name = ConfigManager.getItemS(ItemName,"ItemType");
        String display = ConfigManager.getItemS(ItemName,"Display");
        List<String> lore = ConfigManager.getItemSlist(ItemName,"Lore");
        int customModel = ConfigManager.getItemI(ItemName,"CustomModel");
        ItemStack item = new ItemStack(getMaterial(name));
        ItemMeta itemMeta = item.getItemMeta();
        if(display != null) itemMeta.displayName(Component.translatable(display).decoration(TextDecoration.ITALIC,false));
        if(lore.size() != 0) itemMeta.lore(getLore(lore));
        itemMeta.setCustomModelData(customModel);
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack Icon(String name,Component display,List<Component> lore){
        ItemStack itemStack = new ItemStack(getMaterial(name));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(display);
        itemMeta.lore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private static List<Component> getLore(List<String> strings){
        List<Component> lore = new ArrayList<>();
        for (String l : strings) {
            lore.add(Component.text(l));
        }
        return lore;
    }

    private static Material getMaterial(String name){
        try {
            Material material = Material.getMaterial(name);
            if(material != null) return material;
            material = Sys_CustomSpawner.getMaterial(name);
            if(material == Material.SAND) plugin.getLogger().info("(CS)"+name+"という名前は間違ってる。https://hub.spigotmc.org/javadocs/bukki/org/bukkit/Material.html を参照してください");
            return material;
        }catch (Exception e){
            plugin.getLogger().info("(CS)"+name+"という名前のアイテムはBukkitでは認識しない");
            return Material.WHITE_STAINED_GLASS_PANE;
        }
    }

    public static void setType(ItemStack itemStack,String type){
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin,"type");
        pdc.set(key, PersistentDataType.STRING,type);
        itemStack.setItemMeta(itemMeta);
    }

    public static String getType(ItemStack itemStack){
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin,"type");
        return pdc.get(key,PersistentDataType.STRING);
    }

    public static void setLoc(ItemStack itemStack, Location location){
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
        NamespacedKey world = new NamespacedKey(plugin,"world");
        NamespacedKey x = new NamespacedKey(plugin,"x");
        NamespacedKey y = new NamespacedKey(plugin,"y");
        NamespacedKey z = new NamespacedKey(plugin,"z");
        pdc.set(world, PersistentDataType.STRING,location.getWorld().getName());
        pdc.set(x,PersistentDataType.INTEGER,location.getBlockX());
        pdc.set(y,PersistentDataType.INTEGER,location.getBlockY());
        pdc.set(z,PersistentDataType.INTEGER,location.getBlockZ());
        itemStack.setItemMeta(itemMeta);
    }

    public static Location getLoc(ItemStack itemStack){
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
        NamespacedKey worldKey = new NamespacedKey(plugin,"world");
        NamespacedKey xKey = new NamespacedKey(plugin,"x");
        NamespacedKey yKey = new NamespacedKey(plugin,"y");
        NamespacedKey zKey = new NamespacedKey(plugin,"z");

        if(!pdc.getKeys().contains(worldKey)  || !pdc.getKeys().contains(xKey) || !pdc.getKeys().contains(yKey) || !pdc.getKeys().contains(zKey)){
            return null;
        }

        try {
            World world = plugin.getServer().getWorld(Objects.requireNonNull(pdc.get(worldKey, PersistentDataType.STRING)));
            int x = pdc.get(xKey,PersistentDataType.INTEGER);
            int y = pdc.get(yKey,PersistentDataType.INTEGER);
            int z = pdc.get(zKey,PersistentDataType.INTEGER);
            return new Location(world,x,y,z);
        }catch (Exception e){
            e.printStackTrace();
            plugin.getLogger().info("(CS)"+"アイテムからLocationを取得するときにエラーが出た");
        }
        return null;
    }

    public static List<Component> getLore(SpawnerConfigManager scm, String type){
        List<Component> lore = new ArrayList<>();
        switch (type){
            case "mainIcon" ->{
                lore.add(Component.translatable("Level").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC,false));
                lore.add(Component.text("  ").append(Component.translatable("Spawn frequency").color(NamedTextColor.WHITE)).append(Component.text(": ").color(NamedTextColor.WHITE)).append(Component.text(scm.getLevel("Frequency")).color(NamedTextColor.YELLOW)).decoration(TextDecoration.ITALIC,false));
                lore.add(Component.text("  ").append(Component.translatable("Spawn range").color(NamedTextColor.WHITE)).append(Component.text(": ").color(NamedTextColor.WHITE)).append(Component.text(scm.getLevel("Range")).color(NamedTextColor.YELLOW)).decoration(TextDecoration.ITALIC,false));
                lore.add(Component.text("  ").append(Component.translatable("Mining times").color(NamedTextColor.WHITE)).append(Component.text(": ").color(NamedTextColor.WHITE)).append(Component.text(scm.getLevel("Mining")).color(NamedTextColor.YELLOW)).decoration(TextDecoration.ITALIC,false));
                return lore;
            }

            case "frequency" ->{
                OreConfigManager ocm = new OreConfigManager(Sys_CustomSpawner.getMaterial(scm.getBlock()));
                lore.add(Component.translatable("This level is ").color(NamedTextColor.WHITE).append(Component.text(scm.getLevel("Frequency")).color(NamedTextColor.YELLOW)).decoration(TextDecoration.ITALIC,false));
                if(scm.getLevel("Frequency") < ocm.getMaxLevel()){
                    lore.add(Component.translatable("Next level is needed ").color(NamedTextColor.WHITE).append(Component.text(ocm.getCost(scm.getLevel("Frequency"),"Frequency")).color(NamedTextColor.YELLOW)).decoration(TextDecoration.ITALIC,false));
                    lore.add(Component.translatable("Left click").color(NamedTextColor.GREEN).append(Component.text(" increase this level").color(NamedTextColor.WHITE)).decoration(TextDecoration.ITALIC,false));
                }else {
                    lore.add(Component.translatable("Maximal level").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC,false));
                }
                return lore;
            }

            case "range" ->{
                OreConfigManager ocm = new OreConfigManager(Sys_CustomSpawner.getMaterial(scm.getBlock()));
                lore.add(Component.translatable("This level is ").color(NamedTextColor.WHITE).append(Component.text(scm.getLevel("Range")).color(NamedTextColor.YELLOW)).decoration(TextDecoration.ITALIC,false));
                if(scm.getLevel("Range") < ocm.getMaxLevel()){
                    lore.add(Component.translatable("Next level is needed ").color(NamedTextColor.WHITE).append(Component.text(ocm.getCost(scm.getLevel("Range"),"Range")).color(NamedTextColor.YELLOW)).decoration(TextDecoration.ITALIC,false));
                    lore.add(Component.translatable("Left click").color(NamedTextColor.GREEN).append(Component.text(" increase this level").color(NamedTextColor.WHITE)).decoration(TextDecoration.ITALIC,false));
                }else {
                    lore.add(Component.translatable("Maximal level").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC,false));
                }
                return lore;
            }

            case "mining" ->{
                OreConfigManager ocm = new OreConfigManager(Sys_CustomSpawner.getMaterial(scm.getBlock()));
                lore.add(Component.translatable("This level is ").color(NamedTextColor.WHITE).append(Component.text(scm.getLevel("Mining")).color(NamedTextColor.YELLOW)).decoration(TextDecoration.ITALIC,false));
                if(scm.getLevel("Mining") < ocm.getMaxLevel()){
                    lore.add(Component.translatable("Next level is needed ").color(NamedTextColor.WHITE).append(Component.text(ocm.getCost(scm.getLevel("Mining"),"Mining")).color(NamedTextColor.YELLOW)).decoration(TextDecoration.ITALIC,false));
                    lore.add(Component.translatable("Left click").color(NamedTextColor.GREEN).append(Component.text(" increase this level").color(NamedTextColor.WHITE)).decoration(TextDecoration.ITALIC,false));
                }else {
                    lore.add(Component.translatable("Maximal level").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC,false));
                }
                return lore;
            }

        }
        plugin.getLogger().info("(CS)"+"ICON作製の時のTypeが"+type+"になっている。開発者に連絡取ってください");
        return null;
    }

}
