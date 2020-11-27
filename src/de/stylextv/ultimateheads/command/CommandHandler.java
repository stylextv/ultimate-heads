package de.stylextv.ultimateheads.command;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import de.stylextv.ultimateheads.gui.GuiManager;
import de.stylextv.ultimateheads.gui.HeadListMenu;
import de.stylextv.ultimateheads.head.Category;
import de.stylextv.ultimateheads.head.Head;
import de.stylextv.ultimateheads.head.HeadManager;
import de.stylextv.ultimateheads.lang.LanguageManager;
import de.stylextv.ultimateheads.main.Main;
import de.stylextv.ultimateheads.main.Variables;
import de.stylextv.ultimateheads.permission.PermissionUtil;
import de.stylextv.ultimateheads.util.AsyncUtil;
import de.stylextv.ultimateheads.util.ItemUtil;
import de.stylextv.ultimateheads.version.VersionUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class CommandHandler {
	
	private static String UI_TEXT_LINE;
	static {
		if(VersionUtil.getMcVersion()<VersionUtil.MC_1_13) {
			UI_TEXT_LINE = "---------------------------------";
		} else {
			UI_TEXT_LINE = "⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯";
		}
	}
	
	@SuppressWarnings("deprecation")
	public static boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p=(Player) sender;
			boolean hasPermUpdate=PermissionUtil.hasUpdatePermission(p);
			boolean hasPermGui=PermissionUtil.hasGuiPermission(p);
			boolean hasPermSearch=PermissionUtil.hasSearchPermission(p);
			boolean hasPermGive=PermissionUtil.hasGivePermission(p);
			boolean hasPermBase64=PermissionUtil.hasBase64Permission(p);
			boolean hasPermUrl=PermissionUtil.hasUrlPermission(p);
			boolean hasPermAdd=PermissionUtil.hasAddPermission(p);
			
			if(hasPermUpdate || hasPermGui || hasPermSearch || hasPermGive || hasPermBase64 || hasPermUrl || hasPermAdd) {
				if(args.length == 0) {
					if(hasPermGui) {
						AsyncUtil.runAsync(() -> GuiManager.openMainMenu(p, 0));
					} else sendHelpSuggestion(p);
				} else {
					
					String subCommand=args[0];
					if(subCommand.equalsIgnoreCase("help")) {
						sendHelp(p);
					} else if(subCommand.equalsIgnoreCase("info")) {
						sendInfo(p);
					} else if(subCommand.equalsIgnoreCase("update")) {
						if(hasPermUpdate) {
							if(args.length==1) {
								Main.getPlugin().runAutoUpdater(p);
							} else p.sendMessage(Variables.PREFIX+LanguageManager.parseMsg("command.use.general", "/uh update"));
						} else sendNoPermission(p);
					} else if(subCommand.equalsIgnoreCase("gui")) {
						if(hasPermGui) {
							if(args.length==1) {
								AsyncUtil.runAsync(() -> GuiManager.openMainMenu(p, 0));
							} else p.sendMessage(Variables.PREFIX+LanguageManager.parseMsg("command.use.general", "/uh gui"));
						} else sendNoPermission(p);
					} else if(subCommand.equalsIgnoreCase("search")) {
						if(hasPermGui && hasPermSearch) {
							if(args.length==1) {
								HeadListMenu.startNewSearch(p, 0);
							} else p.sendMessage(Variables.PREFIX+LanguageManager.parseMsg("command.use.general", "/uh search"));
						} else sendNoPermission(p);
					} else if(subCommand.equalsIgnoreCase("give")) {
						if(hasPermGive) {
							if(args.length==4) {
								String type = args[2];
								boolean local = false;
								if(type.equalsIgnoreCase("local")) {
									local = true;
								} else if(!type.equalsIgnoreCase("global")) {
									p.sendMessage(Variables.PREFIX+LanguageManager.parseMsg("command.use.general", "/uh give (player/all) (global/local) (head_id)"));
									return false;
								}
								try {
									int id = Integer.parseInt(args[3]);
									Head h = HeadManager.getHeadById(id, local);
									if(h == null) {
										p.sendMessage(Variables.PREFIX+LanguageManager.parseMsg("command.error.headnotfound", id));
									} else {
										if(!PermissionUtil.hasCategoryPermission(p, h.getCategory())) {
											sendNoPermission(p);
											return false;
										}
										
										ItemStack item = h.asItemStack();
										String name = args[1];
										if(name.equalsIgnoreCase("all")) {
											for(Player all:Bukkit.getOnlinePlayers()) {
												giveItem(all, item);
												all.sendMessage(Variables.PREFIX+LanguageManager.parseMsg("command.give.receive", h.getName()));
											}
											p.sendMessage(Variables.PREFIX+LanguageManager.parseMsg("command.give.given", LanguageManager.parseMsg("general.word.everyone"), h.getName()));
										} else {
											Player receiver = Bukkit.getPlayer(name);
											if(receiver == null) p.sendMessage(Variables.PREFIX+LanguageManager.parseMsg("command.error.playernotfound"));
											else {
												giveItem(receiver, item);
												p.sendMessage(Variables.PREFIX+LanguageManager.parseMsg("command.give.given", receiver.getName(), h.getName()));
												receiver.sendMessage(Variables.PREFIX+LanguageManager.parseMsg("command.give.receive", h.getName()));
											}
										}
									}
								} catch(NumberFormatException ex) {
									p.sendMessage(Variables.PREFIX+LanguageManager.parseMsg("command.error.headnotfound", args[3]));
								}
							} else p.sendMessage(Variables.PREFIX+LanguageManager.parseMsg("command.use.general", "/uh give (player/all) (global/local) (head_id)"));
						} else sendNoPermission(p);
					} else if(subCommand.equalsIgnoreCase("base64")) {
						if(hasPermBase64) {
							if(args.length==1) {
								ItemStack item = p.getItemInHand();
						        if(item.hasItemMeta() && item.getItemMeta() instanceof SkullMeta) {
							        String encodeded = ItemUtil.getHeadValue(item);
							        if(encodeded != null) {
							        	sendClickableText(p, "§r"+encodeded, LanguageManager.parseMsg("command.hover.copy"), Action.COPY_TO_CLIPBOARD, encodeded);
							        } else p.sendMessage(Variables.PREFIX+LanguageManager.parseMsg("command.error.novalidhead"));
						        } else p.sendMessage(Variables.PREFIX+LanguageManager.parseMsg("command.error.novalidhead"));
							} else p.sendMessage(Variables.PREFIX+LanguageManager.parseMsg("command.use.general", "/uh base64"));
						} else sendNoPermission(p);
					} else if(subCommand.equalsIgnoreCase("url")) {
						if(hasPermUrl) {
							if(args.length==1) {
								ItemStack item = p.getItemInHand();
						        if(item.hasItemMeta() && item.getItemMeta() instanceof SkullMeta) {
							        String encodeded = ItemUtil.getHeadValue(item);
							        if(encodeded != null) {
							        	String url = ItemUtil.headValueToUrl(encodeded);
							        	sendClickableText(p, "§r"+url, LanguageManager.parseMsg("command.hover.open"), Action.OPEN_URL, url);
							        } else p.sendMessage(Variables.PREFIX+LanguageManager.parseMsg("command.error.novalidhead"));
						        } else p.sendMessage(Variables.PREFIX+LanguageManager.parseMsg("command.error.novalidhead"));
							} else p.sendMessage(Variables.PREFIX+LanguageManager.parseMsg("command.use.general", "/uh url"));
						} else sendNoPermission(p);
					} else if(subCommand.equalsIgnoreCase("add")) {
						if(hasPermAdd) {
							if(args.length==4) {
								String url = args[1];
								String name = args[2];
								Category c = HeadManager.getOrAddCategory(args[3]);
								if(PermissionUtil.hasCategoryPermission(p, c)) {
									HeadManager.addLocalHead(new Head(HeadManager.getNextLocalId(), name, url, null, false, true, c));
									p.sendMessage(Variables.PREFIX+LanguageManager.parseMsg("command.add.added", name));
								} else sendNoPermission(p);
							} else p.sendMessage(Variables.PREFIX+LanguageManager.parseMsg("command.use.general", "/uh add (url) (name) (category)"));
						} else sendNoPermission(p);
					} else sendHelpSuggestion(p);
					
				}
			} else {
				sendNoPermission(p);
			}
			
		} else sender.sendMessage(Variables.PREFIX_CONSOLE+LanguageManager.parseMsg("command.error.noplayer"));
		return false;
	}
	
	private static void sendHelpSuggestion(Player p) {
		p.sendMessage(Variables.PREFIX+LanguageManager.parseMsg("command.use.help"));
	}
	public static void sendNoPermission(Player p) {
		p.sendMessage(Variables.PREFIX+LanguageManager.parseMsg("command.error.noperm"));
	}
	private static void sendHelp(Player p) {
		p.sendMessage(
				Variables.COLOR2+"§m#"+Variables.COLOR1+"§m"+UI_TEXT_LINE+Variables.COLOR2+"§m#"+"§r\n"+
				"                 "+Variables.COLOR1+Variables.NAME+"§r\n \n"+
				"§8- "+Variables.COLOR1+LanguageManager.parseMsg("command.help.line1")+"§r\n"+
				"§8- "+Variables.COLOR1+LanguageManager.parseMsg("command.help.line2")+"§r\n"+
				"§8- "+Variables.COLOR1+LanguageManager.parseMsg("command.help.line3")+"§r\n"+
				"   "+Variables.COLOR1+LanguageManager.parseMsg("command.help.line4")+"§r\n"+
				"§8- "+Variables.COLOR1+LanguageManager.parseMsg("command.help.line5")+"§r\n"+
				"§8- "+Variables.COLOR1+LanguageManager.parseMsg("command.help.line6")+"§r\n"+
				"§8- "+Variables.COLOR1+LanguageManager.parseMsg("command.help.line7")+"§r\n"+
				"§8   "+LanguageManager.parseMsg("command.help.line8")+"§r\n"+
				"§8- "+Variables.COLOR1+LanguageManager.parseMsg("command.help.line9")+"§r\n"+
				"§8- "+Variables.COLOR1+LanguageManager.parseMsg("command.help.line10")+"§r\n"+
				"§8- "+Variables.COLOR1+LanguageManager.parseMsg("command.help.line11")+"§r\n \n"+
				"§7(): "+Variables.COLOR1+LanguageManager.parseMsg("general.word.required")+"§7, []: "+Variables.COLOR1+LanguageManager.parseMsg("general.word.optional")+"§r\n"+
				LanguageManager.parseMsg("command.help.needsperm")+"§r\n \n"+
				Variables.COLOR2+"§m#"+Variables.COLOR1+"§m"+UI_TEXT_LINE+Variables.COLOR2+"§m#"
		);
	}
	private static void sendInfo(Player p) {
		p.sendMessage(
				Variables.COLOR2+"§m#"+Variables.COLOR1+"§m"+UI_TEXT_LINE+Variables.COLOR2+"§m#"+"§r\n"+
				Variables.COLOR1+LanguageManager.parseMsg("command.info.title")+"§r\n \n"+
				LanguageManager.parseMsg("command.info.line1")+"§r\n"+
				LanguageManager.parseMsg("command.info.line2", Variables.NAME)+"§r\n \n"+
				LanguageManager.parseMsg("command.info.line3", Variables.AUTHOR)+"§r\n"+
				LanguageManager.parseMsg("command.info.line4", Variables.VERSION)+"§r\n \n"+
				Variables.COLOR2+"§m#"+Variables.COLOR1+"§m"+UI_TEXT_LINE+Variables.COLOR2+"§m#"
		);
	}
	
	@SuppressWarnings("deprecation")
	private static void sendClickableText(Player p, String s, String hover, ClickEvent.Action action, String value) {
		TextComponent comp = new TextComponent(TextComponent.fromLegacyText(Variables.PREFIX));
		TextComponent extra = new TextComponent(TextComponent.fromLegacyText(s));
		extra.setClickEvent(new ClickEvent(action, value));
		if(VersionUtil.getMcVersion() <= VersionUtil.MC_1_11) {
			extra.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()));
		} else {
			extra.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(new TextComponent(hover)).create()));
		}
		comp.addExtra(extra);
		p.spigot().sendMessage(comp);
	}
	public static void giveItem(Player p, ItemStack item) {
		HashMap<Integer, ItemStack> overflow = p.getInventory().addItem(item);
		if(!overflow.isEmpty()) {
			for(ItemStack item2 : overflow.values()) {
				AsyncUtil.runSync(() -> p.getWorld().dropItemNaturally(p.getLocation(), item2));
			}
		}
	}
	
}
