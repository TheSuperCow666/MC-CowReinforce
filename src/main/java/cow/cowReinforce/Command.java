package cow.cowReinforce;

import com.fileTool.*;
import de.tr7zw.nbtapi.NBT;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class Command implements TabExecutor {
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command arg1, String arg2, String[] args) {
        if (args.length == 0) {
            Tool.sendListMessage(sender, CowReinforce.getinstance().getConfig().getStringList("Settings.HelpMessage"));
            return true;
        }
        if (args[0].equals("reload")) {
            Tool.sendMessage(sender,   "&bCowReinforce &a配置重载成功！");
            CowReinforce.getinstance().saveDefaultConfig();
            CowReinforce.getinstance().reloadConfig();
            Item.loadConfig();
            Item.load();
            Reinforce.loadConfig();
            Reinforce.load();
            Gui.loadConfig();
            Gui.load();
            Gui2.loadConfig();
            Gui2.load();
            Tab.loadTabConfig();
            SpecialItem.loadConfig();
            SpecialItem.load();
            Inheritance.loadConfig();
            Inheritance.load();
            for(Player p : Bukkit.getOnlinePlayers()){
                ReinforceGui.loadReinforceGui(p);
                InheritanceGui.loadInherianceGui(p);
            }
        } else if (args[0].equals("open")){
            try {
                if (args.length == 3) {
                    if(args[1].equals("Reinforce")){
                        ReinforceGui.open(Objects.requireNonNull(Bukkit.getPlayer(args[2])));
                    }else if(args[1].equals("Inheritance")){
                        InheritanceGui.open(Objects.requireNonNull(Bukkit.getPlayer(args[2])));
                    }else{
                        Tool.sendMessage(sender, "&bCowReinforce &c错误的格式");
                        Tool.sendMessage(sender, "&bCowReinforce &a->/crf open <Type> <Player>[可选]");
                    }
                } else if (args.length == 2) {
                    if(args[1].equals("Reinforce")){
                        ReinforceGui.open((Player) sender);
                    }else if(args[1].equals("Inheritance")){
                        InheritanceGui.open((Player)sender);
                    }else{
                        Tool.sendMessage(sender, "&bCowReinforce &c错误的格式");
                        Tool.sendMessage(sender, "&bCowReinforce &a->/crf open <Type> <Player>[可选]");
                    }
                } else {
                    Tool.sendMessage(sender, "&bCowReinforce &c错误的格式");
                    Tool.sendMessage(sender, "&bCowReinforce &a->/crf open <Type> <Player>[可选]");
                }
            }catch (Exception ex){
                ex.printStackTrace();
                Tool.sendMessage(sender, "&bCowReinforce &c错误的格式");
                Tool.sendMessage(sender, "&bCowReinforce &a->/crf open <Type> <Player>[可选]");
            }
        }else if (args[0].equals("give")) {
            try {
                if (args.length == 5) {
                    Player p = Bukkit.getPlayer(args[2]);
                    assert p != null;
                    ItemStack i = null;
                    if(args[1].equals("Material")){
                        i = Item.getItem(args[3]);
                    }else if(args[1].equals("Special")){
                        i = SpecialItem.getItem(args[3]);
                    }else if(args[1].equals("Inheritance")){

                        i = Inheritance.getLi().get(args[3]);
                    }
                    assert i != null;
                    i.setAmount(Integer.parseInt(args[4]));
                    p.getInventory().addItem(i);
                    i.setAmount(1);
                    return true;
                } else if (args.length == 4) {
                    Player p = Bukkit.getPlayer(args[2]);
                    assert p != null;
                    ItemStack i = null;
                    if(args[1].equals("Material")){
                        i = Item.getItem(args[3]);
                    }else if(args[1].equals("Special")){
                        i = SpecialItem.getItem(args[3]);
                    }else if(args[1].equals("Inheritance")){
                        i = Inheritance.getLi().get(args[3]);
                    }
                    assert i != null;
                    p.getInventory().addItem(i);
                    return true;
                } else {
                    Tool.sendMessage(sender, "&bCowReinforce &c错误的格式");
                    Tool.sendMessage(sender, "&bCowReinforce &a->/crf give <Type> <Player> <Item> <Amount>[可选]");
                }
            }catch(Exception e){
                Tool.sendMessage(sender, "&bCowReinforce &c错误的格式");
                Tool.sendMessage(sender, "&bCowReinforce &a->/crf give <Type> <Player> <Item> <Amount>[可选]");
            }
        }else if (args[0].equals("addnbt")){
            Player p = (Player)sender;
            Tool.sendMessage(sender, "&bCowReinforce &a->测试成功");
            if(args.length >=3){
                ItemMeta im = p.getInventory().getItemInMainHand().getItemMeta();
                NBT.modify(p.getInventory().getItemInMainHand(), nbt -> {
                    nbt.setString(args[1], args[2]);
                });
                p.getInventory().getItemInMainHand().setItemMeta(im);
            }

        }else if (args[0].equals("shownbt")){
            Player p = (Player)sender;
            Tool.sendMessage(sender, "&bCowReinforce &a->测试成功");
            if(args.length >=2){
                NBT.get(p.getInventory().getItemInMainHand(), nbt -> {
                    p.sendMessage(nbt.getInteger(args[1]) + "");
                });
            }
        }

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            if (args.length == 1)
                return null;
            args = Arrays.<String>copyOfRange(args, 1, args.length);
        }
        return Tab.getCompleteList(args, Tab.getTabList(args, command.getName(), sender.isOp()));
    }


}
