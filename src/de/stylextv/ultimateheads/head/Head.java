package de.stylextv.ultimateheads.head;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

import de.stylextv.ultimateheads.lang.LanguageManager;
import de.stylextv.ultimateheads.util.ItemUtil;

public class Head {
	
	private int id;
    private String name;
    private String url;
    private String pack;
    
    private boolean staffPicked;
    private boolean local;
    private Category category;
    
    public Head(int id, String name, String url, String pack, boolean staffPicked, boolean local, Category category) {
    	this.id = id;
    	this.name = name;
    	this.url = url;
    	this.pack = pack;
    	
    	this.staffPicked = staffPicked;
    	this.local = local;
    	this.category = category;
    }
    
    public ItemStack asItemStack() {
    	return asItemStack(false, false);
    }
    public ItemStack asItemStack(boolean favorite, boolean info) {
    	String name = (favorite ? "§6⭐ " : "")+"§9"+this.name;
    	if(info) {
    		ArrayList<String> lore = new ArrayList<String>();
    		if(pack != null) {
    			lore.add("§8"+LanguageManager.parseMsg("gui.head.lore.pack", pack));
    		}
    		if(staffPicked) {
    			lore.add("§8"+LanguageManager.parseMsg("gui.head.lore.staff"));
    		}
			lore.add("§8ID: §7"+id);
    		if(local) {
    			lore.add("§8"+LanguageManager.parseMsg("gui.head.lore.delete"));
    		}
    		return ItemUtil.createItemStack(ItemUtil.headUrlToValue(url), name, lore);
    	} else return ItemUtil.createItemStack(ItemUtil.headUrlToValue(url), name);
    }
    
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getUrl() {
		return url;
	}
	public String getPack() {
		return pack;
	}
	
	public boolean isStaffPicked() {
		return staffPicked;
	}
	public boolean isLocal() {
		return local;
	}
	public Category getCategory() {
		return category;
	}
	
}
