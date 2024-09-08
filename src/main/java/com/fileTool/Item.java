package com.fileTool;


import java.io.File;
import java.util.HashMap;
import java.util.List;

import cow.cowReinforce.CowReinforce;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Item {
    private static HashMap<String,ItemStack> li;

    private static YamlConfiguration yaml;

    public static void loadConfig() {
        File file = new File(CowReinforce.getinstance().getDataFolder(), "item.yml");
        if (!file.exists())
            CowReinforce.getinstance().saveResource("item.yml", false);
        yaml = YamlConfiguration.loadConfiguration(file);
    }

    public static void load() {
        ConfigurationSection section = yaml.getConfigurationSection("Items");
        HashMap<String,ItemStack> hl = new HashMap<String,ItemStack>();
        assert section != null;
        for (String key : section.getKeys(false)) {
            ItemStack i = new ItemStack(Material.valueOf(yaml.getString("Items." + key + ".material").toUpperCase()));
            ItemMeta im = i.getItemMeta();
            if(im != null) {
                im.setDisplayName(yaml.getString("Items." + key + ".name").replace("&","§"));
                List<String> lore = yaml.getStringList("Items." + key + ".lore");
                lore.replaceAll(a -> a.replace("&","§"));
                im.setLore(lore);
                i.setItemMeta(im);
            }
            hl.put(key,i);

        }
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
        Item.li = li;
    }
}
