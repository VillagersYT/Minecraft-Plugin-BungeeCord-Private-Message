package fr.villagersyt.privatemessage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.ProxyServer;

public class ReplyCommand extends Command {

    private final Map<ProxiedPlayer, ProxiedPlayer> lastMessaged = new HashMap<>();
    private final Set<ProxiedPlayer> spyPlayers = new HashSet<>();

    public ReplyCommand() {
        super("r");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;

            if (args.length >= 1) {
                ProxiedPlayer recipient = lastMessaged.get(player);

                if (recipient != null) {
                    StringBuilder message = new StringBuilder();
                    for (int i = 0; i < args.length; i++) {
                        message.append(args[i]).append(" ");
                    }

                    String msg = message.toString().trim();
                    recipient.sendMessage("§a§lMessages privés §7» §e" + player.getName() + " §7: §f" + msg);
                    player.sendMessage("§a§lMessages privés §7» §e" + recipient.getName() + " §7: §f" + msg);

                    // Si un administrateur est en mode espion, enregistrez la conversation
                    if (spyPlayers.contains(player)) {
                        ProxyServer.getInstance().getConsole().sendMessage("§8[§eSpyMsg§8] §e" + player.getName() + " §7-> " + recipient.getName() + "§7 : §f" + msg);
                    }
                } else {
                    player.sendMessage("§cAucune conversation récente à laquelle répondre.");
                }
            } else {
                player.sendMessage("§cUtilisation correcte : /r <message>");
            }
        } else {
            // Si la commande est exécutée depuis la console
            sender.sendMessage("§cCette commande doit être exécutée par un joueur.");
        }
    }

    public void toggleSpy(ProxiedPlayer player) {
        if (spyPlayers.contains(player)) {
            spyPlayers.remove(player);
            player.sendMessage("§fMode espion des messages désactivé.");
        } else {
            spyPlayers.add(player);
            player.sendMessage("§fMode espion des messages activé.");
        }
    }
}