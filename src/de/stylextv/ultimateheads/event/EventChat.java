package de.stylextv.ultimateheads.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import de.stylextv.ultimateheads.chat.ChatPromptManager;

public class EventChat implements Listener {
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		ChatPromptManager.onChat(e);
	}
	
}
