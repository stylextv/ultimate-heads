package de.stylextv.ultimateheads.gui;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import de.stylextv.ultimateheads.util.AsyncUtil;

public class GuiManager {
	
	private static MainMenu mainMenu;
	private static SettingsMenu settingsMenu;
	private static ConfigMenu configMenu;
	
	private static ConcurrentHashMap<Player, Menu> openedMenues = new ConcurrentHashMap<Player, Menu>();
	
	public static void openSettingsMenu(Player p) {
		if(settingsMenu == null) {
			settingsMenu = new SettingsMenu();
			settingsMenu.create();
		}
		
		setOpenedMenu(p, settingsMenu);
		settingsMenu.openFor(p);
	}
	public static void openConfigMenu(Player p) {
		if(configMenu == null) {
			configMenu = new ConfigMenu();
			configMenu.create();
		}
		
		setOpenedMenu(p, configMenu);
		configMenu.openFor(p);
	}
	public static void openMainMenu(Player p) {
		if(mainMenu == null) {
			mainMenu = new MainMenu();
			mainMenu.create();
		}
		
		setOpenedMenu(p, mainMenu);
		mainMenu.openForViewer(p);
	}
	public static void openHeadsListMenu(Player p, HeadListMenu list) {
		list.create();
		setOpenedMenu(p, list);
		list.openFor(p);
	}
	public static void setOpenedMenu(Player p, Menu m) {
		Menu closing = openedMenues.get(p);
		if(closing != null && !closing.equals(m)) {
			closing.onClose(p);
		}
		if(p.isOnline()) openedMenues.put(p, m);
	}
	
	public static void updateTranslations() {
		if(settingsMenu != null) settingsMenu.updateTitle();
		if(configMenu != null) configMenu.updateTitle();
		if(mainMenu != null) mainMenu.updateTitle();
		for(Menu m:openedMenues.values()) {
			if(m instanceof HeadListMenu) ((HeadListMenu)m).updateTitle();
		}
	}
	
	public static void onClick(InventoryClickEvent e) {
		if(e.getClickedInventory() != null && e.getWhoClicked() instanceof Player) {
			Player p=(Player) e.getWhoClicked();
			Menu menu=openedMenues.get(p);
			if(menu!=null) {
				if(e.getClickedInventory().equals(e.getView().getTopInventory())) {
					e.setCancelled(true);
					AsyncUtil.runAsync(() -> menu.onClick(p, e));
				}
			}
		}
	}
	public static void onClose(InventoryCloseEvent e) {
		if(e.getPlayer() instanceof Player) {
			Player p=(Player) e.getPlayer();
			Menu menu=openedMenues.get(p);
			if(menu!=null && e.getView().getTopInventory().equals(menu.getInventory())) {
				menu.onClose(p);
				openedMenues.remove(p);
			}
		}
	}
	
	public static void updateMainMenu() {
		if(mainMenu != null) {
			mainMenu.updateTitle();
		}
	}
	
	public static void onDisable() {
		for(Player p:openedMenues.keySet()) {
			openedMenues.get(p).onClose(p);
			p.closeInventory();
		}
	}
	
	public static ConfigMenu getConfigMenu() {
		return configMenu;
	}
	
}
