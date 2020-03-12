package backcab.RandomTP;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Events implements Listener {
    private RandomTP rtp;

    protected Events(RandomTP rtp){
        this.rtp = rtp;
    }

    @EventHandler
    protected void logoff(PlayerQuitEvent event){
        Task.cancel(event.getPlayer().getUniqueId());
    }

    @EventHandler
    protected void signClick(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        Block b = event.getClickedBlock();
        if ((!b.getType().equals(Material.OAK_SIGN)) && (!b.getType().equals(Material.OAK_WALL_SIGN))) {
            return;
        }
        Sign s = (Sign)b.getState();

        String line = ChatColor.stripColor(s.getLine(0));
        if (!line.equalsIgnoreCase("[randomtp]")) {
            return;
        }
        if (!this.rtp.checkPermission(p, "randomtp.sign.use", "You do not have permisson to use this sign.")) {
            return;
        }
        this.rtp.file(p.getName() + ": successfully used sign");

        new PreTP().start(this.rtp, p, TeleportType.SIGN);
    }

    @EventHandler
    protected void makeSign(SignChangeEvent event) {
        String line = event.getLine(0);
        line = ChatColor.stripColor(line);

        Player p = event.getPlayer();
        if (!line.equalsIgnoreCase("[randomtp]")){
            this.rtp.file(p.getName() + ": " + line);
            return;
        }
        if (!this.rtp.checkPermission(p, "randomtp.sign.make", null)) {
            return;
        }
        this.rtp.file(p.getName() + ": successfully made sign");

        event.setLine(0, ChatColor.DARK_BLUE + "[RandomTP]");
    }
}
