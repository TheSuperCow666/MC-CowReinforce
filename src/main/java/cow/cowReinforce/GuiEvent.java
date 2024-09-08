package cow.cowReinforce;

import com.fileTool.Gui;
import com.fileTool.Reinforce;
import com.fileTool.SpecialItem;
import org.bukkit.Sound;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.Random;

public class GuiEvent implements Listener {
    @EventHandler
    public static void onOpenGuiEvent(InventoryClickEvent e){
        if(e.getClickedInventory()== null){
            return;
        }
        Player p = (Player)e.getWhoClicked();

        if(e.getInventory().equals(ReinforceGui.inv.get(p))){
            e.setCancelled(true);

            if(e.getClickedInventory().equals(ReinforceGui.inv.get(p))){
                ItemStack old = e.getClickedInventory().getItem(e.getSlot());
                double gl = new Random().nextDouble()*100;
                for(int slot : Gui.getQianghua().keySet()){
                    if(slot == e.getSlot()){
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
                        double upchance = Tool.getchance(Tool.getReinforceLevel(p) + 1, Reinforce.getUpchance().get(Tool.getItemReinforceType(p))) * 100;
                        double downchance = Tool.getchance(Tool.getReinforceLevel(p) + 1, Reinforce.getDownchance().get(Tool.getItemReinforceType(p))) * 100;
                        if (gl > upchance) {
                            p.playSound(p.getLocation(), Sound.valueOf(CowReinforce.getinstance().getConfig().getString("Settings.Material.Sound.failure")),1,1);
                            double dc = new Random().nextDouble()*100;
                            if(dc < downchance){
                                Tool.getLastLevelItem(p,p.getInventory().getItemInMainHand(),Tool.getItemReinforceType(p));
                                e.getClickedInventory().setItem(e.getSlot(),Gui.getSymbol().get('o'));
                                new BukkitRunnable(){
                                    public void run(){
                                        e.getClickedInventory().setItem(e.getSlot(),old);
                                        cancel();
                                    }
                                }.runTaskLater(CowReinforce.getinstance(),CowReinforce.getinstance().getConfig().getLong("Settings.Material.time")*20);
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
                        ReinforceGui.loadReinforceGui(p);
                        p.playSound(p.getLocation(), Sound.valueOf(CowReinforce.getinstance().getConfig().getString("Settings.Material.Sound.success")),1,1);
                        Tool.getNextLevelItem(p,p.getInventory().getItemInMainHand(),Tool.getItemReinforceType(p));
                        p.closeInventory();
                        ReinforceGui.open(p);
                    }
                }

            }
        }
    }

    @EventHandler
    public static void onDragItem(InventoryClickEvent e){
        assert e.getClickedInventory() != null;
        if(e.getAction() != InventoryAction.SWAP_WITH_CURSOR){
            return;
        }
        if(e.getClickedInventory() == e.getWhoClicked().getInventory()){
            String group =  Tool.getReinforceGroup((Player)e.getWhoClicked(),e.getCursor(),e.getSlot());
            if(!group.equals("未找到")){
                if(!Tool.checkLevelItem((Player) e.getWhoClicked(), e.getClickedInventory().getItem(e.getSlot()), SpecialItem.getLevel().get(Tool.getSpecialGroup((Player) e.getWhoClicked(), e.getCursor())))){
                    return;
                }
                Tool.setLevelItem((Player) e.getWhoClicked(),e.getClickedInventory().getItem(e.getSlot()),
                Tool.getItemReinforceType((Player) e.getWhoClicked(),e.getSlot()), SpecialItem.getLevel().get(Tool.getSpecialGroup((Player)e.getWhoClicked(),e.getCursor())));
                ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(),Sound.valueOf(CowReinforce.getinstance().getConfig().getString("Settings.UsedSuccessfully.sound")),1,1);
                Tool.sendListMessage((Player)e.getWhoClicked(),CowReinforce.getinstance().getConfig().getStringList("Settings.UsedSuccessfully.message"));
                e.getCursor().setAmount(e.getCursor().getAmount()-1);
                e.setCancelled(true);
            }
        }
    }

}
