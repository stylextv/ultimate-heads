package de.stylextv.ultimateheads.util;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;

public class MathUtil {
	
	public static final Random RANDOM = new Random();
	
	private static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance(Locale.ENGLISH);
	public static String formatInt(int i) {
		return NUMBER_FORMAT.format(i);
	}
	
}
