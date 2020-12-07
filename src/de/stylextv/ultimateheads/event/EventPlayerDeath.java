package de.stylextv.ultimateheads.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import de.stylextv.ultimateheads.config.ConfigManager;
import de.stylextv.ultimateheads.head.Head;
import de.stylextv.ultimateheads.head.HeadManager;
import de.stylextv.ultimateheads.util.MathUtil;

public class EventPlayerDeath implements Listener {
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player p=e.getEntity();
		int chance = ConfigManager.VALUE_PLAYERHEAD_DROP_CHANCE.getValue();
		if(chance!=0 && !e.getKeepInventory() && p.getKiller() instanceof Player && (chance==100 || chance < MathUtil.RANDOM.nextInt(100))) {
			Head h = HeadManager.getPlayerHead(p);
			if(h != null) p.getWorld().dropItemNaturally(p.getLocation(), h.asItemStack());
		}
	}
	
}
