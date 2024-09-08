package com.fileTool;


import cow.cowReinforce.CowReinforce;
import cow.cowReinforce.Tool;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class Reinforce {

    private static HashMap<String,String> decimals;
    private static HashMap<String,Integer> maxlevel;
    private static HashMap<String,List<String>> checkname;
    private static HashMap<String,List<String>> checklore;
    private static HashMap<String, HashMap<ItemStack,Integer>> needitem;
    private static HashMap<String, HashMap<String,Double>> RFarttribute;
    private static HashMap<String, HashMap<Integer,Double>> upchance;
    private static HashMap<String, HashMap<Integer,Double>> downchance;


    private static YamlConfiguration yaml;


    public static void loadConfig() {
        File file = new File(CowReinforce.getinstance().getDataFolder(), "Reinforce.yml");
        if (!file.exists())
            CowReinforce.getinstance().saveResource("Reinforce.yml", false);
        yaml = YamlConfiguration.loadConfiguration(file);
    }

    public static void load() {
        ConfigurationSection section = yaml.getConfigurationSection("ReinForce");
        assert section != null;
        HashMap<String,List<String>> checkname = new HashMap<>();
        HashMap<String,List<String>> checklore = new HashMap<>();
        HashMap<String,Integer> maxlevel = new HashMap<>();
        HashMap<String,String> decimals = new HashMap<>();
        HashMap<String, HashMap<ItemStack,Integer>> hii =  new HashMap<>();
        HashMap<String, HashMap<String,Double>> hsd =  new HashMap<String, HashMap<String,Double>>();
        HashMap<String, HashMap<Integer,Double>> upchance = new HashMap<>();
        HashMap<String, HashMap<Integer,Double>> downchance = new HashMap<>();
        for(String key : section.getKeys(false)){
            ConfigurationSection chancesection = yaml.getConfigurationSection("ReinForce." + key + ".Settings.chance");
            //分类 1
            List<String> lsn =  yaml.getStringList("ReinForce." + key + ".Check.name");
            List<String> lsl =  yaml.getStringList("ReinForce." + key + ".Check.lore");
            // 分类2
            HashMap<ItemStack,Integer> hii2 = new HashMap<ItemStack,Integer>();
            HashMap<String,Double> hsd2 = new HashMap<String,Double>();
            HashMap<Integer,Double> upchance2 = new HashMap<>();
            HashMap<Integer,Double> downchance2 = new HashMap<>();
            maxlevel.put(key,yaml.getInt("ReinForce." + key + ".Settings.maxlevel"));
            decimals.put(key,yaml.getString("ReinForce." + key + ".Settings.decimals"));

            for(String s : yaml.getStringList("ReinForce." + key + ".Settings.item.list")){
                hii2.put( Item.getItem(Tool.getType(s)),(int)Tool.getNumber(s));
            }
            for(String s : yaml.getStringList("ReinForce." + key + ".Settings.attribute")){
                hsd2.put(Tool.getType(s),Tool.getNumber(s));
            }
            for(String a : chancesection.getKeys(false)){
                for(int n = 1 ;n<=maxlevel.get(key);n++){
                    String[] levels = a.split("-");
                    double uc = yaml.getDouble("ReinForce." + key + ".Settings.chance." + a + ".uc");
                    double dc = yaml.getDouble("ReinForce." + key + ".Settings.chance." + a + ".dc");
                    if (n >= Integer.parseInt(levels[0]) && n <= Integer.parseInt(levels[1])) {
                        upchance2.put(n,uc);
                    }
                    if (n >= Integer.parseInt(levels[0]) && n <= Integer.parseInt(levels[1])) {
                        downchance2.put(n,dc);
                    }
                }
            }
            setMaxlevel(maxlevel);
            upchance.put(key,upchance2);
            downchance.put(key,downchance2);
            hii.put(key,hii2);
            hsd.put(key,hsd2);
            lsn.replaceAll(a->a.replace("&","§"));
            lsl.replaceAll(a->a.replace("&","§"));
            checkname.put(key,lsn);
            checklore.put(key,lsl);
        }
        Reinforce.setDownchance(downchance);
        Reinforce.setDecimals(decimals);
        Reinforce.setUpchance(upchance);
        Reinforce.setRFarttribute(hsd);
        Reinforce.setNeeditem(hii);
        Reinforce.setCheckname(checkname);
        Reinforce.setChecklore(checklore);

    }

    public static YamlConfiguration getYaml() {
        return yaml;
    }






    public static HashMap<String, HashMap<ItemStack, Integer>> getNeeditem() {
        return needitem;
    }

    public static void setNeeditem(HashMap<String, HashMap<ItemStack, Integer>> needitem) {
        Reinforce.needitem = needitem;
    }

    public static HashMap<String, HashMap<String, Double>> getRFarttribute() {
        return RFarttribute;
    }

    public static void setRFarttribute(HashMap<String, HashMap<String, Double>> RFarttribute) {
        Reinforce.RFarttribute = RFarttribute;
    }

    public static HashMap<String, HashMap<Integer, Double>> getUpchance() {
        return upchance;
    }

    public static void setUpchance(HashMap<String, HashMap<Integer, Double>> upchance) {
        Reinforce.upchance = upchance;
    }



    public static HashMap<String, List<String>> getCheckname() {
        return checkname;
    }

    public static void setCheckname(HashMap<String, List<String>> checkname) {
        Reinforce.checkname = checkname;
    }

    public static HashMap<String, List<String>> getChecklore() {
        return checklore;
    }

    public static void setChecklore(HashMap<String, List<String>> checklore) {
        Reinforce.checklore = checklore;
    }

    public static void setMaxlevel(HashMap<String, Integer> maxlevel) {
        Reinforce.maxlevel = maxlevel;
    }
    public static HashMap<String, Integer> getMaxlevel() {
        return maxlevel;
    }

    public static HashMap<String, String> getDecimals() {
        return decimals;
    }

    public static void setDecimals(HashMap<String, String> decimals) {
        Reinforce.decimals = decimals;
    }

    public static HashMap<String, HashMap<Integer, Double>> getDownchance() {
        return downchance;
    }

    public static void setDownchance(HashMap<String, HashMap<Integer, Double>> downchance) {
        Reinforce.downchance = downchance;
    }
}
