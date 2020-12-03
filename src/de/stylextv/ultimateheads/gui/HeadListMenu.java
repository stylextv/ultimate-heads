package de.stylextv.ultimateheads.gui;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import de.stylextv.ultimateheads.chat.ChatPromptManager;
import de.stylextv.ultimateheads.command.CommandHandler;
import de.stylextv.ultimateheads.head.Category;
import de.stylextv.ultimateheads.head.Head;
import de.stylextv.ultimateheads.head.HeadManager;
import de.stylextv.ultimateheads.lang.LanguageManager;
import de.stylextv.ultimateheads.main.Variables;
import de.stylextv.ultimateheads.permission.PermissionUtil;
import de.stylextv.ultimateheads.player.PlayerManager;
import de.stylextv.ultimateheads.util.AsyncUtil;
import de.stylextv.ultimateheads.util.ItemUtil;
import de.stylextv.ultimateheads.util.MathUtil;
import de.stylextv.ultimateheads.version.VersionUtil;

public class HeadListMenu extends Menu {
	
	private Player p;
	private ListType type;
	
	private MainMenu menu;
	
	private Category c;
	private String query;
	
	private List<Head> heads;
	private int page;
	private int pages;
	
	public HeadListMenu(Player p, ListType type, Category c, String query, MainMenu menu) {
		this.p = p;
		this.type = type;
		
		this.menu = menu;
		
		this.c = c;
		this.query = query;
		
		getInitialHeads();
		this.pages = (int) Math.ceil(heads.size()/45.0);
		if(this.pages < 1) this.pages = 1;
	}
	private void getInitialHeads() {
		switch(type) {
		case CATEGORY:
			heads = HeadManager.sortByFavorites(HeadManager.getHeadsByCategory(c), p);
			break;
		case FAVORITES:
			heads = HeadManager.getFavorites(p);
			break;
		case PACK:
			heads = HeadManager.sortByFavorites(HeadManager.getHeadsOfLatestPack(), p);
			break;
		case SEARCH:
			heads = HeadManager.sortByFavorites(HeadManager.getHeadsByQuery(query), p);
			break;
		}
	}
	
	@Override
	public void createInventory() {
		inv = Bukkit.createInventory(null, 9*6, getTitle());
	}
	@Override
	public void fillConstants() {
		setItem(3, 0, ItemUtil.SEARCH_NEW);
		if(heads.size() > 0) setItem(5, 0, ItemUtil.SEARCH_REFINE);
		else setItem(5, 0, ItemUtil.BLANK);
	}
	
	private String getTitle() {
		return getName()+" ("+MathUtil.formatInt(heads.size())+") "+LanguageManager.parseMsg("general.word.page")+" "+(page+1)+"/"+pages;
	}
	private String getName() {
		String name = null;
		switch(type) {
		case CATEGORY:
			name = c.getName();
			break;
		case FAVORITES:
			name = LanguageManager.parseMsg("general.word.favorites");
			break;
		case PACK:
			name = "Latest Pack";
			break;
		case SEARCH:
			name = LanguageManager.parseMsg("general.word.query")+": "+query;
			break;
		}
		return name;
	}
	public void updateTitle() {
		setTitle(getTitle());
		AsyncUtil.runSync(() -> {
			openFor(p);
		});
	}
	
	private void refineSearch() {
		ChatPromptManager.sendChatPrompt(p, LanguageManager.parseMsg("prompt.refine"), (answer) -> {
			if(answer != null) {
				String answerLower = answer.toLowerCase();
				if(query == null) {
					query = getName()+", "+answer;
					type = ListType.SEARCH;
				} else query = query+", "+answer;
				heads = heads.stream().filter(h -> HeadManager.doesHeadMatch(h, answerLower)).collect(Collectors.toList());
				pages = (int) Math.ceil(heads.size()/45.0);
				if(pages < 1) pages = 1;
				page = 0;
				fillConstants();
				updateDynamicContent();
				GuiManager.setOpenedMenu(p, this);
				updateTitle();
			} else p.sendMessage(Variables.PREFIX+LanguageManager.parseMsg("prompt.canceled"));
		});
	}
	
	@Override
	public void updateDynamicContent() {
		int start = page*45;
		List<String> favorites = PlayerManager.getFavorites(p);
		for(int i=0; i<45; i++) {
			int j=start+i;
			if(j < heads.size()) {
				Head h = heads.get(j);
				setItem(9+i, h.asItemStack(favorites.contains(h.getUrl()), true));
			} else setItem(9+i, ItemUtil.EMPTY);
		}
		
		setItem(4, 0, ItemUtil.createMenuItem(VersionUtil.getMcVersion()<VersionUtil.MC_1_13?Material.valueOf("EMPTY_MAP"):Material.MAP, page+1, LanguageManager.parseMsg("gui.item.pageback.name"), LanguageManager.parseMsg("gui.item.pageback.desc")));
		for(int i=1; i<=3; i++) {
			if(page+i < pages) setItem(5+i, getArrow(i, page+i+1, true, true));
			else setItem(5+i, ItemUtil.BLANK);
			if(page-i >= 0) setItem(3-i, getArrow(i, page-i+1, true, true));
			else setItem(3-i, ItemUtil.BLANK);
		}
	}
	public static ItemStack getArrow(int offset, int number, boolean right, boolean useItemCounter) {
		return ItemUtil.createMenuItem(Material.ARROW, useItemCounter?number:1, LanguageManager.parseMsg("gui.item.arrow.name", number), LanguageManager.parseMsg("gui.item."+(right?"right":"left")+".desc"+(offset==1?"1":"2"), offset));
	}
	
	@Override
	public void onClick(Player p, InventoryClickEvent e) {
		int slot=e.getSlot();
		if(slot==4) {
			if(PermissionUtil.hasGuiPermission(p)) {
				playClickSound(p, true);
				GuiManager.openMainMenu(p, menu);
			} else {
				kickPlayerForNoPerm(p);
			}
		} else if(slot==3) {
			if(PermissionUtil.hasGuiPermission(p)) {
				playClickSound(p, true);
				closeInventory(p);
				startNewSearch(p, menu);
			} else {
				kickPlayerForNoPerm(p);
			}
		} else if(slot==5) {
			if(heads.size() > 0) {
				if(PermissionUtil.hasGuiPermission(p)) {
					playClickSound(p, true);
					closeInventory(p);
					refineSearch();
				} else {
					kickPlayerForNoPerm(p);
				}
			}
		} else if(slot < 9) {
			if(PermissionUtil.hasGuiPermission(p)) {
				int a;
				if(slot < 4) a = -3+slot;
				else a = slot-5;
				int newPage = page+a;
				
				if(newPage >= 0 && newPage < pages) {
					playClickSound(p, true);
					page = newPage;
					updateTitle();
				}
			} else {
				kickPlayerForNoPerm(p);
			}
		} else {
			int index = slot-9;
			if(index >= 0) {
				int realIndex = index + page*45;
				if(realIndex < heads.size()) {
					Head h = heads.get(realIndex);
					if(PermissionUtil.hasGuiPermission(p) && PermissionUtil.hasCategoryPermission(p, h.getCategory())) {
						if(e.isShiftClick()) {
							playClickSound(p, true);
							PlayerManager.getPlayerEntry(p).toggleFavorite(h.getUrl());
							updateDynamicContent();
						} else if(e.getAction() == InventoryAction.CLONE_STACK) {
							if(h.isLocal()) {
								playClickSound(p, true);
								heads.remove(h);
								HeadManager.getLocalHeads().remove(h);
								if(HeadManager.getHeadsByCategory(h.getCategory()).isEmpty()) {
									HeadManager.getCategories().remove(h.getCategory());
								}
								GuiManager.updateMainMenu();
								updateTitle();
							} else {
								playClickSound(p, false);
							}
						} else {
							playClickSound(p, true);
							CommandHandler.giveItem(p, h.asItemStack());
						}
					} else {
						kickPlayerForNoPerm(p);
					}
				}
			}
		}
	}
	@Override
	public void onClose(Player p) {
	}
	
	public static void startNewSearch(Player p, MainMenu menu) {
		ChatPromptManager.sendChatPrompt(p, LanguageManager.parseMsg("prompt.search"), (answer) -> {
			if(answer != null) GuiManager.openHeadsListMenu(p, new HeadListMenu(p, ListType.SEARCH, null, answer, menu));
			else p.sendMessage(Variables.PREFIX+LanguageManager.parseMsg("prompt.canceled"));
		});
	}
	
	public MainMenu getMainMenu() {
		return menu;
	}
	
}
