package cow.cowReinforce;

import com.fileTool.Gui;
import com.fileTool.Item;
import com.fileTool.Reinforce;
import com.fileTool.SpecialItem;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
            Tab.loadTabConfig();
            SpecialItem.loadConfig();
            SpecialItem.load();
            for(Player p : Bukkit.getOnlinePlayers()){
                ReinforceGui.loadReinforceGui(p);
            }
        } else if (args[0].equals("open")){
            try {
                if (args.length == 2) {
                    ReinforceGui.open(Objects.requireNonNull(Bukkit.getPlayer(args[1])));
                } else if (args.length == 1) {
                    ReinforceGui.open((Player) sender);
                } else {
                    Tool.sendMessage(sender, "&bCowReinforce &c错误的格式");
                    Tool.sendMessage(sender, "&bCowReinforce &a->/crf open <Player>[可选]");
                }
            }catch (Exception ex){
                ex.printStackTrace();
                Tool.sendMessage(sender, "&bCowReinforce &c错误的格式");
                Tool.sendMessage(sender, "&bCowReinforce &a->/crf open <Player>[可选]");
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
        }else if (args[0].equals("test")){
            Player p = (Player)sender;
            Tool.sendMessage(sender, "&bCowReinforce &a->测试成功");
            Tool.getLastLevelItem(p,p.getInventory().getItemInMainHand(),Tool.getItemReinforceType(p));

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
