package de.stylextv.ultimateheads.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;

import de.stylextv.ultimateheads.config.ConfigManager;
import de.stylextv.ultimateheads.config.ConfigValue;
import de.stylextv.ultimateheads.lang.LanguageManager;
import de.stylextv.ultimateheads.permission.PermissionUtil;
import de.stylextv.ultimateheads.util.ItemUtil;

public class ConfigMenu extends StaticMenu {
	
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
		
		setItem(4, 1, ItemUtil.CONFIG_DEFAULT);
//		setPlusMinusButtons(2, 2, ConfigManager.VALUE_VIEW_DISTANCE);
//		setPlusMinusButtons(6, 2, ConfigManager.VALUE_MAP_SENDS_PER_3TICKS);
//		setPlusMinusButtons(2, 3, ConfigManager.VALUE_RESERVED_VANILLA_MAPS);
		setPlusMinusButtons(4, 2, ConfigManager.VALUE_LANGUAGE);
	}
	private void setPlusMinusButtons(int x, int y, ConfigValue<?> value) {
		setItem(x-1, y, value.getLeftButton());
		setItem(x+1, y, value.getRightButton());
	}
	
	@Override
	public String getTitle() {
		return LanguageManager.parseMsg("gui.title.config");
	}
	
	@Override
	public void updateDynamicContent() {
//		setItem(2, 2, ConfigManager.VALUE_VIEW_DISTANCE.getItemStack());
//		setItem(6, 2, ConfigManager.VALUE_MAP_SENDS_PER_3TICKS.getItemStack());
//		setItem(2, 3, ConfigManager.VALUE_RESERVED_VANILLA_MAPS.getItemStack());
		setItem(4, 2, ConfigManager.VALUE_LANGUAGE.getItemStack());
	}
	
	@Override
	public void onClick(Player p, InventoryClickEvent e) {
		int slot=e.getSlot();
		if(slot==13) {
			if(PermissionUtil.hasConfigPermission(p) && PermissionUtil.hasGuiPermission(p)) {
				playClickSound(p, true);
				ConfigManager.restoreDefaultConfig();
			} else {
				kickPlayerForNoPerm(p);
			}
		} else if(slot == getLastY()*9+4) {
			if(PermissionUtil.hasGuiPermission(p)) {
				playClickSound(p, true);
				kickPlayerToSettingsMenu(p);
			} else {
				kickPlayerForNoPerm(p);
			}
		} else {
//			if(handleButtonPresses(p, slot, e, ConfigManager.VALUE_VIEW_DISTANCE, 2*9+2)) return;
//			if(handleButtonPresses(p, slot, e, ConfigManager.VALUE_MAP_SENDS_PER_3TICKS, 2*9+6)) return;
//			if(handleButtonPresses(p, slot, e, ConfigManager.VALUE_RESERVED_VANILLA_MAPS, 3*9+2)) return;
			if(handleButtonPresses(p, slot, e, ConfigManager.VALUE_LANGUAGE, 2*9+4)) return;
		}
	}
	private boolean handleButtonPresses(Player p, int slot, InventoryClickEvent e, ConfigValue<?> value, int valueSlot) {
		if(Math.abs(valueSlot-slot) == 1) {
			if(PermissionUtil.hasConfigPermission(p) && PermissionUtil.hasGuiPermission(p)) {
				int dir = slot - valueSlot;
				boolean shift = e.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY);
				
				playClickSound(p, value.handleButtonPress(dir, shift));
				
			} else {
				kickPlayerForNoPerm(p);
			}
		}
		return false;
	}
	
	public void kickPlayerToSettingsMenu(Player p) {
		GuiManager.openSettingsMenu(p);
	}
	
}
