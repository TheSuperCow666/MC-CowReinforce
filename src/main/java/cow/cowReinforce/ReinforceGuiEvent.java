package cow.cowReinforce;

import com.fileTool.*;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
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
        String item_reinforceType = Tool.getItemReinforceType(p);
        double gl = new Random().nextDouble()*100;
        if(e.getSlot() == Gui.getQiangbao_slot()){
            //强化保护石槽位
            if(e.getClickedInventory().getItem(Gui.getQiangbao_slot()) != null &&e.getClickedInventory().getItem(Gui.getQiangbao_slot()).equals(Gui.getContent().get(Gui.getQiangbao_slot()))){
                //里面没有放保护石
                e.getClickedInventory().setItem(e.getSlot(),new ItemStack(Material.AIR));
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        if(e.getClickedInventory().getItem(e.getSlot()) == null){
                            e.getClickedInventory().setItem(e.getSlot(),Gui.getContent().get(e.getSlot()));
                        }
                    }
                }.runTaskLaterAsynchronously(CowReinforce.getinstance(),10);
            }else{
                //放置了
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        if(e.getClickedInventory().getItem(e.getSlot()) == null){
                            e.getClickedInventory().setItem(e.getSlot(),Gui.getContent().get(e.getSlot()));
                        }
                    }
                }.runTaskLaterAsynchronously(CowReinforce.getinstance(),10);
            }
            return;
        }else if(e.getSlot() == Gui.getXingyunfu_slot()){
            if(e.getClickedInventory().getItem(Gui.getXingyunfu_slot()) != null && e.getClickedInventory().getItem(Gui.getXingyunfu_slot()).hasItemMeta() && e.getClickedInventory().getItem(Gui.getXingyunfu_slot()).equals(Gui.getContent().get(Gui.getXingyunfu_slot()))){
                //里面没放东西
                e.getClickedInventory().setItem(e.getSlot(),new ItemStack(Material.AIR));
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        if(e.getClickedInventory().getItem(e.getSlot()) == null){
                            e.getClickedInventory().setItem(e.getSlot(),Gui.getContent().get(e.getSlot()));
                        }
                    }
                }.runTaskLaterAsynchronously(CowReinforce.getinstance(),10);
            }else{
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        if(e.getClickedInventory().getItem(e.getSlot()) == null){
                            e.getClickedInventory().setItem(e.getSlot(),Gui.getContent().get(e.getSlot()));
                        }
                    }
                }.runTaskLaterAsynchronously(CowReinforce.getinstance(),10);
            }
            new BukkitRunnable(){
                @Override
                public void run() {
                    if(e.getClickedInventory().getItem(Gui.getXingyunfu_slot()) != null && !e.getClickedInventory().getItem(Gui.getXingyunfu_slot()).equals(Gui.getContent().get(Gui.getXingyunfu_slot()))) {
                        String proptype =  ReinforceProps.getType(e.getClickedInventory().getItem(Gui.getXingyunfu_slot()),item_reinforceType);
                        if(!(proptype.equals("无") || ReinforceProps.getType().get(proptype) != 1)){

                            double more_upchance = ReinforceProps.getUpchance().get(proptype);
                            for(int x : Gui.getQianghua().keySet()){
                                ItemStack curItem = e.getClickedInventory().getItem(x);
                                ItemMeta curim = curItem.getItemMeta();
                                List<String> lores = curItem.getItemMeta().getLore();
                                for(int j = 0;j<lores.size();j++){
                                    if(lores.get(j).contains(Reinforce.getLuckstone_check().get(item_reinforceType))){
                                        lores.set(j,lores.get(j) + " +" +more_upchance +"%");
                                        break;
                                    }
                                }
                                curim.setLore(lores);
                                curItem.setItemMeta(curim);
                                e.getClickedInventory().setItem(x,curItem);
                            }
                        }
                    }else{
                        for(int x : Gui.getQianghua().keySet()){
                            ItemStack curItem = e.getClickedInventory().getItem(x);
                            ItemMeta curim = curItem.getItemMeta();
                            List<String> lores = curItem.getItemMeta().getLore();
                            for(int j = 0;j<lores.size();j++){
                                if(lores.get(j).contains(Reinforce.getLuckstone_check().get(item_reinforceType))){
                                    lores.set(j,lores.get(j).replaceAll("\\s*\\+\\s*[\\d.]+%$", ""));
                                    break;
                                }
                            }
                            curim.setLore(lores);
                            curItem.setItemMeta(curim);
                            e.getClickedInventory().setItem(x,curItem);
                        }
                    }
                }
            }.runTaskLaterAsynchronously(CowReinforce.getinstance(),6);
            return;
        }
        for(int slot : Gui.getContent().keySet()){
            if(e.getSlot() == slot && Gui.getContent().get(slot).getType() == Material.AIR){
               return;
            }
        }
        e.setCancelled(true);
        int temp_level = Tool.getReinforceLevel(p);
        for(int slot : Gui.getQianghua().keySet()){
            if(slot == e.getSlot()){
                if(CowReinforce.econ != null && CowReinforce.econ.getBalance(p) < Reinforce.getNeedMoney().get(item_reinforceType).get(temp_level+1)){
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
                for(Map.Entry<ItemStack,Integer> entry : Reinforce.getNeeditem().get(item_reinforceType).entrySet()){

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
                boolean has_protectiveStone = false;
                if(e.getClickedInventory().getItem(Gui.getQiangbao_slot()) != null && !e.getClickedInventory().getItem(Gui.getQiangbao_slot()).equals(Gui.getContent().get(Gui.getQiangbao_slot()))){
                    //检测强化保护石位置不为空
                    String proptype =  ReinforceProps.getType(e.getClickedInventory().getItem(Gui.getQiangbao_slot()),item_reinforceType);
                    if(proptype.equals("无") || ReinforceProps.getType().get(proptype) != 0){
                        e.getClickedInventory().setItem(e.getSlot(),Gui.getSymbol().get('N'));
                        p.playSound(p.getLocation(), Sound.valueOf(CowReinforce.getinstance().getConfig().getString("Settings.Material.Sound.failure")),1,1);
                        new BukkitRunnable(){
                            public void run(){
                                e.getClickedInventory().setItem(e.getSlot(),old);
                                cancel();
                            }
                        }.runTaskLater(CowReinforce.getinstance(),CowReinforce.getinstance().getConfig().getLong("Settings.Material.time")*20);
                        return;
                    }else{
                        has_protectiveStone = true;
                    }
                }
                double more_upchance = 0;
                if(e.getClickedInventory().getItem(Gui.getXingyunfu_slot()) != null && !e.getClickedInventory().getItem(Gui.getXingyunfu_slot()).equals(Gui.getContent().get(Gui.getXingyunfu_slot()))){
                    String proptype =  ReinforceProps.getType(e.getClickedInventory().getItem(Gui.getXingyunfu_slot()),item_reinforceType);
                    if(proptype.equals("无") || ReinforceProps.getType().get(proptype) != 1){
                        e.getClickedInventory().setItem(e.getSlot(),Gui.getSymbol().get('L'));
                        p.playSound(p.getLocation(), Sound.valueOf(CowReinforce.getinstance().getConfig().getString("Settings.Material.Sound.failure")),1,1);
                        new BukkitRunnable(){
                            public void run(){
                                e.getClickedInventory().setItem(e.getSlot(),old);
                                cancel();
                            }
                        }.runTaskLater(CowReinforce.getinstance(),CowReinforce.getinstance().getConfig().getLong("Settings.Material.time")*20);
                        return;
                    }else{
                        more_upchance = ReinforceProps.getUpchance().get(proptype);
                    }
                }
                for(Map.Entry<ItemStack,Integer> entry : Reinforce.getNeeditem().get(Tool.getItemReinforceType(p)).entrySet()){
                    Tool.dItem(entry.getKey(),p,entry.getValue());
                }
                if(CowReinforce.econ != null)CowReinforce.econ.withdrawPlayer(p,Reinforce.getNeedMoney().get(item_reinforceType).get(temp_level+1));
                double upchance = Tool.getchance(temp_level + 1, Reinforce.getUpchance().get(item_reinforceType)) * 100;
                double downchance = Tool.getchance(temp_level + 1, Reinforce.getDownchance().get(item_reinforceType)) * 100;

                if(upchance >0){
                    int amount = e.getClickedInventory().getItem(Gui.getXingyunfu_slot()).getAmount()-1;
                    e.getClickedInventory().getItem(Gui.getXingyunfu_slot()).setAmount(amount);
                    if(amount <=0){
                        e.getClickedInventory().setItem(Gui.getXingyunfu_slot(),Gui.getContent().get(Gui.getXingyunfu_slot()));
                    }
                }
                if (gl > upchance+more_upchance) {
                    p.playSound(p.getLocation(), Sound.valueOf(CowReinforce.getinstance().getConfig().getString("Settings.Material.Sound.failure")),1,1);
                    double dc = new Random().nextDouble()*100;
                    if(dc < downchance){
                        //扣级
                        if(has_protectiveStone){
                           //检测有强化保护石
                            e.getClickedInventory().setItem(e.getSlot(),Gui.getSymbol().get('W'));
                            int amount = e.getClickedInventory().getItem(Gui.getQiangbao_slot()).getAmount()-1;
                            e.getClickedInventory().getItem(Gui.getQiangbao_slot()).setAmount(amount);
                            if(amount <=0){
                                e.getClickedInventory().setItem(Gui.getQiangbao_slot(),Gui.getContent().get(Gui.getQiangbao_slot()));
                            }
                            new BukkitRunnable(){
                                public void run(){
                                    e.getClickedInventory().setItem(e.getSlot(),old);
                                    cancel();
                                }
                            }.runTaskLater(CowReinforce.getinstance(),CowReinforce.getinstance().getConfig().getLong("Settings.Material.time")*20);
                        }else{
                            Tool.getLastLevelItem(p,p.getInventory().getItemInMainHand(),item_reinforceType);
                            p.closeInventory();
                            ReinforceGui.failureopen(p);
                        }
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
                p.playSound(p.getLocation(), Sound.valueOf(CowReinforce.getinstance().getConfig().getString("Settings.Material.Sound.success")),1,1);
                Tool.getNextLevelItem(p,p.getInventory().getItemInMainHand(),item_reinforceType);
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
            if(e.getInventory().getItem(Gui.getQiangbao_slot())!= null && !e.getInventory().getItem(Gui.getQiangbao_slot()).equals(Gui.getContent().get(Gui.getQiangbao_slot()))){
                p.getInventory().addItem(e.getInventory().getItem(Gui.getQiangbao_slot()));
            }
            if(e.getInventory().getItem(Gui.getXingyunfu_slot()) !=null&& !e.getInventory().getItem(Gui.getXingyunfu_slot()).equals(Gui.getContent().get(Gui.getXingyunfu_slot()))){
                p.getInventory().addItem(e.getInventory().getItem(Gui.getXingyunfu_slot()));
            }
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
                if(e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().equals(e.getWhoClicked().getInventory().getItemInMainHand())){
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
