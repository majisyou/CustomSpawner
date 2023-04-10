package com.github.majisyou.Main;

import io.papermc.paper.enchantments.EnchantmentRarity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.EntityCategory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class Sys_EnchantWrapper extends Enchantment {

    private final String name;
    private final int maxLvl;
    private final Component display;
    private final boolean curse;

    public Sys_EnchantWrapper(String namespace,String name,int lvl, boolean curse){
        super(NamespacedKey.minecraft(namespace));
        this.name = name;
        this.maxLvl = lvl;
        this.display = Component.text(name);
        this.curse = curse;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getMaxLevel() {
        return maxLvl;
    }

    @Override
    public int getStartLevel() {
        return 0;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.TOOL;
    }

    @Override
    public boolean isTreasure() {
        return true;
    }

    @Override
    public boolean isCursed() {
        return curse;
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return true;
    }

    @Override
    public Component displayName(int level) {
        switch (level){
            case 1 ->{
                return display.decoration(TextDecoration.ITALIC,false);
            }
            case 2 ->{
                return display.append(Component.text(" II")).decoration(TextDecoration.ITALIC,false);
            }
            case 3 ->{
                return display.append(Component.text(" III")).decoration(TextDecoration.ITALIC,false);
            }
            case 4 ->{
                return display.append(Component.text(" IV")).decoration(TextDecoration.ITALIC,false);
            }
            case 5 ->{
                return display.append(Component.text(" V")).decoration(TextDecoration.ITALIC,false);
            }
            case 6 ->{
                return display.append(Component.text(" VI")).decoration(TextDecoration.ITALIC,false);
            }
            case 7 ->{
                return display.append(Component.text(" VII")).decoration(TextDecoration.ITALIC,false);
            }
            case 8 ->{
                return display.append(Component.text(" VIII")).decoration(TextDecoration.ITALIC,false);
            }
            case 9 ->{
                return display.append(Component.text(" IX")).decoration(TextDecoration.ITALIC,false);
            }
            case 10 ->{
                return display.append(Component.text(" X")).decoration(TextDecoration.ITALIC,false);
            }
            default -> {
                return display.append(Component.text(" "+level)).decoration(TextDecoration.ITALIC,false);
            }
        }
    }

    @Override
    public boolean isTradeable() {
        return false;
    }

    @Override
    public boolean isDiscoverable() {
        return false;
    }

    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.COMMON;
    }

    @Override
    public float getDamageIncrease(int level, EntityCategory entityCategory) {
        return 0;
    }

    @Override
    public Set<EquipmentSlot> getActiveSlots() {
        Set<EquipmentSlot> slots = new HashSet<>();
        slots.add(EquipmentSlot.HAND);
        return slots;
    }

    @Override
    public String translationKey() {
        return name;
    }

}
