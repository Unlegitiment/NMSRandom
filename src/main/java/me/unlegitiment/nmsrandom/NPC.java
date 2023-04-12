package me.unlegitiment.nmsrandom;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_19_R2.CraftServer;
import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NPC {
    private static final List<ServerPlayer> NPC = new ArrayList<>();
    public static void createNPC(Player player){
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        ServerLevel level = ((CraftWorld) Bukkit.getWorld(player.getWorld().getName())).getHandle();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), ChatColor.RED +"Test");
        ServerPlayer npc = new ServerPlayer(server, level, gameProfile);
        npc.setPos(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
        addNPCPacket(npc);
        NPC.add(npc);
    }
    public static void addNPCPacket(ServerPlayer npc){
        for(Player player : Bukkit.getOnlinePlayers()){
            ServerPlayer nmsPlayer  = ((CraftPlayer)player).getHandle();
            ServerGamePacketListenerImpl playerconnection = ((CraftPlayer) player).getHandle().connection;
            playerconnection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, npc));
            playerconnection.send(new ClientboundAddPlayerPacket(npc));
            playerconnection.send(new ClientboundRotateHeadPacket(npc, (byte) (nmsPlayer.getYHeadRot() * 256/360)));

        }
    }
    public static void onJoinPacketUpdate(Player player){
        ServerPlayer nmsPlayer = ((CraftPlayer)player).getHandle();
        for(ServerPlayer npc : NPC) {
            nmsPlayer.connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, npc));
            nmsPlayer.connection.send(new ClientboundAddPlayerPacket(npc));
            nmsPlayer.connection.send(new ClientboundRotateHeadPacket(npc, (byte) (npc.getYHeadRot() * 256 / 360)));
        }
    }

    public static List<ServerPlayer> getNPC() {
        return NPC;
    }
}
