package cow.cowReinforce;

import com.fileTool.Gui;
import com.fileTool.Item;
import com.fileTool.Reinforce;
import com.fileTool.SpecialItem;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CowReinforce extends JavaPlugin {

    private static JavaPlugin instance;
    public static double version = 1.05;

    @Override
    public void onEnable() {
        System.out.print(System.getProperty("java.version"));
        instance = this;
        Tool.sendMessage(Bukkit.getConsoleSender(),"&aCowReinforce");
        Tool.sendMessage(Bukkit.getConsoleSender(),"&a  启动成功！");
        Tool.sendMessage(Bukkit.getConsoleSender(),"&aVersion: " + version);
        Tool.sendMessage(Bukkit.getConsoleSender(),"&aAuthor: TheSuperCow");
        Tool.sendMessage(Bukkit.getConsoleSender(),"&aQ群: 897273171");
        Bukkit.getPluginCommand("CowReinforce").setExecutor(new Command());
        Bukkit.getPluginManager().registerEvents(new JoinEvent(), this);
        Bukkit.getPluginManager().registerEvents(new GuiEvent(), this);
        getinstance().saveDefaultConfig();
        getinstance().reloadConfig();
        Item.loadConfig();
        Item.load();
        Reinforce.loadConfig();
        Reinforce.load();
        Gui.loadConfig();
        Gui.load();
        SpecialItem.loadConfig();
        SpecialItem.load();
    }

    @Override
    public void onDisable() {
        Tool.sendMessage(Bukkit.getConsoleSender(),"&cCowReinforce");
        Tool.sendMessage(Bukkit.getConsoleSender(),"&c  关闭成功");
        Tool.sendMessage(Bukkit.getConsoleSender(),"&aVersion: " + version);
        Tool.sendMessage(Bukkit.getConsoleSender(),"&cAuthor: TheSuperCow");
        Tool.sendMessage(Bukkit.getConsoleSender(),"&cQ群: 897273171");
    }
    public static JavaPlugin getinstance() {
        return instance;
    }

}
