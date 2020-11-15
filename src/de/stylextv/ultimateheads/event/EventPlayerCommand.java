package de.stylextv.ultimateheads.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import de.stylextv.ultimateheads.chat.ChatPromptManager;

public class EventPlayerCommand implements Listener {
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) {
		ChatPromptManager.cancelPrompt(e.getPlayer());
	}
	
}
