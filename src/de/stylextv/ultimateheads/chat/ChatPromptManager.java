package de.stylextv.ultimateheads.chat;

import java.util.HashMap;
import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import de.stylextv.ultimateheads.main.Variables;

public class ChatPromptManager {
	
	private static final HashMap<Player, Consumer<String>> OPEN_PROMPTS = new HashMap<Player, Consumer<String>>();
	
	public static void sendChatPrompt(Player p, String msg, Consumer<String> result) {
		p.sendMessage(Variables.PREFIX+msg);
		OPEN_PROMPTS.put(p, result);
	}
	
	public static void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		Consumer<String> result = OPEN_PROMPTS.remove(p);
		if(result != null) {
			e.setCancelled(true);
			result.accept(e.getMessage());
		}
	}
	
	public static void cancelPrompt(Player p) {
		Consumer<String> result = OPEN_PROMPTS.remove(p);
		if(result != null) {
			result.accept(null);
		}
	}
	public static void removePlayer(Player p) {
		OPEN_PROMPTS.remove(p);
	}
	
}
