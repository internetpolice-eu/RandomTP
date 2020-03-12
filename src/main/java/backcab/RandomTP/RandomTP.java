package backcab.RandomTP;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class RandomTP extends JavaPlugin{
    private PluginFile config;

    public void onEnable() {
        this.config = new PluginFile(this, "config", true);
        Task.init(this);

        Bukkit.getPluginManager().registerEvents(new Events(this), this);
        getCommand("randomtp").setExecutor(new TPCommand(this));
    }

    protected void log(Level level, String message){
        getLogger().log(level, message);
    }

    protected void file(String message) {
        if (!this.config.getConfig().getBoolean("debug")) {
            return;
        }
        File debug = new File(getDataFolder(), "debug.log");
        if (!debug.exists()) {
            try{
                debug.createNewFile();
            }
            catch (IOException localIOException) {}
        }
        try{
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(debug, true)));
            out.println(message);
            out.close();
        }
        catch (IOException localIOException1) {}
    }

    protected boolean checkPermission(CommandSender sender, String perm, String message) {
        if (!sender.hasPermission(perm)){
            file(sender.getName() + " does not have " + perm);
            if (message != null) {
                sender.sendMessage(ChatColor.RED + message);
            }
            return false;
        }
        return true;
    }

    protected PluginFile config(){
        return this.config;
    }
}
