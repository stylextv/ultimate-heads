package de.stylextv.ultimateheads.lang;

import de.stylextv.ultimateheads.config.ConfigManager;
import de.stylextv.ultimateheads.gui.GuiManager;
import de.stylextv.ultimateheads.util.ItemUtil;
import de.stylextv.ultimateheads.util.MathUtil;

public class LanguageManager {
	
	private static final Language[] LANGUAGES = new Language[5];
	
	private static boolean inPluginRefresh = false;
	
	public static String getTranslation(String id) {
		int index = ConfigManager.VALUE_LANGUAGE.getCurrentIndex();
		Language l = LANGUAGES[index];
		if(l == null) {
			l = new Language(ConfigManager.VALUE_LANGUAGE.getOptions()[index]);
			LANGUAGES[index] = l;
		}
		
		return l.getTranslation(id);
	}
	
	public static void refreshPlugin() {
		if(!inPluginRefresh) {
			inPluginRefresh = true;
			
			MathUtil.createLocale();
			ItemUtil.create();
			GuiManager.updateTranslations();
			
			inPluginRefresh = false;
		}
	}
	
	public static String parseMsg(String s, Object... arguments) {
		String trans = getTranslation(s);
		if(trans != null) {
			if(arguments.length != 0) for(int i=0; i<arguments.length; i++) {
				trans = trans.replace("%"+i+"%", arguments[i].toString());
			}
			return trans;
		}
		return s;
	}
	
}
