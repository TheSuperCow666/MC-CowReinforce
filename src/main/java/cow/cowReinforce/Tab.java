package cow.cowReinforce;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.fileTool.Item;
import com.fileTool.SpecialItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Tab {
    public static HashMap<String, List<String>> tabListMap = new HashMap<>();

    public static HashMap<String, List<String>> tabListMapOp = new HashMap<>();


    static {
        loadTabConfig();
    }
    public static void loadTabConfig(){
        List<String> pname = new ArrayList<>();
        List<String> li = new ArrayList<>();
        List<String> si = new ArrayList<>();
        tabListMapOp.put("CowReinforce", Arrays.asList(new String[] { "reload", "give","open"}));
        tabListMapOp.put("CowReinforce.give", Arrays.asList(new String[] { "Material", "Special"}));
        for(String s : Item.getLi().keySet()){
            if(s != null) {
                li.add(s);
            }
        }
        for(String s : SpecialItem.getLi().keySet()){
            if(s != null) {
                si.add(s);
            }
        }
        for(Player p : Bukkit.getOnlinePlayers()){
            pname.add(p.getName());
            tabListMapOp.put("CowReinforce.give.Material." + p.getName(), li);
            tabListMapOp.put("CowReinforce.give.Special." + p.getName(), si);
        }
        tabListMapOp.put("CowReinforce.give.Material", pname);
        tabListMapOp.put("CowReinforce.give.Special", pname);
        tabListMapOp.put("CowReinforce.open", pname);
    }
    public static List<String> getTabList(String[] args, String command, boolean ifop) {
        StringBuilder builder = new StringBuilder(command);
        for (int i = 1; i < args.length; i++)
            builder.append(".").append(args[i - 1]);
        if (ifop)
            return tabListMapOp.get(builder.toString());
        return tabListMap.get(builder.toString());
    }

    public static List<String> getCompleteList(String[] args, List<String> list) {
        return getCompleteList(args, list, false);
    }

    public static List<String> getCompleteList(String[] args, List<String> list, boolean listToLowerCase) {
        List<String> ret = new ArrayList<>();
        if (list == null)
            return ret;
        if (list.isEmpty())
            return null;
        if (args[args.length - 1].equals(""))
            return list;
        String arg = args[args.length - 1];
        for (String value : list) {
            if (listToLowerCase) {
                if (value.toLowerCase().startsWith(arg))
                    ret.add(value);
                continue;
            }
            if (value.startsWith(arg))
                ret.add(value);
        }
        return ret;
    }
}