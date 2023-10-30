package fr.villagersyt.privatemessage;

import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin {

	private static Main instance;
	
	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {
		instance = this;
		
		getProxy().getConsole().sendMessage("§8[§ePrivate Message§8] §aLe plugin viens de s' allumer correctement.");
		getProxy().getPluginManager().registerCommand(this, new MsgCommand());
		getProxy().getPluginManager().registerCommand(this, new ReplyCommand());
		getProxy().getPluginManager().registerCommand(this, new MsgSpyCommand());
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onDisable() {
		
		getProxy().getConsole().sendMessage("§8[§ePrivate Message§8] §cLe plugin viens de s' eteindre correctement.");
	}
	
	public static Main getInstance() {
		
		return instance;
	}
}