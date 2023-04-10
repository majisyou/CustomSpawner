package com.github.majisyou.FileManager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

public class CustomFileManager {

    private FileConfiguration config = null;
    private File configFile;
    private String file;
    private final Plugin plugin;
    private final File dir;

    public CustomFileManager(Plugin plugin, String fileName,String DirName){
        //file名のconfigを読み込むソース
        this.plugin = plugin;
        this.file = fileName;
        this.dir = loadDirectory(plugin,DirName);
        configFile = new File(dir,file);
    }

    public CustomFileManager(Plugin plugin,String DirName){
        this.plugin = plugin;
        this.dir = loadDirectory(plugin,DirName);
    }

    public File loadDirectory(Plugin plugin,String DirName){
        File newDir = plugin.getDataFolder().toPath().resolve(DirName).toFile();
        if(!newDir.exists()){
            if(newDir.mkdir()){
                plugin.getLogger().info("(CS)"+DirName+"のフォルダを作成した");
            }else {
                plugin.getLogger().info("(CS)"+DirName+"のフォルダを作成できなかった");
            }
        }
        return newDir;
    }

    public void createFile(String fileName){
        configFile = new File(dir,fileName);
        if(!configFile.exists()){
            try {
                Files.createFile(Path.of(configFile.getAbsolutePath()));
            }catch (IOException e){
                plugin.getLogger().info("(CS)"+fileName+"を作成できなかった");
            }
        }
    }

    public void saveDefaultConfig(){
        if(!configFile.exists()){
            //コンフィグファイルが存在するならsaveResourceでファイルを保存
            plugin.saveResource(file,false);
            plugin.getLogger().info("(CS)"+file+"がなかったから"+file+"を作成したよ");
        }
    }

    public void createConfig() throws Exception {
        if(!configFile.exists()){
            //Files.createFileには絶対的なPathが必要ということでconfigFileの絶対Pathの作成
            Files.createFile(Path.of(configFile.getAbsolutePath()));
            plugin.getLogger().info("(CS)"+file+"がなかったから"+file+"を作成したよ");
        }
    }

    public void reloadConfig(){
        config = YamlConfiguration.loadConfiguration(configFile);

        final InputStream defConfigStream = plugin.getResource(file);
        if(defConfigStream == null){
            return;
        }

        config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, StandardCharsets.UTF_8)));
    }

    public FileConfiguration getConfig(){
        if(config == null){
            reloadConfig();
        }
        return config;
    }

    public void deleteConfig(){
        if(configFile.delete()){
            plugin.getLogger().info("(CS)"+file+" is deleted");
        }else {
            plugin.getLogger().info("(CS)"+file+" cant be deleted");
        }
    }

    public void saveConfig(){
        if(config == null){
            return;
        }
        try {
            getConfig().save(configFile);
        } catch(IOException ex){
            plugin.getLogger().log(Level.SEVERE,"(CS)"+"Could not save config to" + configFile, ex);
        }
    }

}
