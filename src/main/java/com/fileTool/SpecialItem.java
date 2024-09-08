package com.fileTool;

import cow.cowReinforce.CowReinforce;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class SpecialItem {
    private static HashMap<String, ItemStack> li;

    private static YamlConfiguration yaml;

    private static HashMap<String ,Integer> level;
    private static HashMap<String ,List<String>> limit;

    public static void loadConfig() {
        File file = new File(CowReinforce.getinstance().getDataFolder(), "SpecialItem.yml");
        if (!file.exists())
            CowReinforce.getinstance().saveResource("SpecialItem.yml", false);
        yaml = YamlConfiguration.loadConfiguration(file);
    }

    public static void load() {
        ConfigurationSection section = yaml.getConfigurationSection("SpecialItems");
        HashMap<String,ItemStack> hl = new HashMap<String,ItemStack>();
        HashMap<String,Integer> level = new HashMap<>();
        HashMap<String,List<String>> limit = new HashMap<>();
        assert section != null;
        for (String key : section.getKeys(false)) {
            ItemStack i = new ItemStack(Material.valueOf(yaml.getString("SpecialItems." + key + ".material").toUpperCase()));
            ItemMeta im = i.getItemMeta();
            if(im != null) {
                im.setDisplayName(yaml.getString("SpecialItems." + key + ".name").replace("&","§"));
                List<String> lore = yaml.getStringList("SpecialItems." + key + ".lore");
                lore.replaceAll(a -> a.replace("&","§"));
                im.setLore(lore);
                i.setItemMeta(im);
            }
            hl.put(key,i);
            level.put(key,yaml.getInt("SpecialItems." + key +  ".level"));
            limit.put(key,yaml.getStringList("SpecialItems." + key +  ".limit"));
        }
        setLevel(level);
        setLimit(limit);
        setLi(hl);
    }

    public static YamlConfiguration getYaml() {
        return yaml;
    }


    public static HashMap<String, ItemStack> getLi() {
        return li;
    }

    public static ItemStack getItem(String key){
        return getLi().get(key);
    }

    public static void setLi(HashMap<String, ItemStack> li) {
        SpecialItem.li = li;
    }

    public static HashMap<String, Integer> getLevel() {
        return level;
    }

    public static void setLevel(HashMap<String, Integer> level) {
        SpecialItem.level = level;
    }

    public static HashMap<String, List<String>> getLimit() {
        return limit;
    }

    public static void setLimit(HashMap<String, List<String>> limit) {
        SpecialItem.limit = limit;
    }
}
