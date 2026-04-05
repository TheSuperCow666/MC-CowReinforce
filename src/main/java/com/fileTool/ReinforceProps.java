package com.fileTool;

import cow.cowReinforce.CowReinforce;
import cow.cowReinforce.Tool;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.Console;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReinforceProps {
    private static YamlConfiguration yaml;

    private static HashMap<String, ItemStack> li;
    private static HashMap<String , List<String>> limit;
    private static HashMap<String, Integer> type;
    private static HashMap<String,Double> upchance;
    public static void loadConfig() {
        File file = new File(CowReinforce.getinstance().getDataFolder(), "ReinforceProps.yml");
        if (!file.exists())
            CowReinforce.getinstance().saveResource("ReinforceProps.yml", false);
        yaml = YamlConfiguration.loadConfiguration(file);

    }
    public static void load() {
        ConfigurationSection section = yaml.getConfigurationSection("ReinforceProps");
        HashMap<String,ItemStack> hl = new HashMap<String,ItemStack>();
        HashMap<String,List<String>> limit = new HashMap<>();
        HashMap<String,Integer> type = new HashMap<>();
        HashMap<String,Double> upchance = new HashMap<>();
        assert section != null;
        for (String key : section.getKeys(false)) {
            ItemStack i = new ItemStack(Material.valueOf(yaml.getString("ReinforceProps." + key + ".material").toUpperCase()));
            ItemMeta im = i.getItemMeta();
            if(im != null) {
                im.setDisplayName(yaml.getString("ReinforceProps." + key + ".name").replace("&","§"));
                List<String> lore = yaml.getStringList("ReinforceProps." + key + ".lore");
                lore.replaceAll(a -> a.replace("&","§"));
                im.setLore(lore);
                i.setItemMeta(im);
            }
            hl.put(key,i);
            limit.put(key,yaml.getStringList("ReinforceProps." + key +  ".limit"));
            type.put(key,yaml.getInt("ReinforceProps." + key + ".type"));
            upchance.put(key,yaml.getDouble("ReinforceProps." + key + ".upchance"));
        }
        setUpchance(upchance);
        setType(type);
        setLimit(limit);
        setLi(hl);
    }

    public static String getType(ItemStack checki,String needgroup){
        if(!checki.hasItemMeta()) return "无";
        for(Map.Entry<String,ItemStack> entry : li.entrySet()){
            if(checki.getItemMeta().getDisplayName().contains(entry.getValue().getItemMeta().getDisplayName())){
                boolean flag = true;
                for(int i = 0;i<checki.getItemMeta().getLore().size();i++){
                    if(!checki.getItemMeta().getLore().get(i).contains(entry.getValue().getItemMeta().getLore().get(i))){
                        flag =false;
                        break;
                    }
                }
                if(flag){
                    for(String group : limit.get(entry.getKey())){
                        if(group.equals(needgroup)){
                            return entry.getKey();
                        }
                    }
                }
            }
        }
        return "无";
    }


    public static YamlConfiguration getYaml() {
        return yaml;
    }

    public static void setYaml(YamlConfiguration yaml) {
        ReinforceProps.yaml = yaml;
    }

    public static HashMap<String, ItemStack> getLi() {
        return li;
    }

    public static void setLi(HashMap<String, ItemStack> li) {
        ReinforceProps.li = li;
    }

    public static HashMap<String, List<String>> getLimit() {
        return limit;
    }

    public static void setLimit(HashMap<String, List<String>> limit) {
        ReinforceProps.limit = limit;
    }

    public static HashMap<String, Integer> getType() {
        return type;
    }

    public static void setType(HashMap<String, Integer> type) {
        ReinforceProps.type = type;
    }

    public static HashMap<String, Double> getUpchance() {
        return upchance;
    }

    public static void setUpchance(HashMap<String, Double> upchance) {
        ReinforceProps.upchance = upchance;
    }
}
