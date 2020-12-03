package de.stylextv.ultimateheads.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import de.stylextv.ultimateheads.head.Head;
import de.stylextv.ultimateheads.head.HeadManager;
import de.stylextv.ultimateheads.util.ItemUtil;

public class EventItem implements Listener {
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onItemSpawn(ItemSpawnEvent e) {
		ItemStack item = e.getEntity().getItemStack();
        if(item.hasItemMeta() && item.getItemMeta() instanceof SkullMeta) {
        	ItemMeta meta = item.getItemMeta();
        	if(meta.getDisplayName() == null || meta.getDisplayName().isEmpty()) {
    	        String encodeded = ItemUtil.getHeadValue(item);
    	        if(encodeded != null) {
    	        	String url = ItemUtil.headValueToUrl(encodeded, true);
    	        	if(url != null) {
    	        		Head h = HeadManager.getHeadByUrl(url);
        	        	if(h!=null) {
        	        		meta.setDisplayName(h.getDisplayName(false));
        	        		item.setItemMeta(meta);
        	        	}
    	        	}
    	        }
        	}
        }
	}
	
}
