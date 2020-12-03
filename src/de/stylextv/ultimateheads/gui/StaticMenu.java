package de.stylextv.ultimateheads.gui;

import java.util.Collection;
import java.util.HashMap;

import org.bukkit.entity.Player;

import de.stylextv.ultimateheads.util.AsyncUtil;

public abstract class StaticMenu extends Menu {
	
	private HashMap<Player, MainMenu> viewers = new HashMap<Player, MainMenu>();
	
	public abstract String getTitle();
	
	public void updateTitle() {
		setTitle(getTitle());
		AsyncUtil.runSync(() -> {
			for(Player p:viewers.keySet()) {
				super.openFor(p);
			}
		});
	}
	
	public void openFor(Player p, MainMenu menu) {
		viewers.put(p, menu);
		super.openFor(p);
	}
	
	@Override
	public void onClose(Player p) {
		viewers.remove(p);
	}
	
	protected MainMenu getPlayerMainMenu(Player p) {
		return viewers.get(p);
	}
	public Collection<MainMenu> getAllMainMenus() {
		return viewers.values();
	}
	
}
