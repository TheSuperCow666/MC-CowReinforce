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

public class Gui2 {


    private static YamlConfiguration yaml;
    private static int size;
    private static String title;
    private static int checkslot;
    private static HashMap<Integer, ItemStack> content;
    private static HashMap<Character, ItemStack> symbol;
    private static HashMap<Integer, ItemStack> qianghua;

    public static void loadConfig() {
        File file = new File(CowReinforce.getinstance().getDataFolder(), "InheritanceGui.yml");
        if (!file.exists())
            CowReinforce.getinstance().saveResource("InheritanceGui.yml", false);
        yaml = YamlConfiguration.loadConfiguration(file);
    }

    public static void load() {
        setSize(yaml.getInt("Gui.Settings.size"));
        setTitle(yaml.getString("Gui.Settings.title").replace("&","§"));
        HashMap<Character, ItemStack> symbol = new HashMap<>();
        HashMap<Integer, ItemStack> content = new HashMap<>();
        HashMap<Integer, ItemStack> qianghua = new HashMap<>();
        ConfigurationSection section = yaml.getConfigurationSection("Gui.Item");
        for(String key : section.getKeys(false)){
            ItemStack i = new ItemStack(Material.valueOf(yaml.getString("Gui.Item." + key + ".material").toUpperCase()));
            ItemMeta im = i.getItemMeta();
            assert im != null;
            im.setDisplayName(yaml.getString("Gui.Item." + key + ".name").replace("&","§"));
            List<String> lore = yaml.getStringList("Gui.Item." + key + ".lore");
            lore.replaceAll(a -> a.replace("&","§"));
            im.setLore(lore);
            i.setItemMeta(im);
            i.setAmount(yaml.getInt("Gui.Item." + key + ".amount"));
            symbol.put(key.toCharArray()[0], i);
            setSymbol(symbol);
        }
        List<String> contents = yaml.getStringList("Gui.Settings.content");
        for(int x = 0;x<size; x=x+9) {
            String s = contents.get(x/9);
            for (int o = 0; o < 9;o++) {
                int n = x+o;
                ItemStack i;
                if(symbol.containsKey(s.charAt(o))){
                    i = symbol.get(s.charAt(o));
                }else{
                    i = new ItemStack(Material.AIR);
                }
                if (s.charAt(o) == 'x') {
                    qianghua.put(n,i);
                }
                if(s.charAt(o) == 'c'){
                    setCheckslot(n);
                }
                content.put(n,i);
            }
        }
        setContent(content);
        setQianghua(qianghua);
    }

    public static YamlConfiguration getYaml() {
        return yaml;
    }


    public static int getSize() {
        return size;
    }

    public static void setSize(int size) {
        Gui2.size = size;
    }

    public static String getTitle() {
        return title;
    }

    public static void setTitle(String title) {
        Gui2.title = title;
    }

    public static HashMap<Integer, ItemStack> getContent() {
        return content;
    }

    public static void setContent(HashMap<Integer, ItemStack> content) {
        Gui2.content = content;
    }

    public static HashMap<Character, ItemStack> getSymbol() {
        return symbol;
    }

    public static void setSymbol(HashMap<Character, ItemStack> symbol) {
        Gui2.symbol = symbol;
    }

    public static HashMap<Integer, ItemStack> getQianghua() {
        return qianghua;
    }

    public static void setQianghua(HashMap<Integer, ItemStack> qianghua) {
        Gui2.qianghua = qianghua;
    }

    public static int getCheckslot() {
        return checkslot;
    }

    public static void setCheckslot(int checkslot) {
        Gui2.checkslot = checkslot;
    }
}
