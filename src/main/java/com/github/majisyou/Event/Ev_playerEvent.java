package com.github.majisyou.Event;

import com.github.majisyou.CustomSpawner;
import com.github.majisyou.FileManager.ConfigManager;
import com.github.majisyou.FileManager.OreConfigManager;
import com.github.majisyou.FileManager.SpawnerConfigManager;
import com.github.majisyou.Main.Sys_CustomSpawner;
import com.github.majisyou.Main.Sys_Enchantment;
import com.github.majisyou.Main.Sys_GuiItem;
import com.github.majisyou.Main.Sys_GuiMaster;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.Objects;
import java.util.Random;

public class Ev_playerEvent implements Listener {
    public Ev_playerEvent(CustomSpawner plugin){plugin.getServer().getPluginManager().registerEvents(this,plugin);}
    private static final CustomSpawner plugin = CustomSpawner.getInstance();


    @EventHandler
    public static void BlockInteract(PlayerInteractEvent event){
        if(event.getClickedBlock() != null){
            if(event.getClickedBlock().getState().getType().equals(Material.SPAWNER)){
                if(event.getAction().isRightClick()){
                    // GUIをオープンするシステム
                    if((new Location(event.getClickedBlock().getWorld(),event.getClickedBlock().getLocation().getBlockX(),event.getClickedBlock().getLocation().getBlockY()+1,event.getClickedBlock().getLocation().getBlockZ()).getBlock() ).getType().equals(Material.AIR)){
                        return;
                    }

                    CreatureSpawner spawner = (CreatureSpawner) event.getClickedBlock().getState();
                    if(ConfigManager.getEntityList().contains(spawner.getSpawnedType().getKey().getKey())){
                        Block block = new Location(event.getClickedBlock().getWorld(),event.getClickedBlock().getLocation().getBlockX(),event.getClickedBlock().getLocation().getBlockY()+1,event.getClickedBlock().getLocation().getBlockZ()).getBlock();
                        Sys_CustomSpawner.initSpawner(spawner,block.getType(),event.getClickedBlock().getLocation());
                    }

                    if(event.getPlayer().isSneaking()){
                        return;
                    }

                    Collection<Entity> entityList = event.getClickedBlock().getLocation().getNearbyEntities(10,10,10);
                    for (Entity e:entityList){
                        if(e instanceof Player player){
                            if(player.equals(event.getPlayer())){
                                continue;
                            }

                            Component title = player.getOpenInventory().title();
                            if(title.equals(Component.text("CustomSpawner")) || title.equals(Component.text("CustomSpawner Confirm"))){
                                event.setCancelled(true);
                                event.getPlayer().sendMessage(Component.text("他プレイヤーがスポナーGUIを開いているため、スポナーGUIが開けなかった").decoration(TextDecoration.ITALIC,false));
                                return;
                            }

                        }
                    }


                    Inventory inventory = Sys_GuiMaster.getSpawnerGui(event.getClickedBlock().getLocation());
                    event.getPlayer().openInventory(inventory);
                    event.setCancelled(true);
                    // 音の追加
                }
            }
        }
    }
    @EventHandler
    public static void BlockBreak(BlockBreakEvent event){
        //破壊したブロックがスポナーであればキャンセル
        //もしくは1つ下のブロックがスポナーであればキャンセル
        BlockState blockState = event.getBlock().getState();
        if(blockState.getType().equals(Material.SPAWNER)){
            CreatureSpawner spawner = (CreatureSpawner) blockState;

            if((new Location(event.getBlock().getWorld(),event.getBlock().getLocation().getBlockX(),event.getBlock().getLocation().getBlockY()+1,event.getBlock().getLocation().getBlockZ()).getBlock() ).getType().equals(Material.AIR)){
                return;
            }

            if(!spawner.getSpawnedType().equals(EntityType.FALLING_BLOCK)){
                return;
            }

            if(ConfigManager.getEntityList().contains(spawner.getSpawnedType().getKey().getKey())){
                Block block = new Location(event.getBlock().getWorld(),event.getBlock().getLocation().getBlockX(),event.getBlock().getLocation().getBlockY()+1,event.getBlock().getLocation().getBlockZ()).getBlock();
                Sys_CustomSpawner.initSpawner(spawner,block.getType(),spawner.getLocation());
                event.setCancelled(true);
                return;
            }
            if(hasEnchantItem(event.getPlayer(), Sys_Enchantment.spawnerBreak) || event.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
                //近場にいるスポーンインベントリを開けている人の画面を強制敵に閉じる
                Location loc = spawner.getLocation();
                Collection<Entity> entityList = spawner.getLocation().getNearbyEntities(10,10,10);
                for (Entity e:entityList){
                    if(e instanceof Player player){
                        if(player.equals(event.getPlayer())){
                            continue;
                        }

                        Component title = player.getOpenInventory().title();
                        if(title.equals(Component.text("CustomSpawner")) || title.equals(Component.text("CustomSpawner Confirm"))){
                            player.closeInventory();
                            player.sendMessage(Component.text("他プレイヤーがスポナーを壊したため、スポナーGUIを閉じた").decoration(TextDecoration.ITALIC,false));
                        }
                    }
                }
                //ファイルを削除する
                SpawnerConfigManager scm = new SpawnerConfigManager(spawner.getLocation());
                scm.deleteThis();
                ConfigManager.removeSpawners(loc.getWorld().getName()+"_"+loc.getBlockX()+"_"+loc.getBlockY()+"_"+loc.getBlockZ());
                plugin.getLogger().info("(CS)"+"[INFO]:"+event.getPlayer().getName()+"というプレイヤーが"+spawner.getLocation().getWorld().getName()+"の("+spawner.getLocation().getBlockX()+","+spawner.getLocation().getBlockY()+","+spawner.getLocation().getBlockZ()+")にあるスポナーを削除した");
                plugin.getLogger().info("(CS)"+"[INFO]:"+spawner.getLocation().getWorld().getName()+"の("+spawner.getLocation().getBlockX()+","+spawner.getLocation().getBlockY()+","+spawner.getLocation().getBlockZ()+")にあるスポナーのレベルはFrequencyが"+scm.getLevel("Frequency")+",Rangeが"+scm.getLevel("Range")+",Miningが"+scm.getLevel("Mining")+"であった");
                return;
            }

            event.setCancelled(true);
            return;
        }
        Location loc = event.getBlock().getLocation();
        blockState = loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY()-1, loc.getBlockZ()).getState();
        if(blockState.getType().equals(Material.SPAWNER)){
            if(event.getPlayer().getGameMode().equals(GameMode.CREATIVE)){return;}

            CreatureSpawner spawner = (CreatureSpawner) blockState;
            if(!spawner.getSpawnedType().equals(EntityType.FALLING_BLOCK)){
                return;
            }

            Block block = event.getBlock();
            if(ConfigManager.getEntityList().contains(spawner.getSpawnedType().getKey().getKey())){
                //Markerスポナーがあった場合の対処
                Sys_CustomSpawner.initSpawner(spawner,block.getType(),spawner.getLocation());
                event.setCancelled(true);
                //音が鳴るようにする
                return;
            }
            Location loc2 = block.getLocation();
            //丸石じゃない場合の仕様
//            if(!event.getBlock().getType().equals(Material.COBBLESTONE)){
//                new BukkitRunnable(){
//                    @Override
//                    public void run() {
//                        loc2.getBlock().setType(Material.COBBLESTONE);
//                    }
//                }.runTaskLater(plugin,1);
//                return;
//            }

            //N回に一回できるようにする
            int miningTimes = Sys_CustomSpawner.getMiningTimes(spawner);
            Material material = Material.COBBLESTONE;
            if(miningTimes != 0) {
                double random = new Random().nextDouble();
                double rate = 1 / (double) miningTimes;
                material = random < rate ? Sys_CustomSpawner.getMaterial(Sys_CustomSpawner.getBlock(spawner)) : Material.COBBLESTONE;
            }

            Material finalMaterial = material;
            new BukkitRunnable(){
                @Override
                public void run() {
                    loc2.getBlock().setType(finalMaterial);
                }
            }.runTaskLater(plugin,1);


        }
    }

    @EventHandler
    public static void evInventory(InventoryClickEvent event){
        if(event.getView().title().equals(Component.text("CustomSpawner"))){
            event.setCancelled(true);

            if(event.getClickedInventory() == null){
                return;
            }

            if(event.getClickedInventory().getType().equals(InventoryType.PLAYER)){
                return;
            }


            if(event.getSlot() == 12 || event.getSlot() == 14 || event.getSlot() == 16){
                //スポーン頻度のレベルアップ
                ItemStack typePlate = event.getCurrentItem();
                ItemStack mainIcon = event.getInventory().getItem(10);
                Location loc = Sys_GuiItem.getLoc(Objects.requireNonNull(mainIcon));

                if(loc == null){
                    event.getWhoClicked().sendMessage(Component.text("スポナーを取得できなかった。もう一度お試しください"));
                    plugin.getLogger().info("(CS)"+"スポナーの場所を得るときににエラーが出た");
                    event.getWhoClicked().closeInventory();
                    return;
                }
                String type = Sys_GuiItem.getType(Objects.requireNonNull(typePlate));
                if(type == null){
                    event.getWhoClicked().sendMessage(Component.text("レベルアップする項目が取得できなかった。もう一度お試しください"));
                    plugin.getLogger().info("(CS)"+"レベルアップ時のtypeにエラーが出た");
                    event.getWhoClicked().closeInventory();
                    return;
                }
                SpawnerConfigManager scm = new SpawnerConfigManager(loc);
                OreConfigManager ocm = new OreConfigManager(Sys_CustomSpawner.getMaterial(scm.getBlock()));

                if(scm.getLevel(type) >= ocm.getMaxLevel()){
                    //Failleの音を入れる
                    return;
                }

                int cost = ocm.getCost(scm.getLevel(type),type);
                if(!CustomSpawner.getEconomy().has((Player) event.getWhoClicked(),cost)){
                    event.getWhoClicked().sendMessage(Component.text("お金が足りません").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC,false));
                    //Failleの音を入れる
                    return;
                }

                Inventory inventory = Sys_GuiMaster.getConfirm(mainIcon,typePlate);
                event.getWhoClicked().openInventory(inventory);
                //ページが変わる音を入れる
                return;
            }

            if(event.getSlot() == 26){
                event.getWhoClicked().closeInventory();
                return;
            }

            //Faile音を鳴らすようにする
            return;
        }

        if(event.getView().title().equals(Component.text("CustomSpawner Confirm"))){
            event.setCancelled(true);

            if(event.getClickedInventory() == null){
                return;
            }

            if(event.getClickedInventory().getType().equals(InventoryType.PLAYER)){
                return;
            }

            if(event.getSlot() == 10){
                //LevelUP
                ItemStack typePlate = event.getInventory().getItem(25);
                ItemStack mainIcon = event.getInventory().getItem(24);
                Location loc = Sys_GuiItem.getLoc(Objects.requireNonNull(mainIcon));

                if(loc == null){
                    event.getWhoClicked().sendMessage(Component.text("スポナーを取得できなかった。もう一度お試しください"));
                    plugin.getLogger().info("(CS)"+"スポナーの場所を得るときににエラーが出た");
                    event.getWhoClicked().closeInventory();
                    return;
                }
                String type = Sys_GuiItem.getType(Objects.requireNonNull(typePlate));
                if(type == null){
                    event.getWhoClicked().sendMessage(Component.text("レベルアップする項目が取得できなかった。もう一度お試しください"));
                    plugin.getLogger().info("(CS)"+"レベルアップ時のtypeにエラーが出た");
                    event.getWhoClicked().closeInventory();
                    return;
                }


                Sys_CustomSpawner.levelUPSpawner(loc,type,(Player) event.getWhoClicked());
                event.getWhoClicked().sendMessage(Component.text("残高が§a"+CustomSpawner.getEconomy().getBalance((Player) event.getWhoClicked())+"§fになった").decoration(TextDecoration.ITALIC,false));

                Inventory inventory = Sys_GuiMaster.getSpawnerGui(loc);
                event.getWhoClicked().openInventory(inventory);
            }

            if(event.getSlot() == 16){
                event.getWhoClicked().closeInventory();
                return;
            }

            if(event.getSlot() == 26){
                ItemStack mainIcon = event.getInventory().getItem(24);
                Location loc = Sys_GuiItem.getLoc(Objects.requireNonNull(mainIcon));
                if(loc == null){
                    event.getWhoClicked().sendMessage(Component.text("ページが戻る時にエラーが出たため戻ることができなかった"));
                    plugin.getLogger().info("(CS)"+"Confirmインベントリから戻るときにエラーが出た");
                    event.getWhoClicked().closeInventory();
                    return;
                }
                Inventory inventory = Sys_GuiMaster.getSpawnerGui(loc);
                event.getWhoClicked().openInventory(inventory);
                return;
            }

        }
    }

    public static boolean hasEnchantItem(Player player,Enchantment enchantment){
        if(player.getInventory().getItemInMainHand().hasItemMeta()){
            return player.getInventory().getItemInMainHand().getItemMeta().getEnchants().containsKey(enchantment);
        }
        return false;
    }

//    @EventHandler
//    public static void anvilEvent(PrepareAnvilEvent event){
//        ItemStack first = event.getInventory().getFirstItem();
//        ItemStack second = event.getInventory().getSecondItem();
//        if(first == null) return;
//
//        if(second == null){
//            if(event.getResult() == null) return;
//            Sys_Enchantment.removeSpawnerBreak(event.getResult());
//            return;
//        }
//        if(!second.getType().equals(Material.ENCHANTED_BOOK)){
//            if(event.getResult() == null) return;
//            Sys_Enchantment.removeSpawnerBreak(event.getResult());
//            return;
//        }
//        if(((EnchantmentStorageMeta) second.getItemMeta()).hasStoredEnchant(Sys_Enchantment.spawnerBreak)){
//            if(event.getResult() != null){
//                ItemStack result = event.getResult();
//                Sys_Enchantment.addSpawnerBreak(result);
//                return;
//            }
//
//            if(event.getResult() == null){
//                ItemStack result = first.clone();
//                Sys_Enchantment.addSpawnerBreak(result);
//                event.setResult(result);
//                return;
//            }
//        }else {
//            if(event.getResult() != null){
//                Sys_Enchantment.removeSpawnerBreak(event.getResult());
//                return;
//            }
//        }
////
////
////
////
////
////        ItemMeta itemMeta = itemStack.getItemMeta();
////
////        if(itemMeta instanceof EnchantmentStorageMeta meta){
////            if(meta.hasStoredEnchant(Sys_Enchantment.spawnerBreak)){
////                Component lore1 = Component.translatable(Sys_Enchantment.spawnerBreak.translationKey()).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC,false);
////                List<Component> lore = meta.lore();
////                if(lore == null) {
////                    lore = new ArrayList<>();
////                }
////
////                if(lore.contains(lore1)){
////                    return;
////                }
////
////                lore.add(lore1);
////
////                meta.lore(lore);
////                itemStack.setItemMeta(meta);
////            }
////        }else {
////            if(itemMeta.hasEnchant(Sys_Enchantment.spawnerBreak)){
////                Component lore1 = Component.translatable(Sys_Enchantment.spawnerBreak.translationKey()).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC,false);
////                List<Component> lore = itemMeta.lore();
////                if(lore == null) {
////                    lore = new ArrayList<>();
////                }
////
////                if(lore.contains(lore1)){
////                    return;
////                }
////
////                lore.add(lore1);
////
////                itemMeta.lore(lore);
////                itemStack.setItemMeta(itemMeta);
////            }
////        }
//    }
//    @EventHandler
//    public static void GrindEvent(PrepareGrindstoneEvent event){
//        ItemStack itemStack = event.getResult();
//        if(itemStack == null) return;
//        ItemMeta itemMeta = itemStack.getItemMeta();
//        if(itemMeta.hasLore()){
//            List<Component> lore = itemMeta.lore();
//            if(lore == null){
//                return;
//            }
//
//            lore.remove(Component.translatable(Sys_Enchantment.spawnerBreak.translationKey()).color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC,false));
//            itemMeta.lore(lore);
//            itemStack.setItemMeta(itemMeta);
//        }
//    }


}
