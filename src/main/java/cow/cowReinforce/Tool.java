package cow.cowReinforce;


import com.fileTool.Reinforce;
import com.fileTool.SpecialItem;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tool {
    public static void sendMessage(CommandSender cs ,String message){
        cs.sendMessage(message.replace("&","§"));
    }
    public static void sendListMessage(CommandSender cs , List<String> ls){
        for(String m : ls){
            Tool.sendMessage(cs,m);
        }
    }
    public static double getNumber(String s){
        String regex = "\\((.*?)\\)";
        Pattern pa = Pattern.compile(regex);
        Matcher ma = pa.matcher(s);
        if (ma.find()) {
            return Double.parseDouble(ma.group().replace("(", "").replace(")", ""));
        }
        return 0;
    }
    public static String getType(String s){
        String regex = "(.*?)\\(";
        Pattern pa = Pattern.compile(regex);
        Matcher ma = pa.matcher(s);
        if (ma.find()) {
            return ma.group().replace("(", "");
        }
        return "none";
    }
    public static double getAttributeNumber(String attribute, String s){
        s = s + "末尾";
        Pattern pa = Pattern.compile(attribute +"(.*?)末尾");
        Matcher ma = pa.matcher(s);
        if (ma.find()) {
            return Double.parseDouble(ma.group()
                    .replace(attribute, "").replace(":", "").replace("+", "")
                    .replace("末尾",""));
        }
        return 0;
    }
    public static  ItemStack getLastLevelItem(Player p ,ItemStack oldi, String type){
        ItemMeta im = oldi.getItemMeta();
        List<String> lore = new ArrayList<>();
        String name = im.getDisplayName();
        if (!name.contains("+")){
            name = name + "§f +0";
        }
        name = name.replace("+" + Tool.getReinforceLevel(p), "+" + (Tool.getReinforceLevel(p) - 1));
        im.setDisplayName(name);
        for(String s : oldi.getItemMeta().getLore()){
            String att = hasAttribute(s,type);
            if (!att.equals("未找到")) {
                Pattern pa = Pattern.compile(att +"(.*?)末尾");
                Matcher ma = pa.matcher(s +  "末尾" );
                if(ma.find()){
                    String o = ma.group().replace(att, "");
                    Pattern pattern = Pattern.compile("§" + "(.?)");
                    Matcher matcher = pattern.matcher(o);
                    while(matcher.find()){
                        o = o.replace(matcher.group(),"");
                    }
                    double result = CalculationUtils.getResult((o.replace("%","").replace("末尾","").replace(":","")) + "/" +Reinforce.getRFarttribute().get(type).get(att));
                    DecimalFormat df = new DecimalFormat(Reinforce.getDecimals().get(type));
                    String result2 = df.format(result);
                    String addlore = (s +"Finally") .replace(o.replace("末尾","").replace(":","").replace(" ", "") + "Finally", result2);
                    if(s.contains("%")){
                        lore.add(addlore + "%");
                    }else{
                        lore.add(addlore);
                    }
                }
            } else {
                lore.add(s);
            }
        }
        assert im != null;
        im.setLore(lore);
        oldi.setItemMeta(im);
        return oldi;
    }
    public static  ItemStack getNextLevelItem(Player p ,ItemStack oldi, String type){
        ItemMeta im = oldi.getItemMeta();
        List<String> lore = new ArrayList<>();
        String name = im.getDisplayName();
        if (!name.contains("+")){
            name = name + "§f +0";
        }
        name = name.replace("+" + Tool.getReinforceLevel(p), "+" + (Tool.getReinforceLevel(p) + 1));
        im.setDisplayName(name);
        for(String s : oldi.getItemMeta().getLore()){
            String att = hasAttribute(s,type);
            if (!att.equals("未找到") && !s.contains(Reinforce.getIgnore().get(type)) ) {
                Pattern pa = Pattern.compile(att +"(.*?)末尾");
                Matcher ma = pa.matcher(s +  "末尾" );
                if(ma.find()){
                    String o = ma.group().replace(att, "");
                    Pattern pattern = Pattern.compile("§" + "(.?)");
                    Matcher matcher = pattern.matcher(o);
                    while(matcher.find()){
                        o = o.replace(matcher.group(),"");
                    }
                    double result = CalculationUtils.getResult((o.replace("%","").replace("末尾","").replace(":","")
                    )+ "*" +Reinforce.getRFarttribute().get(type).get(att));
                    DecimalFormat df = new DecimalFormat(Reinforce.getDecimals().get(type));
                    String result2 = df.format(result);
                    String addlore = (s +"Finally") .replace(o.replace("末尾","").replace(":","").replace(" ", "") + "Finally", result2);
                    if(s.contains("%")){
                        lore.add(addlore + "%");
                    }else{
                        lore.add(addlore);
                    }
                }
            } else {
                lore.add(s);
            }
        }
        assert im != null;
        im.setLore(lore);
        oldi.setItemMeta(im);
        return oldi;
    }
    public static boolean checkLevelItem(Player p ,ItemStack oldi,int level){
        int levels = level - Tool.getReinforceLevel(oldi);
        if(levels <=0){
            Tool.sendListMessage(p,CowReinforce.getinstance().getConfig().getStringList("Settings.RatherThanLevelMessage"));
            return false;
        }
        return true;
    }
    public static void setLevelItem(Player p ,ItemStack oldi, String type,int level){
        ItemMeta im = oldi.getItemMeta();
        List<String> lore = new ArrayList<>();
        String name = im.getDisplayName();
        if (!name.contains("+")){
            name = name + "§f +0";
        }
        int levels = level - Tool.getReinforceLevel(oldi);
        if(levels <=0){
            Tool.sendListMessage(p,CowReinforce.getinstance().getConfig().getStringList("Settings.RatherThanLevelMessage"));
            return;
        }else{
            name = name.replace("+" + Tool.getReinforceLevel(oldi), "+" + level);
            assert oldi.hasItemMeta();
            for(String s : oldi.getItemMeta().getLore()) {
                String att = hasAttribute(s,type);
                if(!att.equals("未找到")){
                    double multiple =  Math.pow(Reinforce.getRFarttribute().get(type).get(att),levels);
                    Pattern pa = Pattern.compile(att +"(.*?)末尾");
                    Matcher ma = pa.matcher(s +  "末尾" );
                    if(ma.find()){
                        String o = ma.group().replace(att, "");
                        Pattern pattern = Pattern.compile("§" + "(.?)");
                        Matcher matcher = pattern.matcher(o);
                        while(matcher.find()){
                            o = o.replace(matcher.group(),"");
                        }
                        double result = CalculationUtils.getResult((o.replace("%","").replace("末尾","").replace(":","")) + "*" +multiple);
                        DecimalFormat df = new DecimalFormat(Reinforce.getDecimals().get(type));
                        String result2 = df.format(result);
                        String addlore = (s +"Finally") .replace(o.replace("末尾","").replace(":","").replace(" ", "") + "Finally", result2);
                        if(s.contains("%")){
                            lore.add(addlore + "%");
                        }else{
                            lore.add(addlore);
                        }
                    }
                } else {
                    lore.add(s);
                }
            }
            im.setDisplayName(name);
            im.setLore(lore);
            oldi.setItemMeta(im);
        }
    }
    public static String hasAttribute(String s,String type){
        for(String key : Reinforce.getRFarttribute().get(type).keySet()) {
            if (s.contains(key)) {
                return key;
            }
        }
        return "未找到";
    }
    public static boolean compareItem(ItemStack playeri, ItemStack comparei) {
        ItemMeta im1 = playeri.getItemMeta();
        ItemMeta im2 = comparei.getItemMeta();
        if (im1.getDisplayName().contains(im2.getDisplayName()) &&
                playeri.getType() == comparei.getType()) {
            List<String> l1 = im1.getLore();
            List<String> l2 = im2.getLore();
            if (l1.containsAll(l2))
                return true;
            return false;
        }
        return false;
    }

    public static int checkItem(ItemStack checki, Player p) {
        int result = 0;
        for (int o = 0; o < p.getInventory().getSize(); o++) {
            if (p.getInventory().getItem(o) != null)
                if (p.getInventory().getItem(o).hasItemMeta() &&
                        p.getInventory().getItem(o).getItemMeta().hasDisplayName() &&
                        p.getInventory().getItem(o).getItemMeta().hasLore() &&
                        compareItem(p.getInventory().getItem(o), checki))
                    result += p.getInventory().getItem(o).getAmount();
        }
        return result;
    }
    public static String  getItemReinforceType(Player p){
        ItemStack i = p.getInventory().getItemInMainHand();
        if(i.hasItemMeta()){
            if(i.getItemMeta().hasDisplayName()) {
                for (Map.Entry<String, List<String>> entry : Reinforce.getCheckname().entrySet()) {
                    for (String name : entry.getValue()) {
                        if (i.getItemMeta().getDisplayName().contains(name)) {
                            return entry.getKey();
                        }
                    }
                }
            }
            if (i.getItemMeta().hasLore()) {
                for (Map.Entry<String, List<String>> entry : Reinforce.getChecklore().entrySet()) {
                    for (String lore : entry.getValue()) {
                        for (String ilore : i.getItemMeta().getLore()) {
                            if (ilore.contains(lore)) {
                                    return entry.getKey();
                            }
                        }
                    }
                }
            }

        }
        return "未找到";
    }
    public static String  getItemReinforceType(Player p,int slot){
        ItemStack i = p.getInventory().getItem(slot);
        if(i == null){
            return "未找到";
        }
        if(i.hasItemMeta()){
            if(i.getItemMeta().hasDisplayName()) {
                for (Map.Entry<String, List<String>> entry : Reinforce.getCheckname().entrySet()) {
                    for (String name : entry.getValue()) {
                        if (i.getItemMeta().getDisplayName().contains(name)) {
                            return entry.getKey();
                        }
                    }
                }
            }
            if (i.getItemMeta().hasLore()) {
                for (Map.Entry<String, List<String>> entry : Reinforce.getChecklore().entrySet()) {
                    for (String lore : entry.getValue()) {
                        for (String ilore : i.getItemMeta().getLore()) {
                            if (ilore.contains(lore)) {
                                return entry.getKey();
                            }
                        }
                    }
                }
            }

        }
        return "未找到";
    }
    public static void dItem(ItemStack i, Player p, int total) {
        for (int o = 0; o < p.getInventory().getSize(); o++) {
            if (total <= 0)
                return;
            if (p.getInventory().getItem(o) != null)
                if (p.getInventory().getItem(o).hasItemMeta() &&
                        p.getInventory().getItem(o).getItemMeta().hasDisplayName() &&
                        p.getInventory().getItem(o).getItemMeta().hasLore() &&
                        p.getInventory().getItem(o).isSimilar(i))
                    if (total > 64) {
                        p.getInventory().setItem(o, new ItemStack(Material.AIR));
                        total -= 64;
                    } else if (total < 64 && p.getInventory().getItem(o).getAmount() >= total) {
                        p.getInventory().getItem(o).setAmount(p.getInventory().getItem(o).getAmount() - total);
                        total = 0;
                    } else if (total < 64 && p.getInventory().getItem(o).getAmount() < total) {
                        total -= p.getInventory().getItem(o).getAmount();
                        p.getInventory().setItem(o, new ItemStack(Material.AIR));
                    }
        }
    }
    public static int getReinforceLevel(Player p){
        ItemStack i = p.getInventory().getItemInMainHand();
        if(i.getItemMeta() != null){
            if(i.getItemMeta().hasDisplayName()){
                if(i.getItemMeta().getDisplayName().contains("+")){
                    return Integer.parseInt(i.getItemMeta().getDisplayName().split("\\+")[1]);
                }else{
                    return 0;
                }
            }
        }
        return 0;
    }
    public static int getReinforceLevel(ItemStack i){
        if(i.getItemMeta() != null){
            if(i.getItemMeta().hasDisplayName()){
                if(i.getItemMeta().getDisplayName().contains("+")){
                    return Integer.parseInt(i.getItemMeta().getDisplayName().split("\\+")[1]);
                }else{
                    return 0;
                }
            }
        }
        return 0;
    }
    public static String getMaterialDescription(Player p ,String type){
        String s = "";

        for(Map.Entry<ItemStack, Integer> entry : Reinforce.getNeeditem().get(type).entrySet()){
            if(Tool.checkItem(entry.getKey(),p) >= entry.getValue()){
                s = s + CowReinforce.getinstance().getConfig().getString("Settings.Material.enough")
                        .replace("&","§")
                        .replace("%material%",entry.getKey().getItemMeta().getDisplayName())
                        .replace("%amount%","" + entry.getValue());

            }else{
                s = s  +CowReinforce.getinstance().getConfig().getString("Settings.Material.notenough")
                        .replace("&","§")
                        .replace("%material%",entry.getKey().getItemMeta().getDisplayName())
                        .replace("%amount%","" + entry.getValue());
            }
        }
        return s;
    }
    public static double getchance(int nextlevel, HashMap<Integer,Double> chances){
        return chances.get(nextlevel);
    }
    //SpecialItem

    public static String getSpecialGroup(Player p ,ItemStack checki){
        for(Map.Entry<String,ItemStack> entry: SpecialItem.getLi().entrySet()){
            if(checki.getItemMeta().getDisplayName().equals(entry.getValue().getItemMeta().getDisplayName())
            && checki.getItemMeta().getLore().equals(entry.getValue().getItemMeta().getLore())){
                return entry.getKey();
            }
        }
        return "未找到";
    }
    public static String getReinforceGroup(Player p ,ItemStack si,int slot){
        String group = getSpecialGroup(p,si);
        String igroup = getItemReinforceType(p,slot);
        for(String limit : SpecialItem.getLimit().get(group)){
            if(igroup.equals(limit)){
                return igroup;
            }
        }
        return "未找到";
    }
}
