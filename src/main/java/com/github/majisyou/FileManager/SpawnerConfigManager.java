package com.github.majisyou.FileManager;

import com.github.majisyou.CustomSpawner;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SpawnerConfigManager {
    private final CustomSpawner plugin = CustomSpawner.getInstance();
    private final CustomFileManager customFileManager;
    private FileConfiguration config;
    private short spawnCount;
    private short spawnRange;
    private short minSpawnDelay;
    private short maxSpawnDelay;
    private short requiredPlayerRange;
    private short miningTimesUnit;

    private Map<String,Integer> level = new HashMap<>();
    private String block;
    private short miningTimes;

    public SpawnerConfigManager(Location loc){
        String fileName = loc.getWorld().getName()+"_"+loc.getBlockX()+"_"+loc.getBlockY()+"_"+loc.getBlockZ()+".yml";
        customFileManager = new CustomFileManager(plugin,fileName,"Spawner");
        config = customFileManager.getConfig();
        loadConfig();
    }

    public void loadConfig(){
        block = config.getString("Block");
        if(block != null){
            miningTimes = readShort(config,"MiningTimes");
            miningTimesUnit = readShort(config,"MiningTimesUnit");
            spawnCount = readShort(config,"SpawnCount");
            spawnRange = readShort(config,"SpawnRange");
            minSpawnDelay = readShort(config,"MinSpawnDelay");
            maxSpawnDelay = readShort(config,"MaxSpawnDelay");
            requiredPlayerRange = readShort(config,"RequiredPlayerRange");

            level.put("Frequency",config.getInt("level.Frequency"));
            level.put("Range",config.getInt("level.Range"));
            level.put("Mining",config.getInt("level.Mining"));
        }
    }

    public void saveConfig(String block, int spawnCount, int spawnRange, int  minSpawnDelay, int maxSpawnDelay,int requiredPlayerRange,int miningTimes,int miningTimesUnit,int freqLev,int rangeLev,int miningLev){
        config.set("Block",block);
        config.set("SpawnCount",spawnCount);
        config.set("SpawnRange",spawnRange);
        config.set("MinSpawnDelay",minSpawnDelay);
        config.set("MaxSpawnDelay",maxSpawnDelay);
        config.set("RequiredPlayerRange",requiredPlayerRange);
        config.set("MiningTimes",miningTimes);
        config.set("MiningTimesUnit",miningTimesUnit);
        config.set("level.Frequency",freqLev);
        config.set("level.Range",rangeLev);
        config.set("level.Mining",miningLev);

        customFileManager.saveConfig();
        customFileManager.reloadConfig();
        config = customFileManager.getConfig();
        loadConfig();
    }

    public void saveConfig(int spawnCount, int spawnRange, int  minSpawnDelay, int maxSpawnDelay,int requiredPlayerRange,int miningTimes,int miningTimesUnit,int freqLev,int rangeLev,int miningLev){
        config.set("SpawnCount",spawnCount);
        config.set("SpawnRange",spawnRange);
        config.set("MinSpawnDelay",minSpawnDelay);
        config.set("MaxSpawnDelay",maxSpawnDelay);
        config.set("RequiredPlayerRange",requiredPlayerRange);
        config.set("MiningTimes",miningTimes);
        config.set("MiningTimesUnit",miningTimesUnit);
        config.set("level.Frequency",freqLev);
        config.set("level.Range",rangeLev);
        config.set("level.Mining",miningLev);

        customFileManager.saveConfig();
        customFileManager.reloadConfig();
        config = customFileManager.getConfig();
        loadConfig();
    }

    public void saveConfig(String path, Objects value){
        config.set(path,value);
        customFileManager.saveConfig();
        customFileManager.reloadConfig();
        config = customFileManager.getConfig();
        loadConfig();
    }

    public short readShort(FileConfiguration config,String path){
        try {
            String num = config.getString(path);
            if(num != null){
                return Short.parseShort(num);
            }
        }catch (Exception ignored){}
        plugin.getLogger().info("(CS)"+"Configに設定されている"+path+"をshortに変換できなかった:"+config.getString(path)+"の値");
        return 1;
    }

    public short getSpawnCount(){return spawnCount;}
    public short getSpawnRange(){return spawnRange;}
    public short getMinSpawnDelay(){return minSpawnDelay;}
    public short getMaxSpawnDelay(){return maxSpawnDelay;}
    public short getRequiredPlayerRange(){return requiredPlayerRange;}
    public String getBlock(){return block;}
    public short getMiningTimes(){return miningTimes;}
    public short getMiningTimesUnit(){return miningTimesUnit;}

    public Integer getLevel(String type){
        if(level.containsKey(type)){
            return level.get(type);
        }
        plugin.getLogger().info("(CS)"+config.getName()+"のlevel."+type+"のレベルが登録されていなかった。したがって1を返す");
        return 1;
    }

    public void saveConfig(String path, int value) {
        config.set(path,value);
        customFileManager.saveConfig();
        customFileManager.reloadConfig();
        config = customFileManager.getConfig();
        loadConfig();
    }

    public void deleteThis(){
        customFileManager.deleteConfig();
    }

}
