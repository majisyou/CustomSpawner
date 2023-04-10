package com.github.majisyou.Main;

import com.github.majisyou.CustomSpawner;
import com.github.majisyou.FileManager.ConfigManager;
import com.github.majisyou.FileManager.OreConfigManager;
import com.github.majisyou.FileManager.SpawnerConfigManager;
import de.tr7zw.nbtapi.NBTContainer;
import de.tr7zw.nbtapi.NBTTileEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class Sys_CustomSpawner {
    private static CustomSpawner plugin = CustomSpawner.getInstance();


//    public static void mergeSpawner(CreatureSpawner spawner, NamespacedKey key, short spawnCount, short spawnRange, short minSpawnDelay, short maxSpawnDelay, short requirePlayerRange,short miningTimes,short miningTimesUnit){
//        NBTTileEntity tileEntity = new NBTTileEntity(spawner.getBlock().getState());
//        String  entityName = mergeFalling_block(key.asString(),(short) 1,false,false, (short) 0, (short) 0);
//        tileEntity.setShort("SpawnCount",spawnCount);
//        tileEntity.setShort("SpawnRange",spawnRange);
//        tileEntity.setShort("MinSpawnDelay",minSpawnDelay);
//        tileEntity.setShort("MaxSpawnDelay",maxSpawnDelay);
//        tileEntity.setShort("RequirePlayerRange",requirePlayerRange);
//        tileEntity.mergeCompound(new NBTContainer(entityName));
//    }

    public static void mergeSpawner(CreatureSpawner spawner, String key, short spawnCount, short spawnRange, short minSpawnDelay, short maxSpawnDelay, short requirePlayerRange,short miningTimes,short miningTimesUnit){
        NBTTileEntity tileEntity = new NBTTileEntity(spawner.getBlock().getState());
        String  entityName = mergeFalling_block(key,(short) 1,false,false, miningTimesUnit, (short) 0);
        tileEntity.setShort("SpawnCount",spawnCount);
        tileEntity.setShort("SpawnRange",spawnRange);
        tileEntity.setShort("MinSpawnDelay",minSpawnDelay);
        tileEntity.setShort("MaxSpawnDelay",maxSpawnDelay);
        tileEntity.setShort("RequirePlayerRange",requirePlayerRange);
        tileEntity.setShort("MaxNearbyEntities",miningTimes);
        tileEntity.mergeCompound(new NBTContainer(entityName));
    }

    public static void mergeSpawner(CreatureSpawner spawner, String entityName){
        NBTTileEntity tileEntity = new NBTTileEntity(spawner.getBlock().getState());
        tileEntity.mergeCompound(new NBTContainer(entityName));
    }


    public static String mergeFalling_block(String block,short time,boolean dropItem,boolean hurtEntities,int fallHurtMax,float fallHurtAmount){
        return "{SpawnData:{entity:"+"{id:\"minecraft:falling_block\",BlockState:{Name:\"minecraft:"+block+"\"},Time:"+time+",DropItem:"+dropItem+",HurtEntities:"+hurtEntities+",FallHurtMax:"+fallHurtMax+",FallHurtAmount:"+fallHurtAmount+"}}}";
    }

    public static void mergeSpawner(CreatureSpawner spawner, short spawnCount, short spawnRange, short minSpawnDelay, short maxSpawnDelay, short requirePlayerRange){
        NBTTileEntity tileEntity = new NBTTileEntity(spawner.getBlock().getState());
        tileEntity.setShort("SpawnCount",spawnCount);
        tileEntity.setShort("SpawnRange",spawnRange);
        tileEntity.setShort("MinSpawnDelay",minSpawnDelay);
        tileEntity.setShort("MaxSpawnDelay",maxSpawnDelay);
        tileEntity.setShort("RequirePlayerRange",requirePlayerRange);
    }

    public static void mergeSpawner(CreatureSpawner spawner, NamespacedKey block){
        NBTTileEntity tileEntity = new NBTTileEntity(spawner.getBlock().getState());
        String EntityName = "{SpawnData:{entity:{id:\"minecraft:falling_block\",BlockState:{Name:\""+block.asString()+"\"},Time:1}}}";
        tileEntity.mergeCompound(new NBTContainer(EntityName));
    }

    public static void mergeSpawner(CreatureSpawner spawner, String tag, short value){
        NBTTileEntity tileEntity = new NBTTileEntity(spawner.getBlock().getState());
        tileEntity.setShort(tag,value);
    }

    public static void mergeSpawner(CreatureSpawner spawner, String tag, String value){
        NBTTileEntity tileEntity = new NBTTileEntity(spawner.getBlock().getState());
        tileEntity.setString(tag,value);
    }

    public static void mergeSpawner(CreatureSpawner spawner, String tag, boolean value){
        NBTTileEntity tileEntity = new NBTTileEntity(spawner.getBlock().getState());
        tileEntity.setBoolean(tag,value);
    }


    public static void mergeSpawner(CreatureSpawner spawner,SpawnerConfigManager scm){
        mergeSpawner(spawner,scm.getBlock(),scm.getSpawnCount(),scm.getSpawnRange(),scm.getMinSpawnDelay(),scm.getMaxSpawnDelay(),scm.getRequiredPlayerRange(),scm.getMiningTimes(),scm.getMiningTimesUnit());
    }

    public static void loadSpawnerConfig(World world,int x,int y,int z){
        Location loc = new Location(world,x,y,z);
        SpawnerConfigManager scm = new SpawnerConfigManager(loc);
        CreatureSpawner spawner = (CreatureSpawner) world.getBlockAt(x,y,z).getState();
//        plugin.getLogger().info(getSpawnerLevelUpCost(spawner)+"の値");
    }

    public static void levelUPSpawner(Location loc,String type,Player player){
        SpawnerConfigManager scm = new SpawnerConfigManager(loc);
        OreConfigManager ocm = new OreConfigManager(getMaterial(scm.getBlock()));
        CreatureSpawner spawner = (CreatureSpawner) loc.getWorld().getBlockAt(loc.getBlockX(),loc.getBlockY(), loc.getBlockZ()).getState();
        int cost = ocm.getCost(scm.getLevel(type),type);
        CustomSpawner.getEconomy().withdrawPlayer(player,cost);
        switch (type){
            case "Frequency" ->{
                mergeSpawner(spawner,"SpawnCount",ocm.getSpawnCount(scm.getLevel(type)+1));
                mergeSpawner(spawner,"MinSpawnDelay",ocm.getMinSpawnDelay(scm.getLevel(type)+1));
                mergeSpawner(spawner,"MaxSpawnDelay",ocm.getMaxSpawnDelay(scm.getLevel(type)+1));

                scm.saveConfig("SpawnCount",ocm.getSpawnCount(scm.getLevel(type)+1));
                scm.saveConfig("MinSpawnDelay",ocm.getMinSpawnDelay(scm.getLevel(type)+1));
                scm.saveConfig("MaxSpawnDelay",ocm.getMaxSpawnDelay(scm.getLevel(type)+1));
                scm.saveConfig("level."+type,scm.getLevel(type)+1);
            }
            case "Range" ->{
                mergeSpawner(spawner,"SpawnRange",ocm.getSpawnRange(scm.getLevel(type)+1));
                mergeSpawner(spawner,"RequiredPlayerRange",ocm.getRequiredPlayerRange(scm.getLevel(type)+1));

                scm.saveConfig("SpawnRange",ocm.getSpawnRange(scm.getLevel(type)+1));
                scm.saveConfig("RequiredPlayerRange",ocm.getRequiredPlayerRange(scm.getLevel(type)+1));
                scm.saveConfig("level."+type,scm.getLevel(type)+1);
            }
            case "Mining" ->{
                String  entityName = mergeFalling_block(scm.getBlock(), (short) 1,false,false, ocm.getMiningTimesUnit(scm.getLevel(type)+1), (short) 0);
                mergeSpawner(spawner,entityName);
                mergeSpawner(spawner,"MaxNearbyEntities",ocm.getMiningTimes(scm.getLevel(type)+1));

                scm.saveConfig("MiningTimes",ocm.getMiningTimes(scm.getLevel(type)+1));
                scm.saveConfig("MiningTimesUnit",ocm.getMiningTimesUnit(scm.getLevel(type)+1));
                scm.saveConfig("level."+type,scm.getLevel(type)+1);
            }
            default -> {
                plugin.getLogger().info("(CS)"+"LevelUp処理に"+type+"というタイプが判定にきた");
            }
        }
    }

    public static void levelDownSpawner(Location loc,String type){
        SpawnerConfigManager scm = new SpawnerConfigManager(loc);
        OreConfigManager ocm = new OreConfigManager(getMaterial(scm.getBlock()));
        CreatureSpawner spawner = (CreatureSpawner) loc.getWorld().getBlockAt(loc.getBlockX(),loc.getBlockY(), loc.getBlockZ()).getState();

        switch (type){
            case "Frequency" ->{
                mergeSpawner(spawner,"SpawnCount",ocm.getSpawnCount(scm.getLevel(type)-1));
                mergeSpawner(spawner,"MinSpawnDelay",ocm.getMinSpawnDelay(scm.getLevel(type)-1));
                mergeSpawner(spawner,"MaxSpawnDelay",ocm.getMaxSpawnDelay(scm.getLevel(type)-1));

                scm.saveConfig("SpawnCount",ocm.getSpawnCount(scm.getLevel(type)-1));
                scm.saveConfig("MinSpawnDelay",ocm.getMinSpawnDelay(scm.getLevel(type)-1));
                scm.saveConfig("MaxSpawnDelay",ocm.getMaxSpawnDelay(scm.getLevel(type)-1));
                scm.saveConfig("level."+type,scm.getLevel(type)-1);
            }
            case "Range" ->{
                mergeSpawner(spawner,"SpawnRange",ocm.getSpawnRange(scm.getLevel(type)-1));
                mergeSpawner(spawner,"RequiredPlayerRange",ocm.getRequiredPlayerRange(scm.getLevel(type)-1));

                scm.saveConfig("SpawnRange",ocm.getSpawnRange(scm.getLevel(type)-1));
                scm.saveConfig("RequiredPlayerRange",ocm.getRequiredPlayerRange(scm.getLevel(type)-1));
                scm.saveConfig("level."+type,scm.getLevel(type)-1);
            }
            case "Mining" ->{
                String  entityName = mergeFalling_block(scm.getBlock(), (short) 1,false,false, ocm.getMiningTimesUnit(scm.getLevel(type)+1), (short) 0);
                mergeSpawner(spawner,entityName);
                mergeSpawner(spawner,"MaxNearbyEntities",ocm.getMiningTimes(scm.getLevel(type)-1));

                scm.saveConfig("MiningTimes",ocm.getMiningTimes(scm.getLevel(type)-1));
                scm.saveConfig("MiningTimesUnit",ocm.getMiningTimesUnit(scm.getLevel(type)-1));
                scm.saveConfig("level"+type,scm.getLevel(type)-1);
            }
            default -> {
                plugin.getLogger().info("(CS)"+"LevelUp処理に"+type+"というタイプが判定にきた");
            }
        }
    }

    public static int getMiningTimes(CreatureSpawner spawner){
        NBTTileEntity tileEntity = new NBTTileEntity(spawner.getBlock().getState());
        short unit = (short) getMiningTimesUnit(spawner);
        return (tileEntity.getShort("MaxNearbyEntities") * ((short) Math.pow(10,unit)));
    }

    public static int getMiningTimesUnit(CreatureSpawner spawner){
        NBTTileEntity tileEntity = new NBTTileEntity(spawner.getBlock().getState());
        if(tileEntity.getCompound("SpawnData") != null){
            return tileEntity.getCompound("SpawnData").getCompound("entity").getInteger("FallHurtMax");
        }
        return 0;
    }

    public static String getBlock(CreatureSpawner spawner){
        NBTTileEntity tileEntity = new NBTTileEntity(spawner.getBlock().getState());
        return tileEntity.getCompound("SpawnData").getCompound("entity").getCompound("BlockState").getString("Name");
    }

    public static Material getMaterial(String key){
        Material material = Material.matchMaterial(key);
        if(material != null){
            return material;
        }
        switch (key){
            default -> {
                plugin.getLogger().info("(CS)"+key+"というブロックはBukkitでは異なる名前になっている");
                return Material.SAND;
            }
        }
    }

    public static void initSpawner(CreatureSpawner spawner,Material block,Location loc){
        OreConfigManager ocm = new OreConfigManager(block);
        SpawnerConfigManager scm = new SpawnerConfigManager(loc);
        scm.saveConfig(block.getKey().getKey(),ocm.getSpawnCount(1),ocm.getSpawnRange(1),ocm.getMinSpawnDelay(1),ocm.getMaxSpawnDelay(1),ocm.getRequiredPlayerRange(1),ocm.getMiningTimes(1),ocm.getMiningTimesUnit(1),1,1,1);
        Sys_CustomSpawner.mergeSpawner(spawner, scm);

        ConfigManager.saveSpawners(loc.getWorld().getName()+"_"+loc.getBlockX()+"_"+loc.getBlockY()+"_"+loc.getBlockZ());
    }

    public static void reloadSpawner(){
        List<String> spawners = ConfigManager.getSpawners();
        for (String spawner: spawners){
            Location loc = getLoc(spawner.split("_"));
            if(loc == null) continue;

            BlockState block = loc.getBlock().getState();
            BlockState above = new Location(loc.getWorld(),loc.getBlockX(),loc.getBlockY()+1,loc.getBlockZ()).getBlock().getState();

            if(block instanceof CreatureSpawner creatureSpawner){
                if(!creatureSpawner.getSpawnedType().equals(EntityType.FALLING_BLOCK)){
                    plugin.getLogger().info("(CS)"+spawner+"のスポナーの中身がFallingBlockではない");
                    continue;
                }

                if(above.getType().equals(Material.AIR)){
                    plugin.getLogger().info("(CS)"+spawner+"のスポナーの上のブロックがAIR");
                    continue;
                }


                SpawnerConfigManager scm = new SpawnerConfigManager(loc);
                OreConfigManager ocm = new OreConfigManager(getMaterial(scm.getBlock()));

                scm.saveConfig(scm.getBlock(), ocm.getSpawnCount(scm.getLevel("Frequency")),ocm.getSpawnRange(scm.getLevel("Range")),ocm.getMinSpawnDelay(scm.getLevel("Frequency")),ocm.getMaxSpawnDelay(scm.getLevel("Frequency")),ocm.getRequiredPlayerRange(scm.getLevel("Range")),ocm.getMiningTimes(scm.getLevel("Mining")),ocm.getMiningTimesUnit(scm.getLevel("Mining")),scm.getLevel("Frequency"),scm.getLevel("Range"),scm.getLevel("Mining"));
                Sys_CustomSpawner.mergeSpawner(creatureSpawner, scm);
            }else {
                plugin.getLogger().info("(CS)"+spawner+"はスポナーではない");
            }
        }
    }

    private static Location getLoc(String[] shugou){
        if(shugou.length != 4){
            plugin.getLogger().info("(CS)"+ Arrays.toString(shugou) +"は場所を表していない");
            return null;
        }

        String world = shugou[0];
        int x = strToInt(shugou[1]);
        int y = strToInt(shugou[2]);
        int z = strToInt(shugou[3]);

        return new Location(plugin.getServer().getWorld(world),x,y,z);
    }

    private static int strToInt(String str){
        if(str.matches("[+-]?\\d*(\\.\\d+)?")){
            return Integer.parseInt(str);
        }
        return 0;
    }
}
