package de.stylextv.ultimateheads.util;

import org.bukkit.scheduler.BukkitRunnable;

import de.stylextv.ultimateheads.main.Main;

public class AsyncUtil {
	
	public static void runAsync(Runnable r) {
		new BukkitRunnable() {
			@Override
			public void run() {
				r.run();
			}
		}.runTaskAsynchronously(Main.getPlugin());
	}
	public static void runSync(Runnable r) {
		new BukkitRunnable() {
			@Override
			public void run() {
				r.run();
			}
		}.runTask(Main.getPlugin());
	}
	
}
