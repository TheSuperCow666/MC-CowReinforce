package cow.cowReinforce;

import com.fileTool.Gui2;
import com.fileTool.Reinforce;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class InheritanceGui implements InventoryHolder {

    public static HashMap<Player,Inventory> inv;
    public static Inventory inventory;
    static{
        inv = new HashMap<>();
    }
    @Override
    public Inventory getInventory() {
        return null; // 无需实现，仅用于标识
    }
    public static void loadInherianceGui(Player p){
        Inventory inv2 =  Bukkit.createInventory(new InheritanceGui(), Gui2.getSize(),Gui2.getTitle());
        Gui2.getContent().forEach(inv2::setItem);
        inv.put(p,inv2);
        inventory = inv2;
    }
    public static void open(Player p){
        if(!inv.containsKey(p)){
            loadInherianceGui(p);
        }
        if(!Tool.getItemReinforceType(p).equals("未找到")) {
            try{
                Gui2.getQianghua().forEach((key, value) -> {
                    ItemStack i = value.clone();
                    List<String> lore = Objects.requireNonNull(i.getItemMeta()).getLore();
                    ItemMeta im = i.getItemMeta();
                    assert lore != null;
                    lore.replaceAll(a -> a
                            .replace("%name%", p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()));
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
