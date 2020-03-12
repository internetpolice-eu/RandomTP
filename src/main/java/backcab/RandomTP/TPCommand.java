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
        if ((args.length == 1) && (args[0].equalsIgnoreCase("reload")) && (this.plugin.checkPermission(sender, "randomtp.reload", "You do not have permission to use this command."))){
            this.plugin.config().reload();
            sender.sendMessage(ChatColor.GREEN + "RandomTP reloaded.");
            return true;
        }

        if ((args.length == 0) && (this.plugin.checkPermission(sender, "randomtp.tp", "You do not have permission to use this command."))){
            if ((sender instanceof Player)){
                this.plugin.file(sender.getName() + ": used /rtp");
                tp.start(this.plugin, (Player)sender, TeleportType.SELF);
            }
            else{
                sender.sendMessage("/rtp <player>");
            }
            return true;
        }

        if ((args.length == 1) && (this.plugin.checkPermission(sender, "randomtp.other", "You do not have permission to use this command."))){
            Player p = plugin.getServer().getPlayer(args[0]);
            if (p == null){
                sender.sendMessage(ChatColor.RED + "Invalid player. Player is either offline or does not exist");
                return true;
            }
            this.plugin.file(sender.getName() + ": used /rtp " + p.getName());
            tp.start(this.plugin, p, TeleportType.CMD);

            return true;
        }

        return false;
    }
}
