package de.stylextv.ultimateheads.player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerEntry {
	
	private UUID uuid;
	private List<String> favorites;
	
	public PlayerEntry(UUID uuid) {
		this(uuid, new ArrayList<String>());
	}
	public PlayerEntry(UUID uuid, List<String> favorites) {
		this.uuid = uuid;
		this.favorites = favorites;
	}
	
	public void toggleFavorite(String url) {
		if(favorites.contains(url)) removeFavorite(url);
		else addFavorite(url);
	}
	public void addFavorite(String url) {
		favorites.add(url);
	}
	public void removeFavorite(String url) {
		favorites.remove(url);
	}
	
	public UUID getUUID() {
		return uuid;
	}
	public List<String> getFavorites() {
		return favorites;
	}
	
}
