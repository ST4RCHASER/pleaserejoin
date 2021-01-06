package me.starchaser.bungee.pleaserejoin;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class YouTube_9MLN4_WvfX0 extends Plugin implements Listener {
    public static ArrayList<String> player_whitelist;
    public static String sv_path,kick_message;
    public static Configuration configuration;
    public static HashMap<String , Long> connection_time;
    public static HashMap<String , Integer> connection_time_point;
    @Override
    public void onEnable() {
        sv_path = getDataFolder().getAbsoluteFile().getParentFile().getParentFile().getAbsolutePath() + File.separator;
        player_whitelist = new ArrayList<>();
        connection_time = new HashMap<>();
        connection_time_point = new HashMap<>();
        ProxyServer.getInstance().getConsole().sendMessage("§a################");
        ProxyServer.getInstance().getConsole().sendMessage("§bPlease Rejoin | No Bot");
        ProxyServer.getInstance().getConsole().sendMessage("§eBy _StarChaser");
        ProxyServer.getInstance().getConsole().sendMessage("§a################");
        getProxy().getPluginManager().registerListener(this, this);
        File f = new File(sv_path+File.separator+"pleaserejoin_config.yml");
        if(f.exists() && !f.isDirectory()) {
            try {
                configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(f);
                String str = configuration.getString("kick_message");
                if (str == null) {
                    kick_message = "&aลองเข้าเซิร์ฟใหม่อีกครั้ง";
                } else {
                    kick_message = str;
                    ProxyServer.getInstance().getConsole().sendMessage("§7PleaseReJoin: §bUsing custom message in config file...");
                }
            }catch (Exception ee) {
                kick_message = "&aลองเข้าเซิร์ฟใหม่อีกครั้ง";
            }
        } else {
            kick_message = "&aลองเข้าเซิร์ฟใหม่อีกครั้ง";
        }
        ProxyServer.getInstance().getConsole().sendMessage("§7PleaseReJoin: §aOK Plugin enabled!");
        clearLists();
    }
    public void clearLists() {
        getProxy().getScheduler().schedule(this, new Runnable() {
            @Override
            public void run() {
                connection_time.clear();
                connection_time_point.clear();
                player_whitelist.clear();
                ProxyServer.getInstance().getConsole().sendMessage("§7PleaseReJoin: §aSeason Clear!");
            }
        }, 60, 60, TimeUnit.MINUTES);
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onConnect(PreLoginEvent preLoginEvent) {
        String ip = preLoginEvent.getConnection().getAddress().getHostName();
        String name = preLoginEvent.getConnection().getName();
        int aa = 0;
        for (ProxiedPlayer player : getProxy ().getPlayers()) {
            if (ip == player.getAddress().getHostString()) {
                aa++;
            }
        }
        if (aa>=3) {
            ProxyServer.getInstance().getConsole().sendMessage("§7PleaseReJoin: §cDeny player > " + name + " for ip > " + ip + " count > " + aa);
            preLoginEvent.setCancelReason(new TextComponent("ไม่สามารถเข้าเซิร์ฟได้หลาย account พร้อมกันโปรดออกตัวอื่นก่อนและค่อยเข้านะ ".replaceAll("&" , "§")));
            preLoginEvent.setCancelled(true);
            return;
        }
        if (name == null) {
            ProxyServer.getInstance().getConsole().sendMessage("§7PleaseReJoin: §cERROR: name is NULL");
        }
        if (player_whitelist == null) {
            ProxyServer.getInstance().getConsole().sendMessage("§7PleaseReJoin: §cERROR: ARRAY is NULL");
        }
        if (!player_whitelist.contains(name)) {
            preLoginEvent.setCancelReason(new TextComponent(kick_message.replaceAll("&" , "§")));
            if (player_whitelist.size() > 5000) player_whitelist.clear();
            player_whitelist.add(name);
            ProxyServer.getInstance().getConsole().sendMessage("§7PleaseReJoin: §aKick first time for player: " + name + " Hostname: " + ip);
            preLoginEvent.setCancelled(true);
        }
    }
}
