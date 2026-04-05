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

public class Gui {


    private static YamlConfiguration yaml;
    private static int size;
    private static String title;
    private static HashMap<Integer, ItemStack> content;
    private static HashMap<Character, ItemStack> symbol;
    private static HashMap<Integer, ItemStack> qianghua;

    private static int qiangbao_slot;
    private static int xingyunfu_slot;

    public static void loadConfig() {
        File file = new File(CowReinforce.getinstance().getDataFolder(), "Gui.yml");
        if (!file.exists())
            CowReinforce.getinstance().saveResource("Gui.yml", false);
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
                if(s.charAt(o) == 'n'){
                    qiangbao_slot = n;
                }else if(s.charAt(o) == 'l'){
                    xingyunfu_slot = n;
                }
                if(symbol.containsKey(s.charAt(o))){
                    i = symbol.get(s.charAt(o));
                }else{
                    i = new ItemStack(Material.AIR);
                }
                if (s.charAt(o) == 'x') {
                    qianghua.put(n,i);
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
        Gui.size = size;
    }

    public static String getTitle() {
        return title;
    }

    public static void setTitle(String title) {
        Gui.title = title;
    }

    public static HashMap<Integer, ItemStack> getContent() {
        return content;
    }

    public static void setContent(HashMap<Integer, ItemStack> content) {
        Gui.content = content;
    }

    public static HashMap<Character, ItemStack> getSymbol() {
        return symbol;
    }

    public static void setSymbol(HashMap<Character, ItemStack> symbol) {
        Gui.symbol = symbol;
    }

    public static HashMap<Integer, ItemStack> getQianghua() {
        return qianghua;
    }

    public static void setQianghua(HashMap<Integer, ItemStack> qianghua) {
        Gui.qianghua = qianghua;
    }

    public static int getQiangbao_slot() {
        return qiangbao_slot;
    }

    public static void setQiangbao_slot(int qiangbao_slot) {
        Gui.qiangbao_slot = qiangbao_slot;
    }

    public static int getXingyunfu_slot() {
        return xingyunfu_slot;
    }

    public static void setXingyunfu_slot(int xingyunfu_slot) {
        Gui.xingyunfu_slot = xingyunfu_slot;
    }
}
