package com.github.majisyou.Command;

import com.github.majisyou.CustomSpawner;
import com.github.majisyou.Main.Sys_CustomSpawner;
import com.github.majisyou.Main.Sys_Enchantment;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;

public class Cmd_debug implements CommandExecutor {
    private static final CustomSpawner plugin = CustomSpawner.getInstance();
    public Cmd_debug(CustomSpawner plugin){
        Objects.requireNonNull(plugin.getCommand("CSDebug")).setExecutor(this);}

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player){
            if(args.length == 0){

                return true;
            }

            Collection<Entity> entityList = player.getLocation().getNearbyEntities(10,10,10);
            for (Entity e:entityList){
                if(e instanceof Player pl){
                    plugin.getLogger().info(pl.getOpenInventory().title().toString());

                }
            }
            plugin.getLogger().info(player.getOpenInventory().getTopInventory().getType().name());

            plugin.getLogger().info(player.getOpenInventory().title().toString());


            ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);
            Sys_Enchantment.addSpawnerBreak(itemStack);
            player.getInventory().addItem(itemStack);

            switch (args[0]){
                case "levelUp" ->{
                    if(args.length > 4){
                        levelUp(args,player);
                    }else {
                        player.sendMessage("LevelUPをする時は第二引数から第四引数までを座標にしてくだいさい");
                    }
                    return true;
                }
            }


            if(args.length == 3){
                Location loc = player.getLocation();
                Sys_CustomSpawner.loadSpawnerConfig(loc.getWorld(),Integer.parseInt(args[0]),Integer.parseInt(args[1]),Integer.parseInt(args[2]));
                return true;
            }
        }
        return false;
    }


    private static void levelUp(String[] args,Player player){
        Block block = player.getWorld().getBlockAt(strToInt(args[1]),strToInt(args[2]),strToInt(args[3]));
        if(!block.getState().getType().equals(Material.SPAWNER)){
            player.sendMessage("座標はSpawnerの位置を指定してください");
            return;
        }
    }

    private static int strToInt(String str){
        if(str.matches("[+-]?\\d*(\\.\\d+)?")){
            return Integer.parseInt(str);
        }
        return 0;
    }

}
