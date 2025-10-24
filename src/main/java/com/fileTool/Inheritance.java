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

public class Inheritance {
    private static HashMap<String, ItemStack> li;

    private static YamlConfiguration yaml;

    private static HashMap<String ,Integer> maxlevel;
    private static HashMap<String ,Integer> prop;
    private static HashMap<String ,Boolean> enchant;
    private static HashMap<String , List<String>> limit;
    private static HashMap<String , List<String>> addlore;

    public static void loadConfig() {
        File file = new File(CowReinforce.getinstance().getDataFolder(), "Inheritance.yml");
        if (!file.exists())
            CowReinforce.getinstance().saveResource("Inheritance.yml", false);
        yaml = YamlConfiguration.loadConfiguration(file);
    }

    public static void load(){
        ConfigurationSection section = yaml.getConfigurationSection("Inheritance");
        HashMap<String, ItemStack> li = new HashMap<>();
        HashMap<String ,Integer> maxlevel  = new HashMap<>();
        HashMap<String ,Integer> prop =  new HashMap<>();
        HashMap<String ,Boolean> enchat =  new HashMap<>();
        HashMap<String , List<String>> limit = new HashMap<>();
        HashMap<String , List<String>> addlore  = new HashMap<>();
        assert section != null;
        for (String key : section.getKeys(false)) {
            ItemStack i = new ItemStack(Material.valueOf(yaml.getString("Inheritance." + key + ".material").toUpperCase()));
            ItemMeta im = i.getItemMeta();
            prop.put(key,yaml.getInt("Inheritance." + key + ".prop"));
            maxlevel.put(key,yaml.getInt("Inheritance." + key +  ".maxlevel"));
            if(im != null) {
                im.setDisplayName(yaml.getString("Inheritance." + key + ".name").replace("&","§"));
                List<String> lore = yaml.getStringList("Inheritance." + key + ".lore");
                lore.replaceAll(a -> a.replace("&","§")
                        .replace("%Inheritance_prop%",prop.get(key) +"")
                        .replace("%Inheritance_maxlevel%",maxlevel.get(key) + ""));
                im.setLore(lore);
                i.setItemMeta(im);
            }
            li.put(key,i);
            addlore.put(key,yaml.getStringList("Inheritance." + key +".addlore"));
            limit.put(key,yaml.getStringList("Inheritance." + key +  ".limit"));
            enchat.put(key,yaml.getBoolean("Inheritance." + key +".enchant"));
        }
        setLi(li);
        setProp(prop);
        setAddlore(addlore);
        setMaxlevel(maxlevel);
        setLimit(limit);
        setEnchant(enchat);
    }

    public static YamlConfiguration getYaml() {
        return yaml;
    }

    public static HashMap<String, Integer> getMaxlevel() {
        return maxlevel;
    }

    public static void setMaxlevel(HashMap<String, Integer> maxlevel) {
        Inheritance.maxlevel = maxlevel;
    }

    public static HashMap<String, List<String>> getAddlore() {
        return addlore;
    }

    public static void setAddlore(HashMap<String, List<String>> addlore) {
        Inheritance.addlore = addlore;
    }

    public static HashMap<String, Integer> getProp() {
        return prop;
    }

    public static void setProp(HashMap<String, Integer> prop) {
        Inheritance.prop = prop;
    }

    public static HashMap<String, List<String>> getLimit() {
        return limit;
    }

    public static void setLimit(HashMap<String, List<String>> limit) {
        Inheritance.limit = limit;
    }

    public static HashMap<String, ItemStack> getLi() {
        return li;
    }

    public static void setLi(HashMap<String, ItemStack> li) {
        Inheritance.li = li;
    }

    public static HashMap<String, Boolean> getEnchant() {
        return enchant;
    }

    public static void setEnchant(HashMap<String, Boolean> enchant) {
        Inheritance.enchant = enchant;
    }
}
