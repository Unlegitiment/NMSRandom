package me.unlegitiment.nmsrandom;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class NMSRandom extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
       getServer().getPluginManager().registerEvents(this,this);
        getCommand("createnpc").setExecutor(this::onCommand);
    }
    @EventHandler
    private void onJoin(PlayerJoinEvent e){
        if(NPC.getNPC() == null) return;
        if(NPC.getNPC().isEmpty()) return;
        NPC.onJoinPacketUpdate(e.getPlayer());
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("createnpc")){
            if(!(sender instanceof Player)){
                return false;
            }
            Player player = (Player) sender;
            NPC.createNPC(player);
            player.sendMessage(String.valueOf(NPC.getNPC()));
            return true;
        }
        return true;
    }
}
