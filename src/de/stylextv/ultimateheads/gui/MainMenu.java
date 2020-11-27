package de.stylextv.ultimateheads.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import de.stylextv.ultimateheads.head.Category;
import de.stylextv.ultimateheads.head.Head;
import de.stylextv.ultimateheads.head.HeadManager;
import de.stylextv.ultimateheads.lang.LanguageManager;
import de.stylextv.ultimateheads.main.Variables;
import de.stylextv.ultimateheads.permission.PermissionUtil;
import de.stylextv.ultimateheads.util.AsyncUtil;
import de.stylextv.ultimateheads.util.ItemUtil;
import de.stylextv.ultimateheads.util.MathUtil;

public class MainMenu extends Menu {
	
	private Player p;
	
	private int page;
	
	public MainMenu(Player p, int page) {
		this.p = p;
		this.page = page;
	}
	
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
	public void updateTitle() {
		setTitle(getTitle());
		AsyncUtil.runSync(() -> {
			openFor(p);
		});
	}
	
	@Override
	public void updateDynamicContent() {
		ArrayList<Category> categories = HeadManager.getCategories();
		List<Head> heads = HeadManager.getAllHeads();
		HashMap<Category, Integer> headCounts = new HashMap<Category, Integer>();
		HashMap<Category, Head> firstHeads = new HashMap<Category, Head>();
		String latestPackName = HeadManager.getNameOfLatestPack();
		int packCount=0;
		Head packHead=null;
		for(Head h:heads) {
			Category c = h.getCategory();
			Integer n = headCounts.get(c);
			if(n==null) {
				n=1;
				firstHeads.put(c, h);
			} else n++;
			headCounts.put(c, n);
			
			if(latestPackName.equals(h.getPack())) {
				if(packHead == null) {
					packCount=1;
					packHead=h;
				} else {
					packCount++;
				}
			}
		}
		
		int size = categories.size();
		int pages = (int) Math.ceil(size/21.0);
		if(pages < 1) pages = 1;
		if(page >= pages) page = pages-1;
		
		int k=page*21;
		int l=size-k;
		if(l>21) l=21;
		for(int i=0; i<21; i++) {
			
			int x = i%7;
			int y = i/7;
			if(i<l) {
				Category c = categories.get(i+k);
				
				int n;
				Head firstHead;
				if(c.isLatestPack()) {
					n=packCount;
					firstHead=packHead;
				} else {
					n=headCounts.get(c);
					firstHead=firstHeads.get(c);
				}
				setItem(1+x, 1+y, c.asItemStack(n, firstHead));
			} else setItem(1+x, 1+y, ItemUtil.EMPTY);
			
		}
		
		int arrowY = getLastY()/2;
		if(page==0) setItem(0, arrowY, ItemUtil.BLANK);
		else setItem(0, arrowY, HeadListMenu.getArrow(1, page, false, false));
		if(page+1 < pages) setItem(8, arrowY, HeadListMenu.getArrow(1, page+2, true, false));
		else setItem(8, arrowY, ItemUtil.BLANK);
	}
	
	@Override
	public void onClick(Player p, InventoryClickEvent e) {
		int slot=e.getSlot();
		if(slot==4) {
			if(PermissionUtil.hasGuiPermission(p)) {
				playClickSound(p, true);
				AsyncUtil.runAsync(() -> {
					GuiManager.openHeadsListMenu(p, new HeadListMenu(p, ListType.FAVORITES, null, null, page));
				});
			} else {
				kickPlayerForNoPerm(p);
			}
		} else if(slot==getLastY()*9+3) {
			if(PermissionUtil.hasGuiPermission(p)) {
				playClickSound(p, true);
				closeInventory(p);
				HeadListMenu.startNewSearch(p, page);
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
			
			ArrayList<Category> categories = HeadManager.getCategories();
			int size = categories.size();
			int pages = (int) Math.ceil(size/21.0);
			if(pages < 1) pages = 1;
			
			int arrowY = getLastY()/2;
			
			if(slot==arrowY*9) {
				if(page!=0) {
					if(PermissionUtil.hasGuiPermission(p)) {
						playClickSound(p, true);
						page--;
						updateDynamicContent();
					} else {
						kickPlayerForNoPerm(p);
					}
				}
			} else if(slot==arrowY*9+8) {
				if(page+1 < pages) {
					if(PermissionUtil.hasGuiPermission(p)) {
						playClickSound(p, true);
						page++;
						updateDynamicContent();
					} else {
						kickPlayerForNoPerm(p);
					}
				}
			} else {
				
				int x=slot%9-1;
				int y=slot/9-1;
				
				if(x>=0 && x<7 && y>=0) {
					int i=y*7+x;
					int k=page*21;
					int l=size-k;
					if(i >= 0 && i < l) {
						Category c=categories.get(i+k);
						if(PermissionUtil.hasGuiPermission(p) && PermissionUtil.hasCategoryPermission(p, c)) {
							playClickSound(p, true);
							AsyncUtil.runAsync(() -> {
								GuiManager.openHeadsListMenu(p, new HeadListMenu(p, c.isLatestPack()?ListType.PACK:ListType.CATEGORY, c, null, page));
							});
						} else {
							kickPlayerForNoPerm(p);
						}
					}
				}
				
			}
			
		}
	}
	@Override
	public void onClose(Player p) {
	}
	
}
