package de.stylextv.ultimateheads.gui;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import de.stylextv.ultimateheads.head.Category;
import de.stylextv.ultimateheads.head.HeadManager;
import de.stylextv.ultimateheads.lang.LanguageManager;
import de.stylextv.ultimateheads.main.Variables;
import de.stylextv.ultimateheads.permission.PermissionUtil;
import de.stylextv.ultimateheads.util.AsyncUtil;
import de.stylextv.ultimateheads.util.ItemUtil;
import de.stylextv.ultimateheads.util.MathUtil;

public class MainMenu extends Menu {
	
	private ArrayList<Player> viewers = new ArrayList<Player>();
	
	private boolean needsUpdate;
	
	@Override
	public void createInventory() {
		inv = Bukkit.createInventory(null, 9*5, getTitle());
	}
	@Override
	public void fillConstants() {
		for(int x=0; x<9; x++) {
			if(x!=4) setItem(x, 0, ItemUtil.BLANK);
			if(x!=3 && x!=5) setItem(x, getLastY(), ItemUtil.BLANK);
		}
		int maxY = getLastY()-1;
		for(int y=1; y<=maxY; y++) {
			if(y != (maxY+1)/2) {
				setItem(0, y, ItemUtil.BLANK);
				setItem(8, y, ItemUtil.BLANK);
			}
		}
		
		setItem(4, 0, ItemUtil.FAVORITES);
		setItem(3, getLastY(), ItemUtil.SEARCH_NEW);
		setItem(5, getLastY(), ItemUtil.SETTINGS);
	}
	
	private String getTitle() {
		int i = HeadManager.getTotalAmountOfHeads();
		return Variables.NAME+" ("+LanguageManager.parseMsg("general.word.head"+(i==1?"":"s"), MathUtil.formatInt(i))+")";
	}
	@SuppressWarnings("unchecked")
	public void updateTitle() {
		setTitle(getTitle());
		AsyncUtil.runSync(() -> {
			for(Player p:(ArrayList<Player>)viewers.clone()) {
				GuiManager.setOpenedMenu(p, this);
				openFor(p);
			}
		});
	}
	
	@Override
	public void updateDynamicContent() {
		if(viewers.size() != 0) {
			
			ArrayList<Category> categories = HeadManager.getCategories();
			int size = categories.size();
			for(int i=0; i<size; i++) {
				Category c = categories.get(i);
				int x = i%7;
				int y = i/7;
				
				setItem(1+x, 1+y, c.asItemStack());
			}
			
		} else needsUpdate=true;
	}
	
	public void openForViewer(Player p) {
		viewers.add(p);
		if(needsUpdate) {
			needsUpdate=false;
			updateDynamicContent();
		}
		openFor(p);
	}
	
	@Override
	public void onClick(Player p, InventoryClickEvent e) {
		int slot=e.getSlot();
		if(slot==4) {
			if(PermissionUtil.hasGuiPermission(p)) {
				playClickSound(p, true);
				AsyncUtil.runAsync(() -> {
					GuiManager.openHeadsListMenu(p, new HeadListMenu(p, ListType.FAVORITES, null, null));
				});
			} else {
				kickPlayerForNoPerm(p);
			}
		} else if(slot==getLastY()*9+3) {
			if(PermissionUtil.hasGuiPermission(p)) {
				playClickSound(p, true);
				closeInventory(p);
				HeadListMenu.startNewSearch(p);
			} else {
				kickPlayerForNoPerm(p);
			}
		} else if(slot==getLastY()*9+5) {
			if(PermissionUtil.hasGuiPermission(p) && (PermissionUtil.hasUpdatePermission(p) || PermissionUtil.hasConfigPermission(p))) {
				playClickSound(p, true);
				GuiManager.openSettingsMenu(p);
			} else {
				kickPlayerForNoPerm(p);
			}
		} else {
			int x=slot%9-1;
			int y=slot/9-1;
			
			if(x>=0 && x<7 && y>=0) {
				int i=y*7+x;
				ArrayList<Category> categories = HeadManager.getCategories();
				if(i >= 0 && i < categories.size()) {
					Category c=categories.get(i);
					if(PermissionUtil.hasGuiPermission(p) && PermissionUtil.hasCategoryPermission(p, c)) {
						playClickSound(p, true);
						AsyncUtil.runAsync(() -> {
							GuiManager.openHeadsListMenu(p, new HeadListMenu(p, c.isLatestPack()?ListType.PACK:ListType.CATEGORY, c, null));
						});
					} else {
						kickPlayerForNoPerm(p);
					}
				}
			}
		}
	}
	@Override
	public void onClose(Player p) {
		viewers.remove(p);
	}
	
}
