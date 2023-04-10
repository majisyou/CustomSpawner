package com.github.majisyou.FileManager;

import com.github.majisyou.CustomSpawner;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    private static final CustomSpawner plugin = CustomSpawner.getInstance();
    private static FileConfiguration config;
    private static ConfigurationSection template;
    private static List<String> entityList = new ArrayList<>();
    private static List<String> items;
    private static Integer maxLevel;
    private static List<String> spawners;

    public static void loadConfig(CustomSpawner plugin){
        config = plugin.getConfig();
        entityList = config.getStringList("changeSpawnerType");
        items = config.getStringList("Item.list");
        maxLevel = config.getInt("default.maxLevel");
        template = config.getConfigurationSection("default");
        spawners = config.getStringList("spawner");
    }


    public static short readShort(FileConfiguration config,String path){
        try {
            String num = config.getString(path);
            if(num != null){
                return Short.parseShort(num);
            }
        }catch (Exception ignored){}
        plugin.getLogger().info("(CS)"+"Configに設定されている"+path+"をshortに変換できなかった:"+config.getString(path));
        return 1;
    }

    public static List<String> getEntityList(){return entityList;}
    public static Integer getMaxLevel(){return maxLevel;}
    public static ConfigurationSection getTemplate(){return template;}
    public static short getSpawnCount(Integer level){
        String path = "default."+level+".SpawnCount";
        return readShort(config,path);
    }
    public static short getSpawnRange(Integer level){
        String path = "default."+level+".SpawnRange";
        return readShort(config,path);
    }
    public static short getMinSpawnDelay(Integer level){
        String path = "default."+level+".MinSpawnDelay";
        return readShort(config,path);
    }
    public static short getMaxSpawnDelay(Integer level){
        String path = "default."+level+".MaxSpawnDelay";
        return readShort(config,path);
    }
    public static short getRequiredPlayerRange(Integer level){
        String path = "default."+level+".RequiredPlayerRange";
        return readShort(config,path);
    }
    public static Integer getCost(Integer level){
        String path = "default."+level+".Cost";
        return config.getInt(path);
    }
    public static Integer getMiningTimes(String name){
        String path = "MiningTimes."+name;
        return config.getInt(path);
    }
    public static List<String> getItems(){return items;}

    public static String getItemS(String name,String type){
        String path = "Item."+name+"."+type;
        return config.getString(path);
    }
    public static Integer getItemI(String name,String type){
        String path = "Item."+name+"."+type;
        return config.getInt(path);
    }
    public static List<String> getItemSlist(String name,String type){
        String path = "Item."+name+"."+type;
        return config.getStringList(path);
    }

    public static List<String> getSpawners(){return spawners;}

    public static void saveSpawners(String name){
        if(spawners.contains(name)){
            plugin.getLogger().info("(CS)"+name+"はすでに登録済み");
            return;
        }
        spawners.add(name);
        config.set("spawner",spawners);
        plugin.saveConfig();
        plugin.reloadConfig();
        loadConfig(plugin);
    }

    public static void removeSpawners(String name){
        if(!spawners.contains(name)){
            plugin.getLogger().info("(CS)"+name+"は登録されえいない");
            return;
        }
        spawners.remove(name);
        config.set("spawner",spawners);
        plugin.saveConfig();
        plugin.reloadConfig();
        loadConfig(plugin);
    }





}
