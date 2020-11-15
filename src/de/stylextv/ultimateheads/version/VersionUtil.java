package de.stylextv.ultimateheads.version;

import org.bukkit.Bukkit;

import de.stylextv.ultimateheads.lang.LanguageManager;
import de.stylextv.ultimateheads.main.Variables;

public class VersionUtil {
	
	public static final int MC_1_8=0;
	public static final int MC_1_9=1;
	public static final int MC_1_10=2;
	public static final int MC_1_11=3;
	public static final int MC_1_12=4;
	public static final int MC_1_13=5;
	public static final int MC_1_14=6;
	public static final int MC_1_15=7;
	public static final int MC_1_16=8;
	
	private static int mcVersion = MC_1_14;
	
	public static void onEnable() {
		String version=Bukkit.getServer().getVersion();
		mcVersion=Integer.valueOf(version.split("MC: 1\\.")[1].split("\\.")[0])-8;
		if(mcVersion<MC_1_8||mcVersion>MC_1_16) {
			Bukkit.getConsoleSender().sendMessage(Variables.PREFIX_CONSOLE+LanguageManager.parseMsg("trans.console.error.wrongversion1", version));
			Bukkit.getConsoleSender().sendMessage(Variables.PREFIX_CONSOLE+LanguageManager.parseMsg("trans.console.error.wrongversion2"));
		}
	}
	
	public static int getMcVersion() {
		return mcVersion;
	}
	
}
