package com.github.majisyou.Main;

import com.github.majisyou.CustomSpawner;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sys_Enchantment {

    private static final CustomSpawner plugin = CustomSpawner.getInstance();
    public static final Enchantment spawnerBreak = new Sys_EnchantWrapper("spawner_break","SPAWNER_BREAK",1,false);

    public static void registerEnchant(){
        boolean registered = true;
        try {
            List<Enchantment> enchantments = Arrays.stream(Enchantment.values()).toList();
            if(!enchantments.contains(spawnerBreak)){
                Field f = Enchantment.class.getDeclaredField("acceptingNew");
                f.setAccessible(true);
                f.set(null,true);
                Enchantment.registerEnchantment(spawnerBreak);
                return;
            }
            registered = false;
        }catch (Exception e){
            plugin.getLogger().info("(CS)"+spawnerBreak.getKey().getKey()+"のエンチャントを追加できなかった");
            registered = false;
            e.printStackTrace();
        }
        if(registered){
            plugin.getLogger().info("(CS)"+spawnerBreak.getKey().getKey()+"のエンチャントを追加しました");
            //send message to console
        }
    }

    public static Enchantment getEnchant(){return spawnerBreak;}

    public static void addSpawnerBreak(ItemStack itemStack){
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<Component> lore = itemMeta.lore();
        if(lore == null) lore = new ArrayList<>();
        lore.add(Component.translatable(spawnerBreak.translationKey()).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC,false));
        itemMeta.lore(lore);

        if(itemMeta instanceof EnchantmentStorageMeta meta){
            meta.addStoredEnchant(spawnerBreak,1,true);
            itemStack.setItemMeta(itemMeta);
        }else {
            itemMeta.addEnchant(spawnerBreak,1,true);
            itemStack.setItemMeta(itemMeta);
        }
    }

    public static void removeSpawnerBreak(ItemStack itemStack){
        ItemMeta itemMeta = itemStack.getItemMeta();
        boolean ench = false;

        if(itemMeta instanceof EnchantmentStorageMeta meta){
            ench = meta.hasStoredEnchant(spawnerBreak);
        }else {
            ench = itemMeta.hasEnchant(spawnerBreak);
        }

        if(ench){
           return;
        }

        if(itemMeta.hasLore()){
            List<Component> lore = itemMeta.lore();
            if(lore == null){
                return;
            }
            lore.remove(Component.translatable(Sys_Enchantment.spawnerBreak.translationKey()).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC,false));
            itemMeta.lore(lore);
            itemStack.setItemMeta(itemMeta);
        }
    }


}
