package net.mayateck.BuyCommand;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.plugins.Economy_BOSE7;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuyCommandHandler implements CommandExecutor{
	public BuyCommand plugin;
	
	public BuyCommandHandler(BuyCommand plugin){
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		List<String> aliases = Arrays.asList("buycommand", "bcmd", "bc", "buycmd");
		if (aliases.contains(cmd.getName())){
			if (args.length>0){
				Player p = (Player)s;
				if (args[0].equalsIgnoreCase("info")){
					s.sendMessage("#===# BuyCommand v"+BuyCommand.ver+" by Wehttam664 #===#");
					s.sendMessage(BuyCommand.head+"Normally info would be here.");
					s.sendMessage(BuyCommand.head+"It's on my TO-DO list. :\\");
					return true;
				} else {
					Set<String> nodes = plugin.getConfig().getConfigurationSection("buyables").getKeys(false);
					String nodepath = "";
					String nodename = "";
					for (String node : nodes){
						if (plugin.getConfig().getString("buyables."+node+".name").equalsIgnoreCase(args[0])){
							nodepath = "buyables."+node+".";
							nodename = node;
						}
					}
					if (nodepath==""){
						s.sendMessage(BuyCommand.head+"Sorry, that buyable doesn't exist.");
					} else {
						Economy eco = new Economy_BOSE7(plugin);
						if (eco.has(p.getName(), plugin.getConfig().getDouble(nodepath+"cost"))){
							if (args.length==1+plugin.getConfig().getInt(nodepath+"arguments")){
								if (s.hasPermission("buycommand.buy."+nodename)){
									eco.withdrawPlayer(p.getName(), plugin.getConfig().getDouble(nodepath+"cost"));
									List<String> cmds = plugin.getConfig().getStringList(nodepath+"commands");
									for(String command : cmds){
										String newcmd = String.format(command, s.getName());
										int iterator = 1;
										for(String arg : args){
											if ((!arg.equalsIgnoreCase(args[0])) && iterator==1){
												newcmd.replaceAll(":arg"+iterator+":", arg);
												iterator++;
											}
										}
										plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), newcmd);
										plugin.getLogger().info(s.getName()+" ran command "+args[0]+".");
									}
								} else {
									s.sendMessage(BuyCommand.head+"Sorry, the argument number was incorrect.");
								}
							} else {
								s.sendMessage(BuyCommand.head+"Sorry, the argument number was incorrect.");
							}
						} else {
							s.sendMessage(BuyCommand.head+"Sorry, you can't afford this buyable.");
						}
					}
					return true;
				}
			} else {
				s.sendMessage("#===# BuyCommand v"+BuyCommand.ver+" by Wehttam664 #===#");
				s.sendMessage(BuyCommand.head+"Normally help would be here.");
				s.sendMessage(BuyCommand.head+"It's on my TO-DO list. :\\");
				// TODO write help.
				return true;
			}
		}
		return false;
	}

}
