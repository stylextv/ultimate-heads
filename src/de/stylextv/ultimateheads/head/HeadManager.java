package de.stylextv.ultimateheads.head;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import de.stylextv.ultimateheads.gui.GuiManager;
import de.stylextv.ultimateheads.main.Variables;
import de.stylextv.ultimateheads.player.PlayerManager;
import de.stylextv.ultimateheads.util.AsyncUtil;
import de.stylextv.ultimateheads.util.ItemUtil;

public class HeadManager {
	
	private static File HEADS_FILE = new File("plugins/UltimateHeads/heads.json");
	
	private static ArrayList<Head> heads = new ArrayList<Head>();
	private static ArrayList<Head> localHeads = new ArrayList<Head>();
	private static ArrayList<Category> categories = new ArrayList<Category>();
	
	private static String latestPackName;
	
	public static void loadHeads() {
		if(HEADS_FILE.exists()) {
			
			loadHeads(loadHeadsFromDisk(), true);
			AsyncUtil.runAsync(() -> {
				JSONArray json = downloadHeads();
				saveHeadsToDisk(json);
			});
			
		} else {
			
			JSONArray json = downloadHeads();
			loadHeads(json, false);
			saveHeadsToDisk(json);
			
		}
	}
	
	private static void loadHeads(JSONArray json, boolean fromDisk) {
        categories.add(new Category("Latest Pack", true));
        for(Object o : json) {
        	JSONObject jsonObj = (JSONObject) o;
        	String name = (String) jsonObj.get("name");
        	if(name != null) {
        		int id = Integer.parseInt((String) jsonObj.get("id"));
            	String url = (String) jsonObj.get("url");
            	String tags = (String) jsonObj.get("tags");
            	String pack = (String) jsonObj.get("pack");
            	boolean staffPicked = Integer.parseInt((String) jsonObj.get("staff_picked")) == 1;
            	
            	Category category = getOrAddCategory(tags);
            	
            	heads.add(new Head(id, name, url, pack, staffPicked, false, category));
        	}
        }
		
        int l=json.size();
		if(fromDisk) Bukkit.getConsoleSender().sendMessage(Variables.PREFIX_CONSOLE+"Successfully loaded §a"+l+"§r heads from disk.");
		else Bukkit.getConsoleSender().sendMessage(Variables.PREFIX_CONSOLE+"Successfully loaded §a"+l+"§r heads.");
	}
	
	public static JSONArray downloadHeads() {
        try {
            final InputStream is = new URL("http://www.head-db.com/dump").openStream();
            final BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            final String jsonText = readAll(rd);
            final JSONParser parser = new JSONParser();
            final JSONArray json = (JSONArray) parser.parse(jsonText);
            
    		return json;
        }
        catch (Exception ex) {
    		Bukkit.getConsoleSender().sendMessage(Variables.PREFIX_CONSOLE+"Failed to §cdownload§7 heads.");
        }
        return null;
	}
	private static String readAll(final Reader rd) throws IOException {
        final StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char)cp);
        }
        return sb.toString();
    }
	
	private static void saveHeadsToDisk(JSONArray json) {
        try {
    		final FileWriter fw = new FileWriter(HEADS_FILE.getPath());
    		fw.write(json.toJSONString());
    		fw.close();
        }
        catch (Exception ex) {
    		Bukkit.getConsoleSender().sendMessage(Variables.PREFIX_CONSOLE+"Failed to §csave§7 heads to disk.");
        }
	}
	private static JSONArray loadHeadsFromDisk() {
        try {
        	final JSONParser parser = new JSONParser();
        	FileReader fr = new FileReader(HEADS_FILE.getPath());
        	JSONArray json = (JSONArray) parser.parse(fr);
        	fr.close();
            return json;
        }
        catch (Exception ex) {
    		Bukkit.getConsoleSender().sendMessage(Variables.PREFIX_CONSOLE+"Failed to §cload§7 heads from disk.");
        }
        return null;
	}
	
	public static Category getOrAddCategory(String name) {
		Optional<Category> categoryOptional = categories.stream().filter(t -> t.getName().equalsIgnoreCase(name)).findFirst();
		Category category = categoryOptional.orElseGet(() -> new Category(name));
		if(!categoryOptional.isPresent()) {
			categories.add(category);
			GuiManager.updateMainMenu();
		}
		
		return category;
	}
	public static void addLocalHead(Head h) {
		localHeads.add(h);
		GuiManager.updateMainMenu();
	}
	public static int removeLocalHead(String name, boolean fromPlayers) {
		List<Head> results = localHeads.stream().filter(h -> h.getName().equals(name)&&(!fromPlayers||h.getCategory().getName().equalsIgnoreCase("Player Heads"))).collect(Collectors.toList());
		int id=-1;
		for(Head h:results) {
			id = h.getId();
			localHeads.remove(h);
		}
		if(id != -1) GuiManager.updateMainMenu();
		return id;
	}
	public static int getNextLocalId() {
		if(localHeads.isEmpty()) return 1;
		else {
			int max=0;
			for(Head h:localHeads) {
				if(max==0 || h.getId()>max) max=h.getId();
			}
			return max+1;
		}
	}
	
	public static List<Head> getFavorites(Player p) {
		List<String> favorites = PlayerManager.getFavorites(p);
		return getAllHeads().stream().filter(head -> favorites.contains(head.getUrl())).collect(Collectors.toList());
	}
	public static List<Head> sortByFavorites(List<Head> heads, Player p) {
		List<String> favorites = PlayerManager.getFavorites(p);
		return heads.stream().sorted(Comparator.comparing(head -> !favorites.contains(head.getUrl()))).collect(Collectors.toList());
	}
	public static List<Head> getHeadsOfLatestPack() {
		String latestPackName = getNameOfLatestPack();
		if(latestPackName!=null) {
			Stream<Head> stream = heads.stream().filter(h -> latestPackName.equals(h.getPack()));
			stream = stream.sorted((Head h1, Head h2) -> h1.getName().toLowerCase().compareTo(h2.getName().toLowerCase()));
			stream = stream.sorted((Head h1, Head h2) -> ((Boolean)h2.isStaffPicked()).compareTo(h1.isStaffPicked()));
			return stream.collect(Collectors.toList());
		}
		return null;
	}
	public static String getNameOfLatestPack() {
		if(latestPackName != null) return latestPackName;
		
		for(int i=heads.size()-1; i>=0; i--) {
			Head h = heads.get(i);
			if(h.getPack() != null) {
				latestPackName = h.getPack();
				return latestPackName;
			}
		}
		return null;
	}
	public static List<Head> getHeadsByCategory(Category c) {
		return getAllHeads().stream().filter(h -> h.getCategory().equals(c)).collect(Collectors.toList());
	}
	public static List<Head> getHeadsByQuery(String query) {
		String queryLower = query.toLowerCase();
		List<Head> results = getAllHeads().stream().filter(h -> doesHeadMatch(h, queryLower)).collect(Collectors.toList());
		return results;
	}
	public static boolean doesHeadMatch(Head h, String queryLower) {
		return h.getName().toLowerCase().contains(queryLower)
				|| h.getCategory().getName().toLowerCase().contains(queryLower)
				|| (h.getPack() != null && h.getPack().toLowerCase().contains(queryLower));
	}
	public static Head getHeadById(int id, boolean local) {
		Optional<Head> optional;
		if(local) optional = localHeads.stream().filter(h -> h.getId()==id).findFirst();
		else optional = heads.stream().filter(h -> h.getId()==id).findFirst();
		if(optional.isPresent()) return optional.get();
		return null;
	}
	public static Head getHeadByUrl(String url) {
		Optional<Head> optional = getAllHeads().stream().filter(h -> h.getUrl().equals(url)).findFirst();
		if(optional.isPresent()) return optional.get();
		return null;
	}
	public static List<Head> getAllHeads() {
		Stream<Head> stream = Stream.concat(heads.stream(), localHeads.stream());
		stream = stream.sorted((Head h1, Head h2) -> h1.getName().toLowerCase().compareTo(h2.getName().toLowerCase()));
		stream = stream.sorted((Head h1, Head h2) -> ((Boolean)h2.isStaffPicked()).compareTo(h1.isStaffPicked()));
		return stream.collect(Collectors.toList());
    }
	public static ArrayList<Head> getGlobalHeads() {
		return heads;
	}
	public static ArrayList<Head> getLocalHeads() {
		return localHeads;
	}
	
	public static void addPlayersToLocalHeads() {
		for(Player all:Bukkit.getOnlinePlayers()) {
			addPlayerToLocalHeads(all);
		}
	}
	public static void addPlayerToLocalHeads(Player p) {
		String name = p.getName();
		int id=removeLocalHead(name, true);
		if(id==-1) id=HeadManager.getNextLocalId();
		addLocalHead(new Head(id, name, ItemUtil.headValueToUrl(ItemUtil.getHeadValue(p), true), null, false, true, HeadManager.getOrAddCategory("Player Heads")));
	}
	public static Head getPlayerHead(Player p) {
		String name = p.getName();
		Optional<Head> optional = localHeads.stream().filter(h -> h.getCategory().getName().equalsIgnoreCase("Player Heads")&&h.getName().equals(name)).findFirst();
		if(optional.isPresent()) return optional.get();
		
		int id=HeadManager.getNextLocalId();
		Head h = new Head(id, name, ItemUtil.headValueToUrl(ItemUtil.getHeadValue(p), true), null, false, true, HeadManager.getOrAddCategory("Player Heads"));
		addLocalHead(h);
		return h;
	}
	
	public static ArrayList<Category> getCategories() {
		return categories;
	}
	
	public static int getTotalAmountOfHeads() {
		return heads.size() + localHeads.size();
	}
	
}
