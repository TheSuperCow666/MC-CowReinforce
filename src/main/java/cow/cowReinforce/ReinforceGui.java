package cow.cowReinforce;

import com.fileTool.Gui;
import com.fileTool.Reinforce;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ReinforceGui {

    public static HashMap<Player,Inventory> inv;

    static{
        inv = new HashMap<>();
    }
    public static void loadReinforceGui(Player p){
        Inventory inv2 =  Bukkit.createInventory(null, Gui.getSize(),Gui.getTitle());
        Gui.getContent().forEach(inv2::setItem);
        inv.put(p,inv2);
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
                    lore.replaceAll(a -> a
                            .replace("%attribute%", "属性")
                            .replace("%name%", p.getInventory().getItemInMainHand().getItemMeta().getDisplayName())
                            .replace("%target_level%", Tool.getReinforceLevel(p) + "")
                            .replace("%target_uplevel%", Tool.getReinforceLevel(p) + 1 + "")
                            .replace("%need_material%", Tool.getMaterialDescription(p, Tool.getItemReinforceType(p)))
                            .replace("%maxlevel%", Reinforce.getMaxlevel().get(Tool.getItemReinforceType(p)) + "")
                            .replace("%upchance%", Tool.getchance(Tool.getReinforceLevel(p) + 1, Reinforce.getUpchance().get(Tool.getItemReinforceType(p))) * 100 + "%")
                            .replace("%downchance%", Tool.getchance(Tool.getReinforceLevel(p) + 1, Reinforce.getDownchance().get(Tool.getItemReinforceType(p))) * 100 + "%"));
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
