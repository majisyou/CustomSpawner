package com.github.majisyou.Command;

import com.github.majisyou.CustomSpawner;
import com.github.majisyou.Main.Sys_CustomSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Cmd_admin implements CommandExecutor {
    public Cmd_admin(CustomSpawner plugin){plugin.getCommand("cs_Admin").setExecutor(this);}

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 0){
            sender.sendMessage("(CS)"+"Hello world");
            return true;
        }

        switch (args[0]){
            case "reloadSpawner" ->{
                Sys_CustomSpawner.reloadSpawner();
                return true;
            }

            case "setDefault" ->{

                return true;
            }

            default -> {
                return false;
            }
        }
    }
}
