package de.stylextv.ultimateheads.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import de.stylextv.ultimateheads.permission.PermissionUtil;

public class MainTabCompleter implements TabCompleter {
	
	private static final ArrayList<String> COMMAND_SUGGESTIONS = new ArrayList<String>();
	
	private static final ArrayList<String> PLAYER_SUGGESTIONS = new ArrayList<String>();
	private static final ArrayList<String> HEAD_TYPE_SUGGESTIONS = new ArrayList<String>();
	private static final ArrayList<String> HEAD_ID_SUGGESTIONS = new ArrayList<String>();
	
	private static final ArrayList<String> URL_SUGGESTIONS = new ArrayList<String>();
	private static final ArrayList<String> HEAD_NAME_SUGGESTIONS = new ArrayList<String>();
	private static final ArrayList<String> CATEGORY_SUGGESTIONS = new ArrayList<String>();
	
	static {
		COMMAND_SUGGESTIONS.add("help");
		COMMAND_SUGGESTIONS.add("info");
		COMMAND_SUGGESTIONS.add("update");
		COMMAND_SUGGESTIONS.add("gui");
		COMMAND_SUGGESTIONS.add("search");
		COMMAND_SUGGESTIONS.add("give");
		COMMAND_SUGGESTIONS.add("base64");
		COMMAND_SUGGESTIONS.add("url");
		COMMAND_SUGGESTIONS.add("add");
		
		PLAYER_SUGGESTIONS.add("(player/all)");
		HEAD_TYPE_SUGGESTIONS.add("(global/local)");
		HEAD_ID_SUGGESTIONS.add("(head_id)");
		
		URL_SUGGESTIONS.add("(url)");
		HEAD_NAME_SUGGESTIONS.add("(name)");
		CATEGORY_SUGGESTIONS.add("(category)");
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player && (cmd.getName().equalsIgnoreCase("uh")||cmd.getName().equalsIgnoreCase("heads")||cmd.getName().equalsIgnoreCase("ultimateheads"))) {
			Player p = (Player) sender;
			if(PermissionUtil.hasUpdatePermission(p)||PermissionUtil.hasGuiPermission(p)||PermissionUtil.hasSearchPermission(p)||PermissionUtil.hasGivePermission(p)||PermissionUtil.hasBase64Permission(p)||PermissionUtil.hasUrlPermission(p)||PermissionUtil.hasAddPermission(p)) {
				if(args.length==1) {
					return COMMAND_SUGGESTIONS;
				} else if(args.length>1) {
					if(args[0].equalsIgnoreCase("give")) {
						
						if(args.length==2) return PLAYER_SUGGESTIONS;
						else if(args.length==3) return HEAD_TYPE_SUGGESTIONS;
						else if(args.length==4) return HEAD_ID_SUGGESTIONS;
						
					} else if(args[0].equalsIgnoreCase("add")) {
						
						if(args.length==2) return URL_SUGGESTIONS;
						else if(args.length==3) return HEAD_NAME_SUGGESTIONS;
						else if(args.length==4) return CATEGORY_SUGGESTIONS;
						
					}
				}
			}
		}
		return null;
	}
	
}
