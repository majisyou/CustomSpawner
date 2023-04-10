package com.github.majisyou.Event;

import com.destroystokyo.paper.event.entity.PreSpawnerSpawnEvent;
import com.github.majisyou.CustomSpawner;
import com.github.majisyou.FileManager.ConfigManager;
import com.github.majisyou.Main.Sys_CustomSpawner;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SpawnerEvent implements Listener {
    private static final CustomSpawner plugin = CustomSpawner.getInstance();
    public SpawnerEvent(CustomSpawner plugin){plugin.getServer().getPluginManager().registerEvents(this,plugin);}

    @EventHandler
    public static void SpawnerEvent(PreSpawnerSpawnEvent event){
        Location loc = event.getSpawnerLocation();
        try {
            CreatureSpawner spawner = (CreatureSpawner) loc.getBlock().getState();
            Sys_CustomSpawner.getMiningTimesUnit(spawner);
            if(ConfigManager.getEntityList().contains(spawner.getSpawnedType().getKey().getKey())){
                Block block = new Location(loc.getWorld(),loc.getBlockX(),loc.getBlockY()+1,loc.getBlockZ()).getBlock();
                if(!block.getType().equals(Material.AIR)){
                    Sys_CustomSpawner.initSpawner(spawner,block.getType(),loc);
                }
            }
        }catch (Exception e){
            String place = loc_to_String(loc);
            plugin.getLogger().info("(CP):"+place+"にあるPreCreatureSpawnEventからの返り値がスポナーなじゃなかった");
            event.setCancelled(true);
        }
    }
    public static String loc_to_String(Location loc){
        return "world:"+loc.getWorld().getName()+"_x:"+loc.getBlockX()+"_y:"+loc.getBlockY()+"_z:"+loc.getBlockZ();
    }
}
