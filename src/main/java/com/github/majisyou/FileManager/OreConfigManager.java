package com.github.majisyou.FileManager;

import com.github.majisyou.CustomSpawner;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class OreConfigManager {
    private final CustomSpawner plugin = CustomSpawner.getInstance();
    private final CustomFileManager customFileManager;
    private FileConfiguration config;
    private String block;
    private static Integer maxLevel;

    public OreConfigManager(Material material){
        String fileName = material.getKey().getKey()+".yml";
        customFileManager = new CustomFileManager(plugin,fileName,"Material");
        config = customFileManager.getConfig();
        loadConfig(material.getKey().getKey());
    }

    public void loadConfig(String  name){
        block = config.getString("OreName");
        if(block == null){
            ConfigurationSection section = ConfigManager.getTemplate();
            config.set("OreName",name);
            config.set("level",section);
            customFileManager.saveConfig();
            customFileManager.reloadConfig();
            config = customFileManager.getConfig();
        }
        maxLevel = config.getInt("level.maxLevel");
    }

    public short getSpawnCount(Integer level){
        String path = "level."+level+".SpawnCount";
        return readShort(config,path);
    }
    public short getSpawnRange(Integer level){
        String path = "level."+level+".SpawnRange";
        return readShort(config,path);
    }
    public short getMinSpawnDelay(Integer level){
        String path = "level."+level+".MinSpawnDelay";
        return readShort(config,path);
    }
    public short getMaxSpawnDelay(Integer level){
        String path = "level."+level+".MaxSpawnDelay";
        return readShort(config,path);
    }
    public short getRequiredPlayerRange(Integer level){
        String path = "level."+level+".RequiredPlayerRange";
        return readShort(config,path);
    }
    public Integer getCost(Integer level,String type){
        String path = "level."+level+".Cost."+type;
        return config.getInt(path);
    }
    public short getMiningTimes(Integer level){
        String path = "level."+level+".MiningTimes";
        return readShort(config,path);
    }

    public Integer getMiningTimesUnit(Integer level){
        String path = "level."+level+".MiningTimesUnit";
        return config.getInt(path);
    }

    public Integer getMaxLevel(){
        return maxLevel;
    }

    public short readShort(FileConfiguration config,String path){
        try {
            String num = config.getString(path);
            if(num != null){
                return Short.parseShort(num);
            }
        }catch (Exception ignored){}
        plugin.getLogger().info("(CS)"+"Configに設定されている"+path+"をshortに変換できなかった:"+config.getString(path));
        return 1;
    }

    public void reloadConfig(){
        customFileManager.reloadConfig();
        config = customFileManager.getConfig();
        loadConfig(block);
    }
}
