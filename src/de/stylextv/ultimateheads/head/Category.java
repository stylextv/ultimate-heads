package de.stylextv.ultimateheads.head;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import de.stylextv.ultimateheads.lang.LanguageManager;
import de.stylextv.ultimateheads.util.ColorUtil;
import de.stylextv.ultimateheads.util.ItemUtil;
import de.stylextv.ultimateheads.util.MathUtil;

public class Category {
	
	private String name;
	private boolean latestPack;
	
	public Category(String name) {
		this.name = name;
		this.latestPack = false;
	}
	public Category(String name, boolean latestPack) {
		this.name = name;
		this.latestPack = latestPack;
	}
	
	public ItemStack asItemStack() {
		List<Head> heads = latestPack?HeadManager.getHeadsOfLatestPack():HeadManager.getHeadsByCategory(this);
		Head firstHead = heads.get(0);
		String name = ColorUtil.getRandomColor()+this.name;
		int i = heads.size();
		return ItemUtil.createItemStack(ItemUtil.headUrlToValue(firstHead.getUrl()), name, latestPack?"§e"+firstHead.getPack():"§e"+LanguageManager.parseMsg("gui.category.lore"+(i==1?"2":"1"), MathUtil.formatInt(i)));
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean isLatestPack() {
		return this.latestPack;
	}
	
}
