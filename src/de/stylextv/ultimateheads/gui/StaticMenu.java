package de.stylextv.ultimateheads.gui;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import de.stylextv.ultimateheads.util.AsyncUtil;

public abstract class StaticMenu extends Menu {
	
	private ArrayList<Player> viewers = new ArrayList<Player>();
	
	public abstract String getTitle();
	
	public void updateTitle() {
		setTitle(getTitle());
		AsyncUtil.runSync(() -> {
			for(Player p:viewers) {
				super.openFor(p);
			}
		});
	}
	
	@Override
	protected void openFor(Player p) {
		viewers.add(p);
		super.openFor(p);
	}
	
	@Override
	public void onClose(Player p) {
		viewers.remove(p);
	}
	
}
