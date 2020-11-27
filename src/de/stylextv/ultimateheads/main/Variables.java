package de.stylextv.ultimateheads.main;

import de.stylextv.ultimateheads.version.VersionUtil;
import net.md_5.bungee.api.ChatColor;

public class Variables {
	
	public static String NAME="UltimateHeads";
	
	public static String COLOR1="§c";
	public static String COLOR2="§4";
	
	public static String PREFIX;
	public static String PREFIX_CONSOLE=NAME+" | ";
	
	public static String AUTHOR;
	public static String VERSION;
	
	public static int METRICS_ID=9420;
	
	public static void loadScheme() {
		if(VersionUtil.getMcVersion() >= VersionUtil.MC_1_16) {
			COLOR1 = ChatColor.of("#E91E45").toString();
			COLOR2 = ChatColor.of("#871129").toString();
		}
		PREFIX=COLOR1+"§lUH §8> §7";
	}
	
}
