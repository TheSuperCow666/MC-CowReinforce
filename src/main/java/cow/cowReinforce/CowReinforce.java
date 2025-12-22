package cow.cowReinforce;

import com.fileTool.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class CowReinforce extends JavaPlugin {

    private static JavaPlugin instance;
    public static Economy econ = null;
    public static double version = 1.16;

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
        Bukkit.getPluginManager().registerEvents(new ReinforceGuiEvent(), this);
        Bukkit.getPluginManager().registerEvents(new InheritanceGuiEvent(), this);
        getinstance().saveDefaultConfig();
        getinstance().reloadConfig();
        Item.loadConfig();
        Item.load();
        Reinforce.loadConfig();
        Reinforce.load();
        Gui.loadConfig();
        Gui.load();
        Gui2.loadConfig();
        Gui2.load();
        SpecialItem.loadConfig();
        SpecialItem.load();
        Inheritance.loadConfig();
        Inheritance.load();

        if(getServer().getPluginManager().getPlugin("NBTAPI") == null){
            Tool.sendMessage(Bukkit.getConsoleSender(),"&c未检测到需要插件NBTAPI");

        }
        if (setupEconomy()) {
            Tool.sendMessage(Bukkit.getConsoleSender(),"&a检测到可选前置 &eVault &a插件");
        }

    }
    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        econ = rsp.getProvider();
        return econ != null;
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
