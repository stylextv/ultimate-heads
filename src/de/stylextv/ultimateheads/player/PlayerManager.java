package de.stylextv.ultimateheads.player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

public class PlayerManager {
	
	private static final HashMap<UUID, PlayerEntry> PLAYERS = new HashMap<UUID, PlayerEntry>();
	
	public static PlayerEntry getPlayerEntry(Player p) {
		UUID uuid = p.getUniqueId();
		PlayerEntry e=PLAYERS.get(uuid);
		if(e == null) PLAYERS.put(uuid, e=new PlayerEntry(p.getUniqueId()));
		return e;
	}
	public static List<String> getFavorites(Player p) {
		return getPlayerEntry(p).getFavorites();
	}
	
	public static void addPlayer(UUID uuid, List<String> favorites) {
		PLAYERS.put(uuid, new PlayerEntry(uuid, favorites));
	}
	
	public static HashMap<UUID, PlayerEntry> getPlayers() {
		return PLAYERS;
	}
	
}
