package de.stylextv.ultimateheads.util;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;

import de.stylextv.ultimateheads.config.ConfigManager;

public class MathUtil {
	
	public static final Random RANDOM = new Random();
	
	private static NumberFormat NUMBER_FORMAT;
	static {
		createLocale();
	}
	public static String formatInt(int i) {
		return NUMBER_FORMAT.format(i).replace((char)160, '.');
	}
	
	public static void createLocale() {
		Locale l = Locale.ENGLISH;
		int i = ConfigManager.VALUE_LANGUAGE.getCurrentIndex();
		if(i==1) l = Locale.GERMAN;
		else if(i==2) l = Locale.FRANCE;
		NUMBER_FORMAT = NumberFormat.getInstance(l);
	}
	
}
