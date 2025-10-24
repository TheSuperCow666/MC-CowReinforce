package cow.cowReinforce;

import com.fileTool.Gui2;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class InheritanceGuiEvent implements Listener {
    @EventHandler
    public static void onOpenGuiEvent(InventoryClickEvent e){
        if(e.getClickedInventory()== null){
            return;
        }
        if(!(e.getClickedInventory().getHolder() instanceof InheritanceGui)){
            return;
        }
        Player p = (Player)e.getWhoClicked();
        ItemStack old = e.getClickedInventory().getItem(e.getSlot());
        for(int slot : Gui2.getContent().keySet()){
            if(e.getSlot() == slot && Gui2.getContent().get(slot).getType() == Material.AIR){
               return;
            }
        }
        e.setCancelled(true);
        for(int slot : Gui2.getQianghua().keySet()){
            if(slot == e.getSlot()){
                ItemStack inherianceStone = e.getClickedInventory().getItem(Gui2.getCheckslot());
                int ToLevel = Tool.getInheritanceLevel(p,inherianceStone);
                if(inherianceStone != null && ToLevel >0){
                    String limitgroup = Tool.getInheritanceGroup(p,inherianceStone,p.getInventory().getItemInMainHand());
                    if(limitgroup.equals("未找到")){
                        e.getClickedInventory().setItem(e.getSlot(), Gui2.getSymbol().get('m'));
                        p.playSound(p.getLocation(), Sound.valueOf(CowReinforce.getinstance().getConfig().getString("Settings.Material.Sound.failure")),1,1);
                        new BukkitRunnable(){
                            public void run(){
                                e.getClickedInventory().setItem(e.getSlot(),old);
                                cancel();
                            }
                        }.runTaskLater(CowReinforce.getinstance(),CowReinforce.getinstance().getConfig().getLong("Settings.Material.time")*20);
                        return;
                    }else{
                        //成功匹配
                        if(ToLevel <= Tool.getReinforceLevel(p.getInventory().getItemInMainHand())){
                            e.getClickedInventory().setItem(e.getSlot(), Gui2.getSymbol().get('r'));
                            p.playSound(p.getLocation(), Sound.valueOf(CowReinforce.getinstance().getConfig().getString("Settings.Material.Sound.failure")),1,1);
                            new BukkitRunnable(){
                                public void run(){
                                    e.getClickedInventory().setItem(e.getSlot(),old);
                                    cancel();
                                }
                            }.runTaskLater(CowReinforce.getinstance(),CowReinforce.getinstance().getConfig().getLong("Settings.Material.time")*20);
                            return;
                        }
                        inherianceStone.setAmount(inherianceStone.getAmount()-1);
                        Tool.setLevelItem(p,p.getInventory().getItemInMainHand(),Tool.getItemReinforceType(p,p.getInventory().getItemInMainHand()),ToLevel);
                        p.playSound(p.getLocation(), Sound.valueOf(CowReinforce.getinstance().getConfig().getString("Settings.Material.Sound.success")),1,1);
                        p.closeInventory();
                    }
                }else{
                    //不存在继承石头
                    e.getClickedInventory().setItem(e.getSlot(), Gui2.getSymbol().get('f'));
                    p.playSound(p.getLocation(), Sound.valueOf(CowReinforce.getinstance().getConfig().getString("Settings.Material.Sound.failure")),1,1);
                    new BukkitRunnable(){
                        public void run(){
                            e.getClickedInventory().setItem(e.getSlot(),old);
                            cancel();
                        }
                    }.runTaskLater(CowReinforce.getinstance(),CowReinforce.getinstance().getConfig().getLong("Settings.Material.time")*20);
                }

            }
        }
    }
    @EventHandler
    public static void onCloseInv(InventoryCloseEvent e){
        if(e.getInventory().getHolder() instanceof InheritanceGui){
            Player p = (Player)e.getPlayer();
            InheritanceGui.loadInherianceGui(p);
            boolean flag = false;
            for(int i =0;i<Gui2.getSize();i++){
                if( Gui2.getContent().get(i).getType() == Material.AIR){
                    if(e.getInventory().getItem(i) != null){
                        flag = true;
                        p.getInventory().addItem(e.getInventory().getItem(i));
                    }
                }
            }
            if(flag){
                p.playSound(p.getLocation(), Sound.valueOf(CowReinforce.getinstance().getConfig().getString("Settings.HasItemInGui.sound")),1,1);
                Tool.sendListMessage(p, CowReinforce.getinstance().getConfig().getStringList("Settings.HasItemInGui.message"));
            }

        }
    }
    @EventHandler
    public static void onDragItem(InventoryClickEvent e){
        assert e.getClickedInventory() != null;
        if(e.getClickedInventory() == e.getWhoClicked().getInventory()){
            if(e.getWhoClicked().getOpenInventory().getTopInventory().getHolder() instanceof InheritanceGui){
                if(e.getCurrentItem() != null){
                    if(e.getCurrentItem().equals(e.getWhoClicked().getInventory().getItemInMainHand())){
                        e.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }

}
