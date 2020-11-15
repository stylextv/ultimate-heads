package de.stylextv.ultimateheads.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import de.stylextv.ultimateheads.gui.GuiManager;

public class EventGui implements Listener {
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		GuiManager.onClick(e);
	}
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		GuiManager.onClose(e);
	}
	
}
