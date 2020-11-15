package de.stylextv.ultimateheads.storage;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import de.stylextv.ultimateheads.head.Head;
import de.stylextv.ultimateheads.head.HeadManager;
import de.stylextv.ultimateheads.player.PlayerEntry;
import de.stylextv.ultimateheads.player.PlayerManager;

public class DataManager {
	
	private static final File DATA_FILE = new File("plugins/UltimateHeads/data.yml");
	
	public static void onEnable() {
		if(DATA_FILE.exists()) {
			readData();
		}
	}
	public static void onDisable() {
		writeData();
	}
	
	private static void readData() {
		try {
			
			YamlConfiguration config = new YamlConfiguration();
			config.load(DATA_FILE);
			
			//read player data
			if(config.isSet("data.players")) {
				Set<String> list = config.getConfigurationSection("data.players").getKeys(false);
				for(String s:list) {
					UUID uuid = UUID.fromString(s);
					PlayerManager.addPlayer(uuid, config.getStringList("data.players."+s+".favorites"));
				}
			}
			//read local heads
			if(config.isSet("data.local")) {
				Set<String> list = config.getConfigurationSection("data.local").getKeys(false);
				for(String s:list) {
					int id = Integer.parseInt(s);
					String name = config.getString("data.local."+s+".name");
					String url = config.getString("data.local."+s+".url");
					String category = config.getString("data.local."+s+".category");
					HeadManager.addLocalHead(new Head(id, name, url, null, false, true, HeadManager.getOrAddCategory(category)));
				}
				HeadManager.sortLocalHeadIds();
			}
			
		} catch (InvalidConfigurationException | IOException ex) {
			ex.printStackTrace();
		}
	}
	private static void writeData() {
		try {
			
			YamlConfiguration config = new YamlConfiguration();
			
			//save player data
			for(PlayerEntry e:PlayerManager.getPlayers().values()) {
				if(!e.getFavorites().isEmpty()) {
					String uuid = e.getUUID().toString();
					config.set("data.players."+uuid+".favorites", e.getFavorites());
				}
			}
			//save local heads
			for(Head h:HeadManager.getLocalHeads()) {
				int id = h.getId();
				config.set("data.local."+id+".name", h.getName());
				config.set("data.local."+id+".url", h.getUrl());
				config.set("data.local."+id+".category", h.getCategory().getName());
			}
			
			config.save(DATA_FILE);
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
}
