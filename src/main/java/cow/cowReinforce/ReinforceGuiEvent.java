package cow.cowReinforce;

import com.fileTool.*;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.Random;

public class ReinforceGuiEvent implements Listener {
    @EventHandler
    public static void onOpenGuiEvent(InventoryClickEvent e){
        if(e.getClickedInventory()== null){
            return;
        }
        if(!(e.getClickedInventory().getHolder() instanceof ReinforceGui)){
            return;
        }
        Player p = (Player)e.getWhoClicked();
        ItemStack old = e.getClickedInventory().getItem(e.getSlot());
        double gl = new Random().nextDouble()*100;
        for(int slot : Gui.getContent().keySet()){
            if(e.getSlot() == slot && Gui.getContent().get(slot).getType() == Material.AIR){
               return;
            }
        }
        e.setCancelled(true);
        for(int slot : Gui.getQianghua().keySet()){
            if(slot == e.getSlot()){
                if(CowReinforce.econ != null && CowReinforce.econ.getBalance(p) < Reinforce.getNeedMoney().get(Tool.getItemReinforceType(p))){
                    e.getClickedInventory().setItem(e.getSlot(),Gui.getSymbol().get('m'));
                    p.playSound(p.getLocation(), Sound.valueOf(CowReinforce.getinstance().getConfig().getString("Settings.Material.Sound.failure")),1,1);
                    new BukkitRunnable(){
                        public void run(){
                            e.getClickedInventory().setItem(e.getSlot(),old);
                            cancel();
                        }
                    }.runTaskLater(CowReinforce.getinstance(),CowReinforce.getinstance().getConfig().getLong("Settings.Material.time")*20);
                    return;
                }
                for(Map.Entry<ItemStack,Integer> entry : Reinforce.getNeeditem().get(Tool.getItemReinforceType(p)).entrySet()){

                    if(Tool.checkItem(entry.getKey(),p) < entry.getValue()){
                        e.getClickedInventory().setItem(e.getSlot(),Gui.getSymbol().get('f'));
                        p.playSound(p.getLocation(), Sound.valueOf(CowReinforce.getinstance().getConfig().getString("Settings.Material.Sound.failure")),1,1);
                        new BukkitRunnable(){
                            public void run(){
                                e.getClickedInventory().setItem(e.getSlot(),old);
                                cancel();
                            }
                        }.runTaskLater(CowReinforce.getinstance(),CowReinforce.getinstance().getConfig().getLong("Settings.Material.time")*20);
                        return;
                    }
                }
                for(Map.Entry<ItemStack,Integer> entry : Reinforce.getNeeditem().get(Tool.getItemReinforceType(p)).entrySet()){
                    Tool.dItem(entry.getKey(),p,entry.getValue());
                }
                if(CowReinforce.econ != null)CowReinforce.econ.withdrawPlayer(p,Reinforce.getNeedMoney().get(Tool.getItemReinforceType(p)));
                double upchance = Tool.getchance(Tool.getReinforceLevel(p) + 1, Reinforce.getUpchance().get(Tool.getItemReinforceType(p))) * 100;
                double downchance = Tool.getchance(Tool.getReinforceLevel(p) + 1, Reinforce.getDownchance().get(Tool.getItemReinforceType(p))) * 100;


                if (gl > upchance) {
                    p.playSound(p.getLocation(), Sound.valueOf(CowReinforce.getinstance().getConfig().getString("Settings.Material.Sound.failure")),1,1);
                    double dc = new Random().nextDouble()*100;
                    if(dc < downchance){
                        Tool.getLastLevelItem(p,p.getInventory().getItemInMainHand(),Tool.getItemReinforceType(p));
//                        e.getClickedInventory().setItem(e.getSlot(),Gui.getSymbol().get('o'));
//                        new BukkitRunnable(){
//                            public void run(){
//                                e.getClickedInventory().setItem(e.getSlot(),old);
//                                cancel();
//                            }
//                        }.runTaskLater(CowReinforce.getinstance(),CowReinforce.getinstance().getConfig().getLong("Settings.Material.time")*20);
                        p.closeInventory();
                        ReinforceGui.failureopen(p);
                        return;
                    }
                    e.getClickedInventory().setItem(e.getSlot(),Gui.getSymbol().get('q'));
                    new BukkitRunnable(){
                        public void run(){
                            e.getClickedInventory().setItem(e.getSlot(),old);
                            cancel();
                        }
                    }.runTaskLater(CowReinforce.getinstance(),CowReinforce.getinstance().getConfig().getLong("Settings.Material.time")*20);

                    return;
                }
         //       ReinforceGui.loadReinforceGui(p);
                p.playSound(p.getLocation(), Sound.valueOf(CowReinforce.getinstance().getConfig().getString("Settings.Material.Sound.success")),1,1);
                Tool.getNextLevelItem(p,p.getInventory().getItemInMainHand(),Tool.getItemReinforceType(p));
                p.closeInventory();
                ReinforceGui.open(p);
            }
        }
    }
    @EventHandler
    public static void onCloseInv(InventoryCloseEvent e){
        if(e.getInventory().getHolder() instanceof ReinforceGui){
            Player p = (Player)e.getPlayer();
            ReinforceGui.loadReinforceGui(p);
            boolean flag = false;
            for(int i =0;i<Gui.getSize();i++){
                if( Gui.getContent().get(i).getType() == Material.AIR){
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
            if(e.getWhoClicked().getOpenInventory().getTopInventory().getHolder() instanceof  ReinforceGui){
                if(e.getCurrentItem().equals(e.getWhoClicked().getInventory().getItemInMainHand())){
                    e.setCancelled(true);
                    return;
                }
            }
            if(e.getAction() != InventoryAction.SWAP_WITH_CURSOR){
                return;
            }
            //SpecialItem
            String group =  Tool.getReinforceGroup((Player)e.getWhoClicked(),e.getCursor(),e.getSlot());
            if(!group.equals("未找到") && !Tool.getSpecialGroup((Player)e.getWhoClicked(),e.getCursor()).equals("未找到")){
                if(!Tool.checkLevelItem((Player) e.getWhoClicked(), e.getClickedInventory().getItem(e.getSlot()), SpecialItem.getLevel().get(Tool.getSpecialGroup((Player) e.getWhoClicked(), e.getCursor())))){
                    return;
                }
                Tool.setLevelItem((Player) e.getWhoClicked(),e.getClickedInventory().getItem(e.getSlot()),
                Tool.getItemReinforceType((Player) e.getWhoClicked(),e.getSlot()), SpecialItem.getLevel().get(Tool.getSpecialGroup((Player)e.getWhoClicked(),e.getCursor())));
                ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(),Sound.valueOf(CowReinforce.getinstance().getConfig().getString("Settings.UsedSuccessfully.sound")),1,1);
                Tool.sendListMessage((Player)e.getWhoClicked(),CowReinforce.getinstance().getConfig().getStringList("Settings.UsedSuccessfully.message"));
                e.getCursor().setAmount(e.getCursor().getAmount()-1);
                e.setCancelled(true);
                return;
            }
            //Inheritance
            String inheritance = Tool.getInheritanceGroup((Player)e.getWhoClicked(),e.getCursor());
            String Ingroup =  Tool.getInheritanceGroup((Player)e.getWhoClicked(),e.getCursor(),e.getSlot());
            if(!Ingroup.equals("未找到") && !inheritance.equals("未找到")) {
                if(e.getCursor().getAmount() >1){
                    ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(),Sound.valueOf(CowReinforce.getinstance().getConfig().getString("Settings.InheritanceWrong.sound")),1,1);
                    Tool.sendListMessage((Player)e.getWhoClicked(),CowReinforce.getinstance().getConfig().getStringList("Settings.InheritanceWrong.message"));
                    return;
                }
                if(Tool.getInheritanceLevel( ((Player) e.getWhoClicked()),e.getCursor()) > 0){
                    ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(),Sound.valueOf(CowReinforce.getinstance().getConfig().getString("Settings.IsInherited.sound")),1,1);
                    Tool.sendListMessage((Player)e.getWhoClicked(),CowReinforce.getinstance().getConfig().getStringList("Settings.IsInherited.message"));
                    return;
                }
                int inLevel =Tool.getReinforceLevel(e.getClickedInventory().getItem(e.getSlot())) * Inheritance.getProp().get(inheritance) /100;
                if(inLevel <=0 ){
                    ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(),Sound.valueOf(CowReinforce.getinstance().getConfig().getString("Settings.InheritanceEquipWrong.sound")),1,1);
                    Tool.sendListMessage((Player)e.getWhoClicked(),CowReinforce.getinstance().getConfig().getStringList("Settings.InheritanceEquipWrong.message"));
                    return;
                }
                Tool.setLevelItem((Player) e.getWhoClicked(),e.getClickedInventory().getItem(e.getSlot()),
                        Tool.getItemReinforceType((Player) e.getWhoClicked(),e.getSlot()), 0);
                ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(),Sound.valueOf(CowReinforce.getinstance().getConfig().getString("Settings.InheritedSuccessfully.sound")),1,1);
                Tool.sendListMessage((Player)e.getWhoClicked(),CowReinforce.getinstance().getConfig().getStringList("Settings.InheritedSuccessfully.message"));
                Tool.addInheritanceLore(e,(Player) e.getWhoClicked(),e.getCursor(),inLevel,inheritance);
            }
        }
    }

}
