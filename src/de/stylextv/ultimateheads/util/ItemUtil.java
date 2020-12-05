package de.stylextv.ultimateheads.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import de.stylextv.ultimateheads.lang.LanguageManager;
import de.stylextv.ultimateheads.version.VersionUtil;

public class ItemUtil {
	
	private static final Class<Object> CRAFTPLAYER_CLASS = Reflections.getUntypedClass("{obc}.entity.CraftPlayer");
	private static final Reflections.MethodInvoker GETPROFILE_METHOD = Reflections.getTypedMethod(CRAFTPLAYER_CLASS, "getProfile", GameProfile.class);
	private static Field PROFILE_FIELD;
	
	public static ItemStack BLANK;
	public static ItemStack EMPTY;
	
	public static ItemStack PAGE_RIGHT;
	public static ItemStack HEAD_BACK;
	
	public static ItemStack SETTINGS;
	
	public static ItemStack SETTINGS_CONFIG;
	public static ItemStack SETTINGS_UPDATE;
	
	public static ItemStack CONFIG_DEFAULT;
	
	public static ItemStack SEARCH_NEW;
	public static ItemStack SEARCH_REFINE;
	public static ItemStack FAVORITES;
	
	static {
		create();
	}
	public static void create() {
		Material comparatorMaterial;
		if(VersionUtil.getMcVersion() < VersionUtil.MC_1_13) {
			comparatorMaterial = Material.valueOf("REDSTONE_COMPARATOR");
			BLANK = createItemStack(Material.valueOf("STAINED_GLASS_PANE"), 15, "§r");
		} else {
			comparatorMaterial = Material.COMPARATOR;
			BLANK = createItemStack(Material.BLACK_STAINED_GLASS_PANE, "§r");
		}
		EMPTY = createItemStack(Material.AIR);
		
		SEARCH_NEW = createMenuItem(Material.COMPASS, LanguageManager.parseMsg("gui.item.searchnew.name"), LanguageManager.parseMsg("gui.item.searchnew.desc"));
		SEARCH_REFINE = createMenuItem(Material.COMPASS, LanguageManager.parseMsg("gui.item.searchrefine.name"), LanguageManager.parseMsg("gui.item.searchrefine.desc"));
		FAVORITES = createMenuItem(Material.GOLDEN_APPLE, LanguageManager.parseMsg("gui.item.favorites.name"), LanguageManager.parseMsg("gui.item.favorites.desc"));
		
		HEAD_BACK = createMenuItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWE2Nzg3YmEzMjU2NGU3YzJmM2EwY2U2NDQ5OGVjYmIyM2I4OTg0NWU1YTY2YjVjZWM3NzM2ZjcyOWVkMzcifX19", LanguageManager.parseMsg("gui.item.back.name"), LanguageManager.parseMsg("gui.item.back.desc"));
		
		SETTINGS = createMenuItem(comparatorMaterial, LanguageManager.parseMsg("gui.item.settings.name"), LanguageManager.parseMsg("gui.item.settings.desc"));
		
		SETTINGS_CONFIG = createMenuItem(Material.BOOKSHELF, LanguageManager.parseMsg("gui.item.config.name"), LanguageManager.parseMsg("gui.item.config.desc"));
		String updateTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzA0MGZlODM2YTZjMmZiZDJjN2E5YzhlYzZiZTUxNzRmZGRmMWFjMjBmNTVlMzY2MTU2ZmE1ZjcxMmUxMCJ9fX0=";
		String msg = null;
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH)+1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		if(month == 10) {
			msg = "* Happy Halloween *";
			updateTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDVhZGI2ZmZhMmM1YzBlMzUwYzI4NDk5MTM4YTU1NjY0NDFkN2JjNTczZGUxOTg5ZmRlMjEyZmNiMTk2NjgyNiJ9fX0=";
		} else if(month == 12 && day <= 26) {
			msg = "* Happy Christmas *";
			updateTexture = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNlZjlhYTE0ZTg4NDc3M2VhYzEzNGE0ZWU4OTcyMDYzZjQ2NmRlNjc4MzYzY2Y3YjFhMjFhODViNyJ9fX0=";
		} else if(month == 12 && day == 31) {
			updateTexture = null;
			SETTINGS_UPDATE = createMenuItem(Material.FIREWORK_ROCKET, LanguageManager.parseMsg("gui.item.update.name"), LanguageManager.parseMsg("gui.item.update.desc"), "", "§8* Happy New Year *", "");
		}
		if(updateTexture!=null) {
			if(msg == null) SETTINGS_UPDATE = createMenuItem(updateTexture, LanguageManager.parseMsg("gui.item.update.name"), LanguageManager.parseMsg("gui.item.update.desc"));
			else SETTINGS_UPDATE = createMenuItem(updateTexture, LanguageManager.parseMsg("gui.item.update.name"), LanguageManager.parseMsg("gui.item.update.desc"), "", "§8"+msg, "");
		}
		
		CONFIG_DEFAULT = createMenuItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmE4NTk3MWZiMTNiZjBiNzhlYjlmOTZiMmJkY2UxYTExMzMxMzczZGUzMGQ5MjM5ZThiYzA2YTI5MTJjNGE0In19fQ==", LanguageManager.parseMsg("gui.item.restoreconfig.name"), LanguageManager.parseMsg("gui.item.restoreconfig.desc"));
	}
	
	public static ItemStack createConfigMinusItemStack() {
		return createItemStack("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ4YTk5ZGIyYzM3ZWM3MWQ3MTk5Y2Q1MjYzOTk4MWE3NTEzY2U5Y2NhOTYyNmEzOTM2Zjk2NWIxMzExOTMifX19", LanguageManager.parseMsg("gui.item.option.minus.name"), LanguageManager.parseMsg("gui.item.option.minus.desc1"),LanguageManager.parseMsg("gui.item.option.minus.desc2"),"",LanguageManager.parseMsg("gui.item.option.plusminus.desc1","-"),LanguageManager.parseMsg("gui.item.option.plusminus.desc2","-"));
	}
	public static ItemStack createConfigPlusItemStack() {
		return createItemStack("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2VkZDIwYmU5MzUyMDk0OWU2Y2U3ODlkYzRmNDNlZmFlYjI4YzcxN2VlNmJmY2JiZTAyNzgwMTQyZjcxNiJ9fX0=", LanguageManager.parseMsg("gui.item.option.plus.name"), LanguageManager.parseMsg("gui.item.option.plus.desc1"),LanguageManager.parseMsg("gui.item.option.plus.desc2"),"",LanguageManager.parseMsg("gui.item.option.plusminus.desc1","+"),LanguageManager.parseMsg("gui.item.option.plusminus.desc2","+"));
	}
	public static ItemStack createConfigOptionLeftItemStack() {
		return createMenuItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ==", LanguageManager.parseMsg("gui.item.option.left.name"), LanguageManager.parseMsg("gui.item.option.left.desc"));
	}
	public static ItemStack createConfigOptionRightItemStack() {
		return createMenuItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTliZjMyOTJlMTI2YTEwNWI1NGViYTcxM2FhMWIxNTJkNTQxYTFkODkzODgyOWM1NjM2NGQxNzhlZDIyYmYifX19", LanguageManager.parseMsg("gui.item.option.right.name"), LanguageManager.parseMsg("gui.item.option.right.desc"));
	}
	
	public static ItemStack createMenuItem(Material m, int amount, String name, String lore) {
		return createItemStack(m, amount, name, splitLore(lore));
	}
	public static ItemStack createMenuItem(Material m, String name, String lore) {
		return createItemStack(m, name, splitLore(lore));
	}
	public static ItemStack createMenuItem(Material m, String name, String lore, String... extraLore) {
		return createItemStack(m, name, splitLore(lore, extraLore));
	}
	public static ItemStack createMenuItem(String texture, String name, String lore) {
		return createItemStack(texture, name, splitLore(lore));
	}
	public static ItemStack createMenuItem(String texture, String name, String lore, String... extraLore) {
		return createItemStack(texture, name, splitLore(lore, extraLore));
	}
	private static ArrayList<String> splitLore(String lore, String... extraLore) {
		int m=27;
		ArrayList<String> list = new ArrayList<String>();
		
		String currentLine=null;
		for(String word:lore.split(" ")) {
			if(currentLine == null) currentLine=word;
			else if(currentLine.length()+word.length()+1<=m) currentLine += " "+word;
			else {
				list.add("§7"+currentLine);
				currentLine=word;
			}
		}
		if(!currentLine.isEmpty()) list.add("§7"+currentLine);
		
		for(String extraLine:extraLore) {
			list.add(extraLine);
		}
		return list;
	}
	
	public static ItemStack createItemStack(Material m) {
		ItemStack itemStack = new ItemStack(m);
		return itemStack;
	}
	public static ItemStack createItemStack(Material m, int amount) {
		ItemStack itemStack = new ItemStack(m, amount);
		return itemStack;
	}
	public static ItemStack createItemStack(Material m, String name) {
		ItemStack itemStack = new ItemStack(m);
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(name);
		itemStack.setItemMeta(meta);
		return itemStack;
	}
	public static ItemStack createItemStack(Material m, String name, String... lore) {
		ItemStack itemStack = new ItemStack(m);
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(name);
		ArrayList<String> list = new ArrayList<String>();
		for(String s : lore) list.add(s);
		meta.setLore(list);
		itemStack.setItemMeta(meta);
		return itemStack;
	}
	public static ItemStack createItemStack(Material m, String name, ArrayList<String> lore) {
		ItemStack itemStack = new ItemStack(m);
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		itemStack.setItemMeta(meta);
		return itemStack;
	}
	public static ItemStack createItemStack(Material m, int amount, String name, ArrayList<String> lore) {
		ItemStack itemStack = new ItemStack(m, amount);
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		itemStack.setItemMeta(meta);
		return itemStack;
	}
	@SuppressWarnings("deprecation")
	public static ItemStack createItemStack(Material m, int data, String name, String... lore) {
		ItemStack itemStack = new ItemStack(m, 1, (short) data);
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(name);
		ArrayList<String> list = new ArrayList<String>();
		for(String s : lore) list.add(s);
		meta.setLore(list);
		itemStack.setItemMeta(meta);
		return itemStack;
	}
	@SuppressWarnings("deprecation")
	public static ItemStack createItemStack(Material m, int data, String name) {
		ItemStack itemStack = new ItemStack(m, 1, (short) data);
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(name);
		itemStack.setItemMeta(meta);
		return itemStack;
	}
	public static ItemStack createItemStack(String texture, String name, String... lore) {
		ArrayList<String> list = new ArrayList<String>();
		for(String s : lore) list.add(s);
		return createItemStack(texture, name, list);
	}
	@SuppressWarnings("deprecation")
	public static ItemStack createItemStack(String texture, String name, ArrayList<String> lore) {
		ItemStack itemStack;
		if(VersionUtil.getMcVersion() < VersionUtil.MC_1_13) {
			itemStack = new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short) 3);
		} else {
			itemStack = new ItemStack(Material.PLAYER_HEAD);
		}
		SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
		meta.setDisplayName(name);
		
		meta.setLore(lore);
		
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", texture));
        try {
            if(PROFILE_FIELD == null) {
            	PROFILE_FIELD = meta.getClass().getDeclaredField("profile");
            	PROFILE_FIELD.setAccessible(true);
            }
            PROFILE_FIELD.set(meta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
        
		itemStack.setItemMeta(meta);
		return itemStack;
	}
	
	public static String headUrlToValue(String url) {
		String s = "{\"textures\":{\"SKIN\":{\"url\":\"http://textures.minecraft.net/texture/"+url+"\"}}}";
		return Base64.getEncoder().encodeToString(s.getBytes());
	}
	public static String headValueToUrl(String value) {
		String s = new String(Base64.getDecoder().decode(value)).replace(" ", "");
		return s.split("url\":\"")[1].split("\"")[0];
	}
	public static String headValueToUrl(String value, boolean trim) {
		String s = headValueToUrl(value);
		if(trim) s=s.replaceFirst("http://textures.minecraft.net/texture/", "");
		return s;
	}
	public static String getHeadValue(ItemStack item) {
        try {
            SkullMeta skullMeta = (SkullMeta)item.getItemMeta();
            Field profileField = skullMeta.getClass().getDeclaredField("profile");
            if(profileField == null) {
                return null;
            }
            profileField.setAccessible(true);
            final GameProfile profile = (GameProfile)profileField.get(skullMeta);
            final Iterator<Property> iterator = profile.getProperties().get("textures").iterator();
            return iterator.hasNext() ? iterator.next().getValue() : null;
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex) {
            return null;
        }
	}
	public static String getHeadValue(Player p) {
        try {
            final Iterator<Property> iterator = ((GameProfile)GETPROFILE_METHOD.invoke(p)).getProperties().get("textures").iterator();
            if(iterator.hasNext()) {
            	return iterator.next().getValue();
            } else {
            	String uuid = getPlayerUUID(p.getName());
            	return getHeadValue(uuid);
            }
        } catch (IllegalArgumentException | SecurityException | IOException ex) {
            return null;
        }
	}
	private static String getPlayerUUID(String name) throws IOException {
		URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
		InputStreamReader reader = new InputStreamReader(url.openStream());
		return new JsonParser().parse(reader).getAsJsonObject().get("id").getAsString();
	}
	private static String getHeadValue(String uuid) throws IOException {
		URL url_1 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
        InputStreamReader reader_1 = new InputStreamReader(url_1.openStream());
        JsonObject textureProperty = new JsonParser().parse(reader_1).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
        return textureProperty.get("value").getAsString();
	}
	
}
