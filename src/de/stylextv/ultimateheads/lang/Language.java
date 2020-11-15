package de.stylextv.ultimateheads.lang;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Language {
	
	private HashMap<String, String> translations = new HashMap<String, String>();
	
	public Language(String id) {
		try {
			
			InputStream input = Language.class.getResourceAsStream("/assets/langs/"+id+".json");
			BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
			
			String[] keyChain = new String[6];
			String line;
			while((line=reader.readLine())!=null) {
				if(line.length() != 0) {
					if(line.endsWith(":")) {
						String key = line.substring(0, line.length()-1);
						int lBefore = key.length();
						key = key.replace(" ", "");
						int level = (lBefore-key.length())/2;
						keyChain[level] = key;
					} else {
						String[] split = line.split(": ", 2);
						String key = split[0];
						int lBefore = key.length();
						key = key.replace(" ", "");
						int level = (lBefore-key.length())/2;
						String trans = split[1];
						String longKey = "";
						for(int i=0; i<level; i++) {
							longKey = longKey+keyChain[i]+".";
						}
						longKey = longKey+key;
						translations.put(longKey, trans);
					}
				}
			}
			
			reader.close();
			
		} catch (Exception ex) {
			System.err.println("Failed to load the following language: " + id);
			ex.printStackTrace();
		}
	}
	
	public String getTranslation(String id) {
		return translations.get(id);
	}
	
}
