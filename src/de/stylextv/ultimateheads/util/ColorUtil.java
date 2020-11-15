package de.stylextv.ultimateheads.util;

public class ColorUtil {
	
	private enum Color {
        C9("§9§l"), 
        CA("§a§l"), 
        CB("§b§l"), 
        C8("§8§l"), 
        CD("§d§l"), 
        CC("§c§l"), 
        C6("§6§l");
        
        String c;
        
        private Color(String c) {
            this.c = c;
        }
        
        public String getColor() {
            return this.c;
        }
    }
	
    public static String getRandomColor() {
        return Color.values()[MathUtil.RANDOM.nextInt(Color.values().length)].getColor();
    }
	
}
