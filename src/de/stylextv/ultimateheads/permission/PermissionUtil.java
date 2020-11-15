package de.stylextv.ultimateheads.permission;

import org.bukkit.entity.Player;

import de.stylextv.ultimateheads.head.Category;

public class PermissionUtil {
	
	public static boolean hasUpdatePermission(Player p) {
		return hasStarPermission(p)||p.hasPermission("ultimateheads.update");
	}
	public static boolean hasConfigPermission(Player p) {
		return hasStarPermission(p)||p.hasPermission("ultimateheads.config");
	}
	public static boolean hasGuiPermission(Player p) {
		return hasStarPermission(p)||p.hasPermission("ultimateheads.gui");
	}
	public static boolean hasSearchPermission(Player p) {
		return hasStarPermission(p)||p.hasPermission("ultimateheads.search");
	}
	public static boolean hasGivePermission(Player p) {
		return hasStarPermission(p)||p.hasPermission("ultimateheads.give");
	}
	public static boolean hasBase64Permission(Player p) {
		return hasStarPermission(p)||p.hasPermission("ultimateheads.base64");
	}
	public static boolean hasUrlPermission(Player p) {
		return hasStarPermission(p)||p.hasPermission("ultimateheads.url");
	}
	public static boolean hasAddPermission(Player p) {
		return hasStarPermission(p)||p.hasPermission("ultimateheads.add");
	}
	
	public static boolean hasCategoryPermission(Player p, Category c) {
		return hasStarPermission(p)||p.hasPermission("ultimateheads.category.*")||p.hasPermission("ultimateheads.category."+c.getName().replace(" ", "_").toLowerCase());
	}
	
	private static boolean hasStarPermission(Player p) {
		return p.hasPermission("ultimateheads.*");
	}
	
}
