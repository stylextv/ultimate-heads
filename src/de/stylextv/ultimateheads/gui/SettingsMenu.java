package de.stylextv.ultimateheads.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import de.stylextv.ultimateheads.lang.LanguageManager;
import de.stylextv.ultimateheads.main.Main;
import de.stylextv.ultimateheads.permission.PermissionUtil;
import de.stylextv.ultimateheads.util.ItemUtil;

public class SettingsMenu extends StaticMenu {
	
	@Override
	public void createInventory() {
		inv = Bukkit.createInventory(null, 9*5, getTitle());
	}
	@Override
	public void fillConstants() {
		for(int x=0; x<9; x++) {
			setItem(x, 0, ItemUtil.BLANK);
			if(x!=4) setItem(x, getLastY(), ItemUtil.BLANK);
		}
		
		setItem(4, getLastY(), ItemUtil.HEAD_BACK);
		
		setItem(2, 2, ItemUtil.SETTINGS_CONFIG);
		setItem(6, 2, ItemUtil.SETTINGS_UPDATE);
	}
	
	@Override
	public String getTitle() {
		return LanguageManager.parseMsg("gui.title.settings");
	}
	
	@Override
	public void updateDynamicContent() {
	}
	
	@Override
	public void onClick(Player p, InventoryClickEvent e) {
		int slot=e.getSlot();
		if(slot==20) {
			if(PermissionUtil.hasGuiPermission(p) && PermissionUtil.hasConfigPermission(p)) {
				playClickSound(p, true);
				GuiManager.openConfigMenu(p);
			} else {
				kickPlayerForNoPerm(p);
			}
		} else if(slot==24) {
			if(PermissionUtil.hasUpdatePermission(p)) {
				playClickSound(p, true);
				closeInventory(p);
				Main.getPlugin().runAutoUpdater(p);
			} else {
				kickPlayerForNoPerm(p);
			}
		} else if(slot == getLastY()*9+4) {
			if(PermissionUtil.hasGuiPermission(p)) {
				playClickSound(p, true);
				kickPlayerToMainMenu(p);
			} else {
				kickPlayerForNoPerm(p);
			}
		}
	}
	
	public void kickPlayerToMainMenu(Player p) {
		GuiManager.openMainMenu(p, 0);
	}
	
}
