package cow.cowReinforce;

import com.fileTool.Gui;
import com.fileTool.Gui2;
import com.fileTool.Reinforce;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ReinforceGui implements InventoryHolder {

    public static HashMap<Player,Inventory> inv;
    public static Inventory inventory;
    static{
        inv = new HashMap<>();
    }
    @Override
    public Inventory getInventory() {
        return null; // 无需实现，仅用于标识
    }
    public static void loadReinforceGui(Player p){
        Inventory inv2 =  Bukkit.createInventory(new ReinforceGui(), Gui.getSize(),Gui.getTitle());
        Gui.getContent().forEach(inv2::setItem);
        inv.put(p,inv2);
        inventory = inv2;
    }
    public static void failureopen(Player p){
        if(!inv.containsKey(p)){
             loadReinforceGui(p);
        }
        if(!Tool.getItemReinforceType(p).equals("未找到")) {
            try{
                Gui.getQianghua().forEach((key, value) -> {
                    ItemStack i = value.clone();
                    List<String> lore = Objects.requireNonNull(i.getItemMeta()).getLore();
                    ItemMeta im = i.getItemMeta();
                    assert lore != null;
                    int level = Tool.getReinforceLevel(p);
                    if(CowReinforce.econ != null){
                        lore.replaceAll(a -> a
                                .replace("%has_money%",""+CowReinforce.econ.getBalance(p))
                                .replace("%need_money%",""+Reinforce.getNeedMoney().get(Tool.getItemReinforceType(p)).get(level+1)));
                    }
                    lore.replaceAll(a -> a
                            .replace("%attribute%", "属性")
                            .replace("%name%", p.getInventory().getItemInMainHand().getItemMeta().getDisplayName())
                            .replace("%target_level%", level + "")
                            .replace("%target_uplevel%", level + 1 + "")
                            .replace("%need_material%", Tool.getMaterialDescription(p, Tool.getItemReinforceType(p)))
                            .replace("%maxlevel%", Reinforce.getMaxlevel().get(Tool.getItemReinforceType(p)) + "")
                            .replace("%upchance%", Tool.getchance(level + 1, Reinforce.getUpchance().get(Tool.getItemReinforceType(p))) * 100 + "%")
                            .replace("%downchance%", Tool.getchance(level + 1, Reinforce.getDownchance().get(Tool.getItemReinforceType(p))) * 100 + "%"));
                    im.setLore(lore);
                    i.setItemMeta(im);
                    inv.get(p).setItem(key, i);
                    inventory.setItem(key,Gui.getSymbol().get('o'));
                    new BukkitRunnable(){
                        public void run(){
                            inventory.setItem(key,i);
                            cancel();
                        }
                    }.runTaskLater(CowReinforce.getinstance(),CowReinforce.getinstance().getConfig().getLong("Settings.Material.time")*20);
                });
            }catch(Exception e){
                Tool.sendListMessage(p,CowReinforce.getinstance().getConfig().getStringList("Settings.GetMaxLevelMessage"));
            }
        }else {
            Tool.sendListMessage(p, CowReinforce.getinstance().getConfig().getStringList("Settings.NoItemInHandMessage"));

        }

        if(!Tool.getItemReinforceType(p).equals("未找到") && Tool.getReinforceLevel(p) < Reinforce.getMaxlevel().get(Tool.getItemReinforceType(p))){
            p.openInventory(inv.get(p));
        }
    }
    public static void open(Player p){
        if(!inv.containsKey(p)){
            loadReinforceGui(p);
        }
        if(!Tool.getItemReinforceType(p).equals("未找到")) {
            try{
                Gui.getQianghua().forEach((key, value) -> {
                    ItemStack i = value.clone();
                    List<String> lore = Objects.requireNonNull(i.getItemMeta()).getLore();
                    ItemMeta im = i.getItemMeta();
                    assert lore != null;
                    int temp_level = Tool.getReinforceLevel(p);
                    if(CowReinforce.econ != null){
                        lore.replaceAll(a -> a
                                .replace("%has_money%",""+CowReinforce.econ.getBalance(p))
                                .replace("%need_money%",""+Reinforce.getNeedMoney().get(Tool.getItemReinforceType(p)).get(temp_level+1)));
                    }
                    lore.replaceAll(a -> a
                            .replace("%attribute%", "属性")
                            .replace("%name%", p.getInventory().getItemInMainHand().getItemMeta().getDisplayName())
                            .replace("%target_level%", temp_level + "")
                            .replace("%target_uplevel%", temp_level + 1 + "")
                            .replace("%need_material%", Tool.getMaterialDescription(p, Tool.getItemReinforceType(p)))
                            .replace("%maxlevel%", Reinforce.getMaxlevel().get(Tool.getItemReinforceType(p)) + "")
                            .replace("%upchance%", Tool.getchance(temp_level + 1, Reinforce.getUpchance().get(Tool.getItemReinforceType(p))) * 100 + "%")
                            .replace("%downchance%", Tool.getchance(temp_level + 1, Reinforce.getDownchance().get(Tool.getItemReinforceType(p))) * 100 + "%"));
                    im.setLore(lore);
                    i.setItemMeta(im);
                    inv.get(p).setItem(key, i);

                });
            }catch(Exception e){
                Tool.sendListMessage(p,CowReinforce.getinstance().getConfig().getStringList("Settings.GetMaxLevelMessage"));
            }
        }else {
            Tool.sendListMessage(p, CowReinforce.getinstance().getConfig().getStringList("Settings.NoItemInHandMessage"));

        }

        if(!Tool.getItemReinforceType(p).equals("未找到") && Tool.getReinforceLevel(p) < Reinforce.getMaxlevel().get(Tool.getItemReinforceType(p))){
            p.openInventory(inv.get(p));
        }
    }


}
