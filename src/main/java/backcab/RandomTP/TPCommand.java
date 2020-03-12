package backcab.RandomTP;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPCommand implements CommandExecutor {
    private RandomTP plugin;

    protected TPCommand(RandomTP rtp){
        this.plugin = rtp;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args){
        PreTP tp = new PreTP();
        if ((args.length == 1) && (args[0].equalsIgnoreCase("reload")) && (plugin.checkPermission(sender, "randomtp.reload", "You do not have permission to use this command."))) {
            plugin.config().reload();
            sender.sendMessage(ChatColor.GREEN + "RandomTP reloaded.");
            return true;
        }

        if ((args.length == 0) && (plugin.checkPermission(sender, "randomtp.tp", "You do not have permission to use this command."))) {
            if ((sender instanceof Player)) {
                tp.start(plugin, (Player)sender, TeleportType.SELF);
            }
            else {
                sender.sendMessage("/rtp <player>");
            }
            return true;
        }

        if ((args.length == 1) && (plugin.checkPermission(sender, "randomtp.other", "You do not have permission to use this command."))) {
            Player player = plugin.getServer().getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Invalid player. Player is either offline or does not exist");
                return true;
            }
            tp.start(plugin, player, TeleportType.CMD);

            return true;
        }

        return false;
    }
}
