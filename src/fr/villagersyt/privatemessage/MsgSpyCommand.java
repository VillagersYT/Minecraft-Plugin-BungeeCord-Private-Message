package fr.villagersyt.privatemessage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.ProxyServer;

public class MsgSpyCommand extends Command {
    private final Map<ProxiedPlayer, ProxiedPlayer> lastMessaged = new HashMap<>();
    private final Set<ProxiedPlayer> spyPlayers = new HashSet<>();

    public MsgSpyCommand() {
        super("spymsg", "msg.spy");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;

            if (spyPlayers.contains(player)) {
                spyPlayers.remove(player);
                player.sendMessage("§fMode espion des messages §cdésactivé§f.");
            } else {
                spyPlayers.add(player);
                player.sendMessage("§fMode espion des messages §aactivé§f.");
            }
        } else if (sender == ProxyServer.getInstance().getConsole()) {
            if (args.length >= 1) {
                ProxiedPlayer recipient = ProxyServer.getInstance().getPlayer(args[0]);
                if (recipient != null) {
                    toggleSpy(recipient);
                    sender.sendMessage("§fMode espion des messages " + (spyPlayers.contains(recipient) ? "§cdésactivé§f." : "§aactivé§f."));
                } else {
                    sender.sendMessage("§cLe joueur n' est pas en ligne ou n' existe pas.");
                }
            } else {
                sender.sendMessage("§cUtilisation correcte : /spymsg <joueur>");
            }
        } else {
            sender.sendMessage("§cCette commande doit être exécutée par un joueur ou la console.");
        }
    }

    public void toggleSpy(ProxiedPlayer player) {
        if (spyPlayers.contains(player)) {
            spyPlayers.remove(player);
        } else {
            spyPlayers.add(player);
        }
    }

    public void replyToLastMessage(ProxiedPlayer player, String[] args) {
        if (lastMessaged.containsKey(player)) {
            ProxiedPlayer lastSender = lastMessaged.get(player);
            StringBuilder message = new StringBuilder();
            for (String arg : args) {
                message.append(arg).append(" ");
            }
            String msg = message.toString().trim();
            lastSender.sendMessage("§fMessage de §e" + player.getName() + " §f(reply) §f: §f" + msg);
            player.sendMessage("§fMessage envoyé par§e " + lastSender.getName() + "§f :§f " + msg);
        } else {
            player.sendMessage("§cAucune conversation récente à laquelle répondre.");
        }
    }
}
