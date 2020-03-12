package backcab.RandomTP;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PreTP {
    private RandomTP rtp;
    private PluginFile config;

    protected void start(RandomTP rtp, Player p, TeleportType type) {
        this.rtp = rtp;
        this.config = rtp.config();
        if ((type.equals(TeleportType.SELF)) && (!validWorld(p.getWorld().getName()))){
            rtp.file(p.getName() + ": invalid world: " + p.getWorld().getName());
            p.sendMessage(ChatColor.RED + "RandomTP is not available in this world");
            return;
        }
        if ((type.equals(TeleportType.SELF)) && (!validPosition(p.getLocation(), p.isFlying()))){
            rtp.file(p.getName() + ": invalid location");
            p.sendMessage(ChatColor.RED + "Cannot teleport from this location. Please be sure you are not falling, jumping, or swimming");
            return;
        }
        sendTP(type, p.getUniqueId());
    }

    private boolean validWorld(String world) {
        List<String> worlds = this.config.getConfig().getStringList("valid_worlds");
        for (int i = 0; i < worlds.size(); i++) {
            if ((worlds.get(i)).startsWith("$")) {
                worlds.set(i, (worlds.get(i)).substring(1));
            }
        }
        this.rtp.file("valid worlds: " + worlds);
        this.rtp.file("current world: " + world);
        if (worlds.contains(world)) {
            return true;
        }
        return false;
    }

    private boolean validPosition(Location loc, boolean flying) {
        if ((this.config.getConfig().getBoolean("anticheat")) && (
                (loc.getBlock().getType().equals(Material.LAVA)) ||
                        (loc.getBlock().getType().equals(Material.WATER)) || (
                        (loc.subtract(0.0D, 1.0D, 0.0D).getBlock().getType().equals(Material.AIR)) && (!flying)))) {
            return false;
        }
        return true;
    }

    private void sendTP(TeleportType type, UUID uuid){
        boolean rand = (Boolean) parse("random_world", Boolean.FALSE, "Invalid value for random_world. Defaulting to false.");
        List<String> worlds = this.config.getConfig().getStringList("valid_worlds");

        int maxX = (Integer) parse("radius.max_X", 1000, "Invalid value for max_X. Defaulting to 1000");
        int maxZ = (Integer) parse("radius.max_Z", 1000, "Invalid value for max_Z. Defaulting to 1000");
        int minX = (Integer) parse("radius.min_X", 0, "Invalid value for min_X. Defaulting to 0");
        int minZ = (Integer) parse("radius.min_Z", 0, "Invalid value for min_Z. Defaulting to 0");

        boolean message = (Boolean) parse("send_message_on_tp", false, "Invalid value for send_message_on_tp");

        int cooldown = (Integer) parse("cooldown", 0, "Invalid value for cooldown. Defaulting to 0");

        String section = type.toString().toLowerCase();
        boolean cooldownEnabled = (Boolean) parse(section + ".cooldown", false, "Invalid value for " + section + ".cooldown. Defaulting to false");

        List<String> biomes = this.config.getConfig().getStringList("biomes");
        List<String> blocks = this.config.getConfig().getStringList("blocks");

        boolean usingWG = (Boolean) parse("worldguard", false, "Invalid value for worldguard. Defaulting to false.");
        boolean usingWB = (Boolean) parse("worldborder", false, "Invalid value for worldborder. Defaulting to false.");

        Task t = new Task(rand, worlds, maxX, maxZ, minX, minZ, message, cooldown, cooldownEnabled, biomes, blocks, uuid, usingWG, usingWB);

        int id = Bukkit.getScheduler().runTaskTimer(this.rtp, t, 0L, 1L).getTaskId();

        t.setID(id);
    }

    private Object parse(String s, Object o, String warning){
        String thing = this.config.getConfig().getString(s);
        try{
            Integer i = Integer.parseInt(thing);
            if ((o instanceof Integer)) {
                return i;
            }
        }
        catch (Exception localException){
            try{
                Double d = Double.parseDouble(thing);
                if ((o instanceof Double)) {
                    return d;
                }
            }
            catch (Exception localException1){
                if (thing.equalsIgnoreCase("true")) {
                    return true;
                }
                if (thing.equalsIgnoreCase("false")) {
                    return false;
                }
                this.rtp.log(Level.SEVERE, warning);
                this.rtp.file(warning);

                this.config.getConfig().set(s, o);
                try{
                    this.config.save();
                }
                catch (IOException localIOException) {}
            }
        }
        return o;
    }
}
