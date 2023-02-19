package saber.mcmmochatcolor;

import org.bukkit.plugin.java.JavaPlugin;
import saber.mcmmochatcolor.commands.CustomPCColor;
import saber.mcmmochatcolor.events.PartyChatEvent;

import java.util.logging.Level;

public final class McMMOChatColor extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        //Load Config
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        // Register listener
        getServer().getPluginManager().registerEvents(new PartyChatEvent(this), this);

        // Register Command
        getCommand("custompccolor").setExecutor(new CustomPCColor(this));

        // Log successful launch
        this.getLogger().log(Level.INFO, "McMMO Chat Color loaded Successfully");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
