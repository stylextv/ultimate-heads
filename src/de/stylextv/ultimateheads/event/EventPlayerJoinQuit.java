package de.stylextv.ultimateheads.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.stylextv.ultimateheads.chat.ChatPromptManager;
import de.stylextv.ultimateheads.head.HeadManager;

public class EventPlayerJoinQuit implements Listener {
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent e) {
		Player p=e.getPlayer();
		HeadManager.addPlayerToLocalHeads(p);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p=e.getPlayer();
		ChatPromptManager.removePlayer(p);
	}
	
}
