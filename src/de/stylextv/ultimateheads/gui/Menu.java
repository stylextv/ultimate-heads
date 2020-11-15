package de.stylextv.ultimateheads.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.stylextv.ultimateheads.command.CommandHandler;
import de.stylextv.ultimateheads.util.AsyncUtil;
import de.stylextv.ultimateheads.version.VersionUtil;

public abstract class Menu {
	
	protected Inventory inv;
	
	public void create() {
		createInventory();
		fillConstants();
		updateDynamicContent();
	}
	
	public abstract void createInventory();
	public abstract void fillConstants();
	
	public abstract void updateDynamicContent();
	
	public abstract void onClick(Player p, InventoryClickEvent e);
	public abstract void onClose(Player p);
	
	protected void openFor(Player p) {
		AsyncUtil.runSync(() -> {
			if(!p.getOpenInventory().getTopInventory().equals(inv)) {
				if(p.getItemOnCursor() != null && !p.getItemOnCursor().getType().equals(Material.AIR)) {
					CommandHandler.giveItem(p, p.getItemOnCursor());
					p.setItemOnCursor(null);
				}
				p.openInventory(inv);
			}
		});
	}
	public void setItem(int x, int y, ItemStack item) {
		inv.setItem(y*9+x, item);
	}
	public void setItem(int index, ItemStack item) {
		inv.setItem(index, item);
	}
	public void setTitle(String title) {
		inv = Bukkit.createInventory(null, inv.getSize(), title);
		fillConstants();
		updateDynamicContent();
	}
	public int getLastY() {
		return inv.getSize()/9-1;
	}
	
	protected static void playClickSound(Player p, boolean success) {
		if(success) {
			if(VersionUtil.getMcVersion()<=VersionUtil.MC_1_8) p.playSound(p.getLocation(), "gui.button.press", 1,2);
			else if(VersionUtil.getMcVersion()<=VersionUtil.MC_1_10) p.playSound(p.getLocation(), "minecraft:block.stone_button.click_off", 1,2);
			else if(VersionUtil.getMcVersion()<=VersionUtil.MC_1_12) p.playSound(p.getLocation(), "minecraft:block.stone_button.click_off", SoundCategory.AMBIENT, 1,2);
			else p.playSound(p.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_OFF, SoundCategory.AMBIENT, 1,2);
		} else {
			if(VersionUtil.getMcVersion()<=VersionUtil.MC_1_8) p.playSound(p.getLocation(), "gui.button.press", 0.5f,0.75f*2);
			else if(VersionUtil.getMcVersion()<=VersionUtil.MC_1_10) p.playSound(p.getLocation(), "minecraft:block.wood_button.click_off", 1,2);
			else if(VersionUtil.getMcVersion()<=VersionUtil.MC_1_12) p.playSound(p.getLocation(), "minecraft:block.wood_button.click_off", SoundCategory.AMBIENT, 1,2);
			else p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_OFF, SoundCategory.AMBIENT, 1,2);
		}
	}
	protected static void kickPlayerForNoPerm(Player p) {
		playClickSound(p, false);
		closeInventory(p);
		CommandHandler.sendNoPermission(p);
	}
	
	public Inventory getInventory() {
		return inv;
	}
	
	public static void closeInventory(Player p) {
		AsyncUtil.runSync(() -> {
			if(p.getItemOnCursor() != null && !p.getItemOnCursor().getType().equals(Material.AIR)) {
				CommandHandler.giveItem(p, p.getItemOnCursor());
				p.setItemOnCursor(null);
			}
			p.closeInventory();
		});
	}
	
}
