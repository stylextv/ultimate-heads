package de.stylextv.ultimateheads.main;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.stylextv.ultimateheads.command.CommandListener;
import de.stylextv.ultimateheads.command.MainTabCompleter;
import de.stylextv.ultimateheads.config.ConfigManager;
import de.stylextv.ultimateheads.event.EventChat;
import de.stylextv.ultimateheads.event.EventGui;
import de.stylextv.ultimateheads.event.EventPlayerCommand;
import de.stylextv.ultimateheads.event.EventPlayerJoinQuit;
import de.stylextv.ultimateheads.gui.GuiManager;
import de.stylextv.ultimateheads.head.HeadManager;
import de.stylextv.ultimateheads.service.AutoUpdater;
import de.stylextv.ultimateheads.service.Metrics;
import de.stylextv.ultimateheads.storage.DataManager;
import de.stylextv.ultimateheads.version.VersionUtil;

public class Main extends JavaPlugin {
	
	private static Main plugin;
	public static Main getPlugin() {
		return plugin;
	}
	
	private AutoUpdater autoUpdater;
	
	@Override
	public void onEnable() {
		register();
		
		VersionUtil.onEnable();
		HeadManager.loadHeads();
		Variables.loadScheme();
		DataManager.onEnable();
		HeadManager.addPlayersToLocalHeads();
		
		enableMetrics();
		enableAutoUpdater();
	}
	private void register() {
		plugin=this;
		Variables.AUTHOR=plugin.getDescription().getAuthors().get(0);
		Variables.VERSION=plugin.getDescription().getVersion();
		
		ConfigManager.onEnable();
		
		MainTabCompleter tabCompleter=new MainTabCompleter();
		registerCommand("uh", tabCompleter);
		registerCommand("heads", tabCompleter);
		registerCommand("ultimateheads", tabCompleter);
		
		PluginManager pm=Bukkit.getPluginManager();
		pm.registerEvents(new EventPlayerJoinQuit(), plugin);
		pm.registerEvents(new EventGui(), plugin);
		pm.registerEvents(new EventChat(), plugin);
		pm.registerEvents(new EventPlayerCommand(), plugin);
	}
	private void registerCommand(String name, MainTabCompleter tabCompleter) {
		PluginCommand c = getCommand(name);
		c.setExecutor(new CommandListener(name));
		c.setTabCompleter(tabCompleter);
	}
	private void enableMetrics() {
		new Metrics(this);
	}
	private void enableAutoUpdater() {
		autoUpdater = new AutoUpdater(plugin);
		autoUpdater.checkAutoUpdater();
	}
	
	@Override
	public void onDisable() {
		GuiManager.onDisable();
		DataManager.onDisable();
		autoUpdater.startAutoUpdater();
	}
	
	public void runAutoUpdater(Player p) {
		autoUpdater.runAutoUpdater(p);
	}
	
	public File getPluginFile() {
		return getFile();
	}
	
}
