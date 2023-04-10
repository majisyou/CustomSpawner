package com.github.majisyou.Command;

import com.github.majisyou.CustomSpawner;
import com.github.majisyou.Main.Sys_Enchantment;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.jetbrains.annotations.NotNull;

public class Cmd_user implements CommandExecutor {

    public Cmd_user(CustomSpawner plugin){plugin.getCommand("spawnerBreak").setExecutor(this);}
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player){
            ItemStack itemStack = new ItemStack(Material.IRON_PICKAXE);
            Damageable meta =(Damageable) itemStack.getItemMeta();
            Component name = Component.translatable("Spawner Breaker").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC,false);
            meta.displayName(name);
            meta.addEnchant(Sys_Enchantment.spawnerBreak,1,true);
            meta.setDamage(250);
            itemStack.setItemMeta(meta);

            if(player.getInventory().firstEmpty() == -1){
                player.getWorld().dropItem(player.getLocation(),itemStack);
            }
            player.getInventory().addItem(itemStack);
            return true;
        }
        sender.sendMessage("Playerのみのコマンドです");
        return true;
    }
}
